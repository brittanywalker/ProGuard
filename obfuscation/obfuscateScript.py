import sys, os, glob
import fileinput

filename = sys.argv[1]

f = open(filename, "r")
lines = f.readlines()
f.close()


f = open(filename, "w")
lines = "".join(lines)
lines = lines.replace("\n", "")
lines = lines.replace("\t", "")

f.write(lines)
f.close()

'''
	for i in range(len(lines)):
		if i == len(lines) -2:
			f.write("here")

for line in fileinput.input(filename, inplace=True):
	if "get" in line:
		line = line.replace("get", "g")
	if "set" in line:
		line = line.replace("set", "s")

	sys.stdout.write(line)

'''

fileinput.close()