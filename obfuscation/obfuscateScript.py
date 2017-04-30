import sys, os, glob
import fileinput

filename = sys.argv[1]

f = open(filename, "r")
lines = f.readlines()
f.close()

lines.insert(1, "private static int userTime = whiteNoise(10,7);")

lines.insert(len(lines)-2, "public static int whiteNoise(int a, int b) {\nCalendar c = Calendar.getInstance();\
\nint currentHour = c.get(Calendar.HOUR_OF_DAY);\
\nint currentMinute = c.get(Calendar.MINUTE);\
\nuserTime = currentHour * 60 + currentMinute + a/b;\
\nuserTime = min(userTime, 720) + b; return a;}\
 \npublic static int whiteNoise2(int a, int b, int c){\n\tint d = a+b/c; return whiteNoise(a,c);}")

f = open(filename, "w")
lines = "".join(lines)
lines = lines.replace("if (", "if (whiteNoise(userTime,2) >= whiteNoise2(5,9,7) && ")
lines = lines.replace("if(", "if (whiteNoise(userTime,2) >= whiteNoise2(5,9,7) && ")
#lines = lines.replace("\n", "")
#lines = lines.replace("\t", "")

f.write(lines)
f.close()
