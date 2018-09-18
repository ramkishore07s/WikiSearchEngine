package com.wikiIndexer;


class Posting{
    public String word;
    public Integer doc_id;
    public Integer count;

    Posting(String s, Integer d, Integer c) {
        word = s;
        doc_id = d;
        count = c;
    }

    public String string_() {
        return (doc_id.toString() + "," + count.toString());
    }

    public int compareTo(Posting p2) {
        return doc_id - p2.doc_id;
    }
}
