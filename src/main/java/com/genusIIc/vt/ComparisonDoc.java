package com.genusIIc.vt;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import com.github.difflib.*;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.AbstractDelta;
import org.apache.logging.log4j.*;

public class ComparisonDoc {
    private static final Logger logger = LogManager.getLogger(ComparisonDoc.class);
    private void checkingFiles(String org, String mdf) throws IOException {
        if(!(new File(org).exists() && new File(mdf).exists())) {
            System.out.println();
            logger.error("A non-existent file is entered in the parameters");
            throw new NullPointerException("A non-existent file is entered in the parameters");
        }
        if (org.equals(mdf)) {
            System.out.println();
            logger.error("The same file was entered in the parameters");
            throw new IOException("The same file was entered in the parameters");
        }
        if(new File(org).length() == 0 || new File(mdf).length() == 0) {
            System.out.println();
            logger.error("Files are empty");
            throw new NullPointerException("Files are empty");
        }
    }

    public List<String> compare(String org, String mdf) throws IOException {
        checkingFiles(org, mdf);
        List<AbstractDelta<String>> list = new ArrayList<>();
        List<String> list_1 = new ArrayList<>();
        try{
            logger.info("File comparison started.");
            logger.info("Files are being compared.");

            List<String> original = Files.readAllLines(new File(String.valueOf(org)).toPath());
            List<String> revised = Files.readAllLines(new File(String.valueOf(mdf)).toPath());

            Patch<String> patch = DiffUtils.diff(original, revised);

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                list.add(delta);
                String str = String.join(" ", list.toString());
                if (str.indexOf("ChangeDelta") != -1){
                    str = str.replace("[[ChangeDelta, ", "Changes: ").
                            replace("position", "line").
                            replace("lines", "changed").
                            replace("]]", "");
                } else if (str.indexOf("DeleteDelta") != -1){
                    str = str.replace("[[DeleteDelta, ", "Delete: ").
                            replace("position", "line").
                            replace("lines", "deleted").
                            replace("]]", "");
                } else if (str.indexOf("InsertDelta") != -1){
                    str = str.replace("[[InsertDelta, ", "Insert: ").
                            replace("position", "line").
                            replace("lines", "inserted").
                            replace("]]", "");
                }
                list.remove(delta);
                list_1.add(str);
            }

            logger.info("File comparison ended.");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return list_1;
    }
}
