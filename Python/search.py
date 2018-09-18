import os
import sys
import re
import math
import time

from nltk.stem import SnowballStemmer
from tert import tert
from tert_names import tert_names
from binary_search import binarySearch
from search_basics import Nmaxelements

#filename = sys.argv[1]
index = open("final_postings")
s_index = open("secondary_index")

names = open("final_names")
s_names = open("secondary_index_names")

types = {'text':4, 'cat':1, 'ref':3, 'title':0, 'link':2}

keys = sorted(list(tert.keys()))
name_keys = sorted(list(tert_names.keys()))

stemmer = SnowballStemmer("english")

def tf_idf(tf, df):
    return math.log(float(tf + 1)/float(df + 1) + 1)

def search(inp):
    index.seek(0, 0)
    s_index.seek(0, 0)
    file_det = {}
    query = [[] for _ in range(5)]
    parts = [a.split(":") for a in inp.split(";")]
    for p in parts:
        try: query[types[p[0]]] = [stemmer.stem(i) for i in re.split("[^A-Za-z]", p[1]) if not i == ""]
        except: pass
    
    for i in query:
        for j in i:
      #      print(j)
            # search tertiary index
            start = binarySearch(keys, j)
            try: end = keys[start + 1]
            except: end = keys[-1]
            start = keys[start]
            #print(start, tert[start], end, tert[end])

            # search secondary index
            s_index.seek(tert[start])
            l = s_index.read(tert[end] - tert[start])
            l = [a.split(":") for a in l.split("\n")][:-1]
            words = [a[0] for a in l if not a == ""]
            offsets = [int(a[1]) for a in l if not a == ""]

            if j in words:
                # title cat link ref text
                offset = offsets[words.index(j)]

                # search primary index
                index.seek(offset)
                words, parts = index.readline().split(":")
                parts = parts.split(";")

                # normal search

                text = parts[query.index(i)].split("|")[:-1]
                text = [a.split(",") for a in text]
                file_nos = [int(a[0]) for a in text]
                counts = [int(a[1]) for a in text]

               # print(query.index(i), len(file_nos))

                if query.index(i) == 4:
                    text = parts[0].split("|")[:-1]
                    text = [a.split(",") for a in text]
                    file_nos.extend([int(a[0]) for a in text])
                    counts.extend([int(a[1]) + 2 for a in text])
                 #   print("title+i", len(file_nos))

                for i_ in range(len(file_nos)):
                    if file_nos[i_] not in file_det:
                        file_det[file_nos[i_]] = tf_idf(counts[i_], len(file_nos))
                    else: file_det[file_nos[i_]] += tf_idf(counts[i_], len(file_nos))


    try:
        results =  Nmaxelements([(k, v) for k, v in file_det.iteritems()], 10)
        for i in results:
        #    print(i)
            start = binarySearch(name_keys, int(i[0]))
            end = name_keys[start + 1]
            start = name_keys[start]
          #  print(start, end)

            s_names.seek(tert_names[start])
            l = s_names.read(tert_names[end] - tert_names[start]).split("\n")[:-1]
            files = [int(a.split(":")[0]) for a in l]
          #  print(files[0], files[-1], len(files))
            if i[0] in files:
                offset = int(l[files.index(i[0])].split(":")[1])
                names.seek(offset)
                print(names.readline()[:-1])
    except: pass


            


print("'text:<words seperated by spaces>;cat:<...>;ref:<...>;title:<...>;link:<...>'")
print('')

inp = ["text: google war goasdfogle;ref: bag words",
"text: android lollipop",
"title: google news",
"title: when harry met sally",
]

# for i in inp:
#     then = time.time()
#     search(i)
#     now = time.time()
#     print("\n")
#     print(str(now - then) + " seconds")
#     print
# #     print

#print(keys)

    
while(True):
    inp = input("> ")
    then = time.time()
    search(inp)
    now = time.time()
    print("\n")
    print(str(now - then) + " seconds")
    print
    print
