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
    private static final Logger logger = LogManager.getLogger(Main.class);
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
        try{
            int i = 1;
            logger.info("File comparison started.");
            logger.info("Files are being compared.");

            List<String> original = Files.readAllLines(new File(String.valueOf(org)).toPath());
            List<String> revised = Files.readAllLines(new File(String.valueOf(mdf)).toPath());

            Patch<String> patch = DiffUtils.diff(original, revised);

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                list.add(delta);
            }
            
            logger.info("File comparison ended.");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return list;
    }
}
