# WikiSearchEngine

=================================================================================

## 1. HOW TOs

To create index run

`./index.sh <path to wikiDump>`

Then to search, run

`./search.sh`

You will see something like this:
`
---------------------------------------------------------------------------------
➜  wikiSearch ./search.sh
'text:<words seperated by spaces>;cat:<...>;ref:<...>;title:<...>;link:<...>'

> 
---------------------------------------------------------------------------------
`

The prompt is where you type your query.

**NOTE: QUOTATIONS AROUND QUERY ARE MUST.**

=================================================================================

## 2. QUERY SYNTAX:  '<fieldname1>: <word1> <word2>;<fieldname2>: <word3> <word4>;'

NOTE:
1. QUOTATIONS AROUND QUERY ARE MUST.
2. SEMICOLONS AFTER EACH FIELD IS A MUST.
3. NOT ALL FIELDS ARE MUST.

Fields:
text -> text of the corpus
title -> title of each document in the corpus
ref -> reference
cat -> category
link -> links in each page

Sample query: 

`>  'text: harry sally;title: google;'`

==================================================================================

## 3. STATISTICS

The search module was coded in PYTHON 2.7
Search for all fields except text will be under 0.3 seconds for 4 words.
With text field, it takes upto 3-4 seconds.

==================================================================================


