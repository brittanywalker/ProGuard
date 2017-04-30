import sys, os, glob
import fileinput, re

"""
This tool is an obfuscation tool for COMPSCI702 project 1

This tool reads in a directory from command line and obfuscates any java files

...
"""

# read in the directory passed in from command line
directory = sys.argv[1]

# loop through all file in the directory to find the java files
for filename in os.listdir(directory):
    if filename.endswith(".java"):

        # open the current file and read in the lines
        f = open(directory + "/" + filename, "r")
        lines = f.readlines()
        f.close()

        # remove all // comments from the code:
        for i, line in zip(range(len(lines)), lines):
            if line.find("//") > -1:
                lines[i] = line[:line.find("//")] + "\n"

        # pattern to match a method signature
        methodSig = re.compile("^\s*(public|private|protected)\s+\w+\s+\w+[\s*|\w*]*\(")
        # pattern to make a class declaration
        classSig = re.compile("^\s*[\s*|\w*]*(class)[\s*|\w*]*\{")

        # loop through the lines in the file to check if they match either pattern
        for line, i in zip(lines, range(len(lines))):
            # if the line is a class signature then insert the white noise class variables
            if classSig.match(line):
                lines.insert(i+1, "private static int userTime = whiteNoise(10,7);\n private static int glass = 8;\n\
                private static int drink = 12;\n private static int hourTime = 60;\n ")
            # if the line is a method signature insert the white method calls
            if methodSig.match(line):
                lines.insert(i+1, "int a = 8;\n int variable = whiteNoise(userTime,a);\n")

        # loop through the file backwards to find the last closing brace
        for j in range(len(lines)-1, 0, -1):
            if lines[j].find("}") > -1:
                # at the last closing brace insert the white noise methods
                lines.insert(j, "public static int whiteNoise(int a, int b) {\nCalendar c = Calendar.getInstance();\
                \nint currentHour = c.get(Calendar.HOUR_OF_DAY);\
                \nint currentMinute = c.get(Calendar.MINUTE);\
                \nuserTime = currentHour * 60 + currentMinute + a/b;\
                \nuserTime = min(userTime, 720) + b; return a;}\
                 \npublic static int whiteNoise2(int a, int b, int c){\n\tint d = a+b/c; return whiteNoise(a,c);}")
                break

        # open the file for writing and write back the lines
        f = open(directory + "/" + filename, "w")
        lines = "".join(lines)
        # replace any if statements with white noise opaque predecates
        lines = lines.replace("if (", "if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && ")
        lines = lines.replace("if(", "if (whiteNoise(userTime,8) >= whiteNoise2(glass,drink,hourTime) && ")
        # replace new lines and tabs to reduce readability
        lines = lines.replace("\n", "")
        lines = lines.replace("\t", "")

        # write the lines and close the file
        f.write(lines + "\n")
        f.close()
