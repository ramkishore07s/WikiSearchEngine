package com.wikiIndexer;

/*
Parse XML
Case folding, stemming, stop word removal etc done at document level
Construct index for each document
Call wikiIndexer to convert index to inverted index
 */

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.util.ArrayList;

public class WikiXMLHandler extends DefaultHandler {
    private boolean page = false;
    private boolean title = false;
    private boolean text = false;

    private WikiIndexer wikiIndexer;
    private StringBuilder text_ = new StringBuilder();
    private String title_;

    WikiXMLHandler(WikiIndexer wikiIndexer_) {
        wikiIndexer = wikiIndexer_;
    }

    @Override
    public void startElement(String uri, String localname, String qname,
                             Attributes attributes) {
        if(qname.equalsIgnoreCase("page"))        page = true;
        else if(qname.equalsIgnoreCase("title")) title = true;
        else if (qname.equalsIgnoreCase("text"))  text = true;
    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (title) {
            title_ = new String(ch, start, length);
        } else if (text) {
            text_.append(new String(ch, start, length));
            text_.append(" ");
        }
    }

    @Override
    public void endElement(String uri, String localname, String qname) {
        if (qname.equalsIgnoreCase("text")) { text = false; }
        if (qname.equalsIgnoreCase("title")) { title = false; }
        if (qname.equalsIgnoreCase("page")) {
            wikiIndexer.index(text_.toString(), title_);
            clearAll();
        }
    }

    private void clearAll() {
        page = title = text = false;
        text_ = new StringBuilder();
        title_ = "";
    }
}

