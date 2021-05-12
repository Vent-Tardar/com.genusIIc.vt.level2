# Program for comparing the original and modified files in Java

Using Gradle, the diff-utils library and Spring

### Description

This program compares the original file with the modified one. In order for the check to occur, you need to perform the following actions:

- Run *SpringHtmlApplication.java*;
- Open http://localhost:8080 in the browser; 
- Upload two files;
- Press the button *Compare Files*

## Important: 

1. For the program to work correctly, create a folder named `uploadFiles` in the disk `C:`;
2. The original file must have `1` or `Original` in its name. For example: *File1.txt, OriginalFile.txt, originalFile.txt*;
3. The modified file must have `2` or `Modified` in its name. For example: *File2.txt, ModifiedFile.txt, modifiedFile.txt*;
4. Files are deleted from the temporary folder after comparison.
