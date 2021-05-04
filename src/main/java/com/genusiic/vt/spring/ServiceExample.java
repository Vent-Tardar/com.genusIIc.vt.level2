package com.genusiic.vt.spring;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceExample {
    @Value("${list_1:unknown}")
    private final List<String> list_1 = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(ServiceExample.class);

    private void checkingFiles(String org, String mdf) throws IOException {
        if(!(new File(org).exists() && new File(mdf).exists())) {
            System.out.println();
            logger.error("A non-existent file is entered in the parameters.");
            throw new FileException("A non-existent file is entered in the parameters");
        }
        if (org.equals(mdf)) {
            System.out.println();
            logger.error("The same file was entered in the parameters");
            throw new FileException("The same file was entered in the parameters");
        }
        if(new File(org).length() == 0 || new File(mdf).length() == 0) {
            System.out.println();
            logger.error("Files are empty");
            throw new FileException("Files are empty");
        }
    }

    public List<String> compare(String org, String mdf) throws IOException {
        checkingFiles(org, mdf);
        List<AbstractDelta<String>> list = new ArrayList<>();
        try{
            logger.info("File comparison started.");
            logger.info("Files are being compared.");

            List<String> original = Files.readAllLines(new File(String.valueOf(org)).toPath());
            List<String> revised = Files.readAllLines(new File(String.valueOf(mdf)).toPath());

            Patch<String> patch = DiffUtils.diff(original, revised);

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                list.add(delta);
                String str = String.join(" ", list.toString());
                if (str.contains("ChangeDelta")){
                    str = str.replace("[[ChangeDelta, ", "Changes: ").
                            replace("position", "line").
                            replace("lines", "changed").
                            replace("]]", "");
                } else if (str.contains("DeleteDelta")){
                    str = str.replace("[[DeleteDelta, ", "Delete: ").
                            replace("position", "line").
                            replace("lines", "deleted").
                            replace("]]", "");
                } else if (str.contains("InsertDelta")){
                    str = str.replace("[[InsertDelta, ", "Insert: ").
                            replace("position", "line").
                            replace("lines", "inserted").
                            replace("]]", "");
                }
                list.remove(delta);
                list_1.add(str);
            }

            logger.info("File comparison ended.");
        }catch (FileException e){
            System.out.println("Error: " + e.getCause());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return list_1;
    }
}

