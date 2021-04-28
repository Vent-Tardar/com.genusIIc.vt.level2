package com.genusIIc.vt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonDocTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void pathTest() throws NullPointerException{
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            File test_file_1 = new File(classLoader.getResource("test3.txt").getFile());
            String absolutePath_1 = test_file_1.getAbsolutePath();
            File test_file_2 = new File(classLoader.getResource("test2.txt").getFile());
            String absolutePath_2 = test_file_2.getAbsolutePath();
        });
        assertNull(thrown.getMessage());
    }

    @Test
    public void filecomparisonTest() {
        File test_file_1 = new File(classLoader.getResource("test1.txt").getFile());
        String absolutePath_1 = test_file_1.getAbsolutePath();
        File test_file_2 = new File(classLoader.getResource("test1.txt").getFile());
        String absolutePath_2 = test_file_2.getAbsolutePath();
        try{
            if (!(absolutePath_1.equals(absolutePath_2))){
                fail("Expected IOException");
            }
        }catch (Exception e) {
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    @Timeout(4)
    public void compareSmallTest(){
        File test_file_1 = new File(classLoader.getResource("smallTest_1.txt").getFile());
        String absolutePath_1 = test_file_1.getAbsolutePath();
        File test_file_2 = new File(classLoader.getResource("smallTest_2.txt").getFile());
        String absolutePath_2 = test_file_2.getAbsolutePath();
        ComparisonDoc cd = new ComparisonDoc();
        List lst = new ArrayList();
        lst = cd.compare(absolutePath_1, absolutePath_2);
        assertEquals(2, lst.size());
    }

    @Test
    @Timeout(6)
    public void compareMediumTest(){
        File test_file_1 = new File(classLoader.getResource("test1.txt").getFile());
        String absolutePath_1 = test_file_1.getAbsolutePath();
        File test_file_2 = new File(classLoader.getResource("test2.txt").getFile());
        String absolutePath_2 = test_file_2.getAbsolutePath();
        ComparisonDoc cd = new ComparisonDoc();
        List lst = new ArrayList();
        lst = cd.compare(absolutePath_1, absolutePath_2);
        assertEquals(5, lst.size());
    }

    @Test
    @Timeout(6)
    public void compareBigTest(){
        File test_file_1 = new File(classLoader.getResource("bigTest_1.txt").getFile());
        String absolutePath_1 = test_file_1.getAbsolutePath();
        File test_file_2 = new File(classLoader.getResource("bigTest_2.txt").getFile());
        String absolutePath_2 = test_file_2.getAbsolutePath();
        ComparisonDoc cd = new ComparisonDoc();
        List lst = new ArrayList();
        lst = cd.compare(absolutePath_1, absolutePath_2);
        assertEquals(6, lst.size());
    }

}
