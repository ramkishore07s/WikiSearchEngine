# WikiSearchEngine

## 1. HOW TOs

To create index run

`./index.sh <path to wikiDump>`

Then to search, run

`./search.sh`

You will see something like this:
<pre>
---------------------------------------------------------------------------------
➜  wikiSearch ./search.sh
'text:<words seperated by spaces>;cat:<...>;ref:<...>;title:<...>;link:<...>'

> 
---------------------------------------------------------------------------------
</pre>

The prompt is where you type your query.

**NOTE: QUOTATIONS AROUND QUERY ARE MUST.**


## 2. QUERY SYNTAX:  
`'\<fieldname1\>: \<word1\> \<word2\>;\<fieldname2\>: \<word3\> \<word4\>;'`

**NOTE:**
1. QUOTATIONS AROUND QUERY ARE MUST.
2. SEMICOLONS AFTER EACH FIELD IS A MUST.
3. NOT ALL FIELDS ARE MUST.

**Fields:**
* text -> text of the corpus
* title -> title of each document in the corpus
* ref -> reference
* cat -> category
* link -> links in each page

Sample query: 

`>  'text: android lollipop;title: google;'`


## 3. STATISTICS

* The search module was coded in PYTHON 2.7.
* 90% of the time results will be given be under 0.3 seconds for 4 words. Sometimes it might take upto 4 seconds.


## 4. IMPLEMENTATION

* The indexer is implemented in JAVA and the search is written in PYTHON.
* It is an implementation of BSBI algorithm.
* In total six index files are constructed, three for the corpus, three for Document name retrieval.
* Two small tertiary indexes in memory, two secondary indexes, one inverted index and one file of document names to be retrieved are created.
* Can lookup any posting in just two disk accesses. This makes the search really fast.
