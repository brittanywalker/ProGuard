import sys, os, glob
import fileinput, re

directory = sys.argv[1]

for filename in os.listdir(directory):
    if filename.endswith(".java"):

        f = open(directory + "/" + filename, "r")
        lines = f.readlines()
        f.close()

        pattern = re.compile("^\s*(public|private|protected)\s+\w+\s+\w+[\s*|\w*]*\(")
        for line, i in zip(lines, range(len(lines))):
            if line.find("*") < 0 and line.find("class") > -1:
                lines.insert(i+1, "private static int userTime = whiteNoise(10,7); private static int glass = 8;\
                private static int drink = 12; private static int hourTime = 60; ")
            if pattern.match(line):
                lines.insert(i+1, "int a = 8; int variable = whiteNoise(userTime,a);")



        for j in range(len(lines), 0):
            if lines[j].find("}") > -1:
                lines.insert(j, "public static int whiteNoise(int a, int b) {\nCalendar c = Calendar.getInstance();\
                \nint currentHour = c.get(Calendar.HOUR_OF_DAY);\
                \nint currentMinute = c.get(Calendar.MINUTE);\
                \nuserTime = currentHour * 60 + currentMinute + a/b;\
                \nuserTime = min(userTime, 720) + b; return a;}\
                 \npublic static int whiteNoise2(int a, int b, int c){\n\tint d = a+b/c; return whiteNoise(a,c);}")
                break



        f = open(directory + "/" + filename, "w")
        lines = "".join(lines)
        lines = lines.replace("if (", "if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && ")
        lines = lines.replace("if(", "if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && ")
        #lines = lines.replace("\n", "")
        #lines = lines.replace("\t", "")

        f.write(lines)
        f.close()
