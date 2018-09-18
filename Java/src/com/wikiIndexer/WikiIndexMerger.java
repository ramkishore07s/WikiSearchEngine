package com.wikiIndexer;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Need to optimize!!!
 * Implement a interface to not read line by line and maybe a multi way merge
 */

public class WikiIndexMerger {
    private int buf_size = 1024 * 1024;
    private String inp_folder = "tmp__/";
    private String out_folder = "output__/";
    private String final_index = "postings";
    private String text_ = "text_";
    private int no_files = new File(inp_folder).listFiles().length/6;
    private String final_output;


    WikiIndexMerger(String final_output_) {
        File file = new File(out_folder + "__final_0");
        File file2 = new File(out_folder);
        final_output = final_output_;
        try {
            file2.mkdir();
            file.createNewFile();
        } catch (IOException e) {}
    }

    public void merge() {

        for(int i=0; i<no_files; ++i) __merge(i, "title_", 0);
        __finish();
        System.out.println("done");
        for (int i=0; i<no_files; ++i) __merge(i, "cat_", 1);
        __finish();
        System.out.println("done");
        for(int i=0; i<no_files; ++i) __merge(i, "link_", 2);
        __finish();
        System.out.println("done");
        for(int i=0; i<no_files; ++i) __merge(i, "ref_", 3);
        __finish();
        System.out.println("done");
        for(int i = 0; i<no_files; ++i) __merge(i, "text_", 4);
        __finish();
        System.out.println("done");

        File name = new File(out_folder + "postings");
        new File(out_folder + "__final_0").renameTo(name);

        __finalisze();



    }

    private void __merge(int i, String type, int j) {
        BufferedReader temp_final, block;
        OutputStream final_index_;
        String pre = "";
        for (int k=0; k<j; ++k) pre += ";";

        String ltemp = "", lblock = "", min = "";
        String[] w1;
        String[] w2;
        try {
            final_index_ = new FileOutputStream(out_folder + "__final_" + (i + 1));
            Writer final_index__ = new OutputStreamWriter(final_index_);
            BufferedWriter final_index = new BufferedWriter(final_index__, buf_size);

            block = new BufferedReader(new FileReader(inp_folder + type + i), buf_size);

            temp_final = new BufferedReader(new FileReader(out_folder + "__final_" + i), buf_size);

            block.readLine();
            ltemp = temp_final.readLine();
            lblock = block.readLine();
            while (ltemp != null && lblock != null) {
                w1 = ltemp.split(":");
                w2 = lblock.split(":");
                if (w1[0].equals(w2[0])) {
                    final_index.write(w1[0] + ":" + w1[1] + w2[1] + "\n");
                    ltemp = temp_final.readLine();
                    lblock = block.readLine();
                }
                else if(w1[0].compareToIgnoreCase(w2[0]) > 0) {
                    String[] w = lblock.split(":");
                    final_index.write(w[0] + ":" + pre + w[1] + "\n");
                    lblock = block.readLine();
                }
                else {
                    final_index.write(ltemp + "\n");
                    ltemp = temp_final.readLine();
                }
            }
            if (ltemp == null && lblock != null) {
                while((lblock = block.readLine()) != null ) {
                    w1 = lblock.split(":");
                    final_index.write(w1[0] + ":" + pre  + w1[1] +  "\n");
                }
            }
            if (lblock == null && ltemp != null) {
                while((ltemp = temp_final.readLine()) != null) final_index.write(ltemp + "\n");
            }

            block.close();
            temp_final.close();
            final_index.close();

            System.out.print(" "+ i);
            File t = new File(out_folder + "__final_" + i);
           t.delete();
        } catch (IOException e) {e.printStackTrace();}
    }

    private void __finish() {
        try {
            FileOutputStream final_index_ = new FileOutputStream(out_folder + "__final_0");
            Writer final_index__ = new OutputStreamWriter(final_index_);
            BufferedWriter final_index = new BufferedWriter(final_index__, buf_size);

            BufferedReader temp_final = new BufferedReader(new FileReader(out_folder + "__final_" + (no_files)), buf_size);

            String l;
            while (( l = temp_final.readLine()) != null) final_index.write(l + ";\n");
        } catch (IOException e) { e.printStackTrace();}
    }

    public void __finalisze() {
        //remove words that occur in less than 7 docs and occur in greater than 1/10th of documents of the corpus
        String line;
        try {
            BufferedReader postings = new BufferedReader(new FileReader(out_folder + "postings"), buf_size);

            FileOutputStream final_index_ = new FileOutputStream(final_output);
            Writer final_index = new OutputStreamWriter(final_index_);

            while ((line = postings.readLine()) != null) {
                StringBuilder final_ = new StringBuilder();
                try {
                    if (line.length() < 7000000)
                        final_index.write(line + "\n");
                    if (line.length() > 7000000) {
                        String[] w = line.split(";");
                        final_index.write(w[0] + ";;;;\n");
                    }

                } catch (ArrayIndexOutOfBoundsException e) {}//e.printStackTrace(); System.out.println(line + " " + parts.length);}
            }
        } catch (IOException e) { e.printStackTrace(); }

    }

    private String sort_(String w1) {
        String v1[] = w1.split(",|\\|");
        ArrayList<Doc> i1 = new ArrayList<>();
        for (int i=0; i<v1.length; i+=2) {
          //  System.out.println(v1[0] + " " + v1[1] + " ;");
            i1.add(new Doc(v1[i], v1[i+1]));
        }
        i1.sort(new DocComparator());

        String val = "";
        for (int i=0; i<i1.size(); ++i)  val += i1.get(i).toString();
        //System.out.println(val);
        return val;
    }
}

class Doc{
    public String a, b;

    Doc(String a_, String b_) {
        a = a_;
        b = b_;
    }

    @Override
    public String toString() {
        return a.toString() + ":" + b.toString() + "|";
    }
}


class DocComparator implements Comparator<Doc> {

    public int compare(Doc p1, Doc p2) {
        return p2.b.compareToIgnoreCase(p1.b);
    }
}