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
    private void checkingFiles(String org, String mdf) {
        if(!(new File(org).exists() && new File(mdf).exists())) {
            System.out.println();
            logger.error("A non-existent file is entered in the parameters");
        }
        if (org.equals(mdf)) {
            System.out.println();
            logger.error("The same file was entered in the parameters");
        }
    }

    public List compare(String org, String mdf){
        checkingFiles(org, mdf);
        List<AbstractDelta<String>> list = new ArrayList<>();
        List<String> list_1 = new ArrayList<>();
        try{
            logger.info("File comparison started.");
            logger.info("Files are being compared.");

            List<String> original = Files.readAllLines(new File(String.valueOf(org)).toPath());
            List<String> revised = Files.readAllLines(new File(String.valueOf(mdf)).toPath());

            Patch<String> patch = DiffUtils.diff(original, revised);
            String str = null;

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                list.add(delta);
                str = String.join(" ", list.toString());
                str = str.replace("[ChangeDelta, ", "").
                        replace("[position", "line").
                        replace("lines", "changed").
                        replace("]]", "");
                list.remove(delta);
                list_1.add(str);
            }

            logger.info("File comparison ended.");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return list_1;
    }
}
