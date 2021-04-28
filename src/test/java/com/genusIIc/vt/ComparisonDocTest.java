package com.genusIIc.vt;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparisonDocTest {
    @Test
    void compareTest(){
        ClassLoader classLoader = getClass().getClassLoader();

        File test_file_1 = new File(classLoader.getResource("test1.txt").getFile());
        String absolutePath_1 = test_file_1.getAbsolutePath();

        File test_file_2 = new File(classLoader.getResource("test2.txt").getFile());
        String absolutePath_2 = test_file_2.getAbsolutePath();

        ComparisonDoc cd = new ComparisonDoc();
        List lst = new ArrayList();
        lst = cd.compare(absolutePath_1, absolutePath_2);
        assertEquals(5, lst.size());
    }
}