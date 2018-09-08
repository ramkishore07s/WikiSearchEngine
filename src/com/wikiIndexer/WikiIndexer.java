package com.wikiIndexer;

/**
 *
 */

import javafx.geometry.Pos;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.math.BigInteger;
import java.util.regex.Pattern;

import java.util.*;
import java.io.*;


public class WikiIndexer {

    private int max_len = 10000000;
    private int sub_str_len = 25;
    private int max_mem_limit = 5000;

    private SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
    private StopWords s = new StopWords();

    private ArrayList<String> doc_names = new ArrayList<>();
    private HashMap<String, String> stemmed = new HashMap<>();

    // iNVERTED INDICES
    private ArrayList<Posting> textPostings = new ArrayList<>();
    private ArrayList<Posting> linkPostings = new ArrayList<>();
    private ArrayList<Posting> refPostings = new ArrayList<>();
    private ArrayList<Posting> catPostings = new ArrayList<>();
    private ArrayList<Posting> titlePostings = new ArrayList<>();

    private String tmpDir = "tmp__";
    private Integer tmp_file_no = 0;

    private double time = System.currentTimeMillis();
    private int file_no = - 1;
    private int pos = 0;
    private double max_mem = 0;

    private Pattern link_pattern = Pattern.compile("http://([a-zA-Z0-9\\.]*/)+");
    private Pattern ref_pattern = Pattern.compile("ref[^a-z].*");
    private Pattern cat_pattern = Pattern.compile("Category:.*");

    WikiIndexer(Integer max_mem_limit_) {
        max_mem_limit = max_mem_limit_;
        //clean or create tmp__ dir for storing intermediate files
        File directory = new File(tmpDir);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (!file.delete()) System.out.println("Failed to delete " + file);
            }
        } else {
            if (!directory.mkdir()) {
                System.out.println("ERROR: cannot create directory");
            }
        }
    }

    public void index(String body, String title) {

        double mem = (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024* 1024);

        if (file_no % 10000 == 0 || mem > max_mem_limit) System.gc();
        if (textPostings.size() > max_len || mem > max_mem_limit) clearAll();

        printStatus(title, mem, file_no);
        if (mem > max_mem) max_mem = mem;

        HashMap<String, Integer> text_index = new HashMap<>();
        HashMap<String, Integer> link_index = new HashMap<>();
        HashMap<String, Integer> ref_index = new HashMap<>();
        HashMap<String, Integer> cat_index = new HashMap<>();
        HashMap<String, Integer> title_index = new HashMap<>();

        file_no += 1;
        doc_names.add(file_no + ":" + title + "\n");

        //Split by paragraphs
        String[] parts = body.split("(\\n\\n)|(\\|)");

        for (int i=0; i<parts.length; ++i) {
            String[] tokens = parts[i].split("[^A-Za-z]");
            Boolean link, cat, ref;
            if (parts[i].length() > sub_str_len) {
                link = link_pattern.matcher(parts[i].substring(0, sub_str_len)).find();
                cat = cat_pattern.matcher(parts[i].substring(0, sub_str_len)).find();
                ref = ref_pattern.matcher(parts[i].substring(0, sub_str_len)).find();
            }
            else {
                link = link_pattern.matcher(parts[i]).find();
                cat = cat_pattern.matcher(parts[i]).find();
                ref = ref_pattern.matcher(parts[i]).find();
            }

            //text Index
            for (String a : tokens) {
                a = stemWord(a);
                if (a.length() > 2) { text_index.putIfAbsent(a, 0); text_index.computeIfPresent(a, (key_, value_) -> { return value_ + 1; }); }
                if (ref && a.length() > 2) { ref_index.putIfAbsent(a, 0); ref_index.computeIfPresent(a, (key_, value_) -> { return value_ + 1; }); }
                if (cat && a.length() > 2) { cat_index.putIfAbsent(a, 0); cat_index.computeIfPresent(a, (key_, value_) -> { return value_ + 1; }); }
                if (link && a.length() > 2) { link_index.putIfAbsent(a, 0); link_index.computeIfPresent(a, (key_, value_) -> { return value_ + 1; });  }
            }
        }

        //title
        String[] tokens = title.split("[^A-Za-z]");
        for (String a: tokens) {
            a = stemWord(a);
            if (a.length() > 2) { title_index.putIfAbsent(a, 0); title_index.computeIfPresent(a,  (key_, value_) -> { return value_ + 1; }); }
        }

        updatePostings(textPostings, text_index);
        updatePostings(linkPostings, link_index);
        updatePostings(catPostings, cat_index);
        updatePostings(refPostings, ref_index);
        updatePostings(titlePostings, title_index);
    }


    private void clearAll() {
        System.out.println("\nPeak memory usage: " + max_mem + " MB");
        System.out.println("Creating tmp file " + tmp_file_no + "...\n");

        writePostings(textPostings, "text");
        writePostings(refPostings, "ref");
        writePostings(linkPostings, "link");
        writePostings(catPostings, "cat");
        writePostings(titlePostings, "title");

        writeDocNames();

        textPostings.clear();
        linkPostings.clear();
        refPostings.clear();
        catPostings.clear();
        titlePostings.clear();

        doc_names.clear();

        stemmed = new HashMap<>();
        tmp_file_no += 1;

        max_mem = 0;
        System.gc();
        time = System.currentTimeMillis() - time;
        System.out.print("Time: "); System.out.print(time/1000. + "s\n") ;
    }

    private String stemWord(String a) {
//        if (a.length() > 12) { a = a.substring(0, 12); }
        a = a.toLowerCase();                                                    //case folding
        if(!s.isStopword(a)) {                                                  //Stop word removal
            stemmed.computeIfAbsent(a, k -> {
                String d = (String) stemmer.stem(k);
                if (d.length() > 12) d = d.substring(0, 12);
                return (String) d;});
            return stemmed.get(a);
        }
        return "";
    }

    private void printStatus(String title, double mem, int file_no) {
        System.out.print("\rMemory in use: ");
        System.out.print(mem + " MB | ");
        System.out.print("Processing: " + file_no + " " + title);
    }

    private void writePostings(ArrayList<Posting> textPostings, String type) {
        Collections.sort(textPostings, new PostingComparator());
        try {
            OutputStream outputStream = new FileOutputStream(tmpDir + "/" + type + "_" + tmp_file_no.toString());
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

            String prev = "";
            for (Posting p : textPostings) {
                if (!p.word.equalsIgnoreCase(prev)){
                    prev = p.word;
                    outputStreamWriter.write("\n");
                    outputStreamWriter.write(p.word);
                    outputStreamWriter.write(":");
                }
                outputStreamWriter.write(p.string_());
                outputStreamWriter.write("|");
            }
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDocNames() {
        try {
            OutputStream outputStream = new FileOutputStream(tmpDir + "/names_" + tmp_file_no.toString());
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

            String prev = "";
            for (String p : doc_names) {
                outputStreamWriter.write(p);
            }
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePostings(ArrayList<Posting> postings, HashMap<String, Integer> index) {
        Iterator iterator = index.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) iterator.next();
            postings.add(new Posting(pair.getKey(), file_no, pair.getValue()));
        }
    }

    public void cleanUp() {
        clearAll();
    }
}
