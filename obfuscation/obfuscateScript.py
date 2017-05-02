import sys, os, glob
import fileinput, re

"""
COMPSCI702 Group Project
This tool obfuscates .java files using control and layout techniques.

----------------

To use the tool, you will need to have Python installed.
Then move this script into the directory that contains the sub-directory holding your .java files.
In order for all of your .java files to be obfuscated at once, they all need to be within the sub-directory.
Open your command line and navigate to the directory where you have saved this script.
Type into command line: python3 obfuscateScript.py sub-directory
where sub-directory is the name of the directory containing your .java files.

-----WARNING-----
Your files will be updated, and the originals will NOT be conserved. So we highly recommend saving
your unobfuscated files somewhere else before running this tool.

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
                lines.insert(i+1, "private static int glass = 8;\n\
                private static int drink = 12;\n private static int hourTime = 60;\n private static int userTime = calcWaterMethod(glass,drink);\n  ")
            # if the line is a method signature insert the white method calls
            if methodSig.match(line):
                lines.insert(i+1, "int a = 8;\n int variable = calcWaterMethod(userTime,a);\n")

        # loop through the file backwards to find the last closing brace
        for j in range(len(lines)-1, 0, -1):
            if lines[j].find("}") > -1:
                # at the last closing brace insert the white noise methods
                lines.insert(j, "public static int calcWaterMethod(int a, int b) {\nCalendar c = Calendar.getInstance();\
                \nint currentHour = c.get(Calendar.HOUR_OF_DAY);\
                \nint currentMinute = c.get(Calendar.MINUTE);\
                \nuserTime = currentHour * 60 + currentMinute + a/b;\
                \nuserTime = min(userTime, 720) + b; return a;}\
                 \npublic static int calcWaterMethod2(int a, int b, int c){\n\tint d = a+b/c; return calcWaterMethod(a,c);}")
                break

        # open the file for writing and write back the lines
        f = open(directory + "/" + filename, "w")
        lines = "".join(lines)
        # replace any if statements with white noise opaque predicates
        lines = lines.replace("if (", "if (calcWaterMethod(userTime,8) >= calcWaterMethod2(glass,drink,hourTime) && ")
        lines = lines.replace("if(", "if (calcWaterMethod(userTime,8) >= calcWaterMethod2(glass,drink,hourTime) && ")
        # replace new lines and tabs to reduce readability
        lines = lines.replace("\n", "")
        lines = lines.replace("\t", "")

        # write the lines and close the file
        f.write(lines + "\n")
        f.close()
