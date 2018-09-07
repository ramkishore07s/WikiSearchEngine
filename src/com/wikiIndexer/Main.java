package com.wikiIndexer;

/**
 *
 */

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class Main {

    public static void main(String args[]) {
        double time = System.currentTimeMillis();
        try {
            String path = args[0];
            WikiIndexer wikiIndexer = new WikiIndexer();
            File inputFile = new File(path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            WikiXMLHandler wikiXMLHandler = new WikiXMLHandler(wikiIndexer);
            saxParser.parse(inputFile, wikiXMLHandler);
            wikiIndexer.cleanUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;
        System.out.print("Time: "); System.out.print(time/1000);
    }
}