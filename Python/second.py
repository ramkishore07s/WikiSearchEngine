f = open("final_postings")

# construct secondary index
f2 = open("secondary_index", "w+")

l = f.readline()
s = 0
i = 0
while(not l == ""):
    if i%1 == 0:
        w = l.split(":")[0]
        f2.write(w + ":" + str(s) + "\n")
    s += len(l)
    l = f.readline()
    i += 1

f2.close()
f.close()

# construct tertiary index
f = open("secondary_index")
f2 = open("tert.py", "w+")

l = f.readline()
s = 0
i = 0
f2.write("tert = {")
while not l == "":
    if i%100 == 0:
        w = l.split(":")[0]
        f2.write("'" + w + "':" + str(s) + ",\n")
    s += len(l)
    l = f.readline()
    i += 1
f2.write("}")
f2.close()
f.close()