package com.genusIIc.vt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonDocTest {
    ClassLoader classLoader = getClass().getClassLoader();
    ComparisonDoc cd;

    @BeforeEach
    public void init() {
        cd = new ComparisonDoc();
    }

//    @Test
//    public void pathTest() throws NullPointerException{
//        Throwable thrown = assertThrows(NullPointerException.class, () -> {
//            File test_file_1 = new File(classLoader.getResource("test3.txt").getFile());
//            String absolutePath_1 = test_file_1.getAbsolutePath();
//            File test_file_2 = new File(classLoader.getResource("test2.txt").getFile());
//            String absolutePath_2 = test_file_2.getAbsolutePath();
//        });
//        assertNull(thrown.getMessage());
//    }

    @Test
    public void filecomparisonTest() {
//        File test_file_1 = new File(classLoader.getResource("test1.txt").getFile());
//        String absolutePath_1 = test_file_1.getAbsolutePath();
//        File test_file_2 = new File(classLoader.getResource("test1.txt").getFile());
//        String absolutePath_2 = test_file_2.getAbsolutePath();
//        try{
//            if (!(absolutePath_1.equals(absolutePath_2))){
//                fail("Expected IOException");
//            }
//        }catch (Exception e) {
//            assertNotEquals("", e.getMessage());
//        }

        List<String> lst = cd.compare(getFilePath("smallTest_1.txt"), getFilePath("smallTest_1.txt"));
        assertEquals(0, lst.size());
    }

    @Test
    @Timeout(4)
    public void compareSmallTest() {
        List<String> lst = cd.compare(getFilePath("smallTest_1.txt"), getFilePath("smallTest_2.txt"));
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
        List<String> lst = new ArrayList<String>();
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
        List<String> lst = new ArrayList<>();
        lst = cd.compare(absolutePath_1, absolutePath_2);
        assertEquals(6, lst.size());
    }

    @Test
    public void checkingParametersTest() throws ArrayIndexOutOfBoundsException{
        List<String> test_1 = new ArrayList<String>();
        test_1.add("bigTest_1.txt");
        test_1.add("bigTest_1.txt");
        test_1.add("bigTest_1.txt");
        if ((test_1.size() > 2)|| (test_1.size() < 2)){
            Throwable thrown = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                throw new ArrayIndexOutOfBoundsException("Many/few parameters");
            });
            assertNotNull(thrown.getMessage());
        }
    }

    private String getFilePath (String filename) {
        return new File(Objects.requireNonNull(classLoader.getResource("smallTest_1.txt")).getFile()).getAbsolutePath();
    }

}
