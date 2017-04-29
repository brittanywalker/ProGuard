import sys, os, glob
import fileinput

filename = sys.argv[1]

f = open(filename, "r")
lines = f.readlines()
f.close()

lines.insert(len(lines)-2, "public static int whiteNoise(int a, int b) {int c = 0; c = a+b; int d = whiteNoise2(c, a, b); return a;}\
 public static int whiteNoise2(int a, int b, int c){int d = a+b/c; return a;}")

f = open(filename, "w")
lines = "".join(lines)
lines = lines.replace("if (", "if (1>0 && ")
lines = lines.replace("if(", "if (1>0 && ")
lines = lines.replace("\n", "")
lines = lines.replace("\t", "")

f.write(lines)
f.close()
