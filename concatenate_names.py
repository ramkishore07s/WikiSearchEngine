import os

files = os.listdir('tmp__')
files = sorted([i for i in files if "names" in i])

# merge all name files created by indexer
f = open("final_names", 'w+')
for i in files:
    f2 = open("tmp__/" + i)
    f.write(f2.read())
    f2.close()
f.close()

# create secondary index for names
f = open("final_names")
f2 = open("secondary_index_names", 'w+')
l = f.readline()
s = 0
while not l == "":
    w = l.split(":")[0]
    f2.write(w + ":" + str(s) + "\n")
    s += len(l)
    l = f.readline()
f.close()
f2.close()

# create tertiary index for names

f = open("secondary_index_names")
f2 = open("tert_names.py", "w+")
l = f.readline()
s = 0
i = 0
f2.write("tert_names = {")
while not l == "":
    if i%1000 == 0:
        w = l.split(":")[0]
        f2.write("'" + w + "':" + str(s) + ",\n")
    s += len(l)
    l = f.readline()
    i += 1
f2.write("}")
f2.close()
f.close()

