package com.wikiIndexer;

import java.util.Comparator;

class PostingComparator implements Comparator<Posting> {

    public int compare(Posting p1, Posting p2) {
        //  if(p1.word.equalsIgnoreCase(p2.word)) {
        //     return p1.doc_id - p2.doc_id;
        // }
        //  return p1.word.compareTo(p2.word);
        if (!p1.word.equalsIgnoreCase(p2.word))
            return (p1.word).compareToIgnoreCase(p2.word);
        else return p1.doc_id - p2.doc_id;
    }
}