package com.wikiIndexer;

/**
 * Stopwords were taken from Python nltk library.
 *
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private String[] sw = {
            //Stop words in english
            "", " ", "did", "s", "again", "so", "about", "hadn", "t", "during", "or", "haven", "don", "aren", "off", "for", "the", "then", "mustn", "into", "where", "why", "than", "wasn", "that", "ll", "was", "me", "you've", "wasn't", "you're", "yourself", "it", "does", "isn", "herself", "do", "o", "this", "have", "what", "hers", "he", "which", "few", "will", "ve", "weren't", "that", "in", "your", "and", "how", "up", "should've", "yours", "shouldn", "further", "whom", "being", "before", "some", "as", "has", "a", "same", "once", "ll", "its", "who", "mightn", "won", "myself", "until", "when", "ma", "below", "been", "ourselves", "i", "they", "most", "no", "an", "between", "other", "theirs", "shan", "too", "d", "himself", "with", "couldn't", "him", "them", "shan't", "such", "didn't", "doesn't", "it's", "didn", "having", "own", "above", "ain", "very", "any", "down", "won't", "shouldn't", "doesn", "my", "but", "because", "of", "isn't", "from", "she's", "needn't", "are", "y", "those", "under", "to", "hasn't", "you", "re", "ours", "only", "each", "can", "should", "both", "itself", "she", "more", "on", "needn", "not", "you'd", "hadn", "at", "don't", "our", "is", "if", "hasn", "wouldn't", "these", "doing", "against", "their", "wouldn", "over", "be", "m", "were", "haven", "you'll", "am", "through", "all", "we", "his", "out", "just", "after", "weren", "here", "mustn't", "her", "themselves", "nor", "mightn't", "by", "couldn", "there", "had", "yourselves", "while", "now",
            // Alphabets
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", // Category stop words
            // Category stop words
            "category",
            // Link stop words
            "http", "https", "stp", "ftp", "www",
            // Reference stop words
            "ref", "cite", 
    };
    private Set<String> stopwords = new HashSet<String>(Arrays.asList(sw));


    public boolean isStopword(String str) {
        if (str.length() < 3) return true;
        char[] c = str.toCharArray();
        if ((c[0] == c[1]) && (c[1] == c[2])) return true;
        return stopwords.contains(str);
    }
}
