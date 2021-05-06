package com.genusiic.vt.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ServiceExampleTest {
    ClassLoader classLoader = getClass().getClassLoader();
    ServiceExample cd;

    @BeforeEach
    public void init() {
        cd = new ServiceExample();
    }

    private String getFilePath (String filename) {
        return new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile()).getAbsolutePath();
    }

    @Test
    public void pathTest() throws NullPointerException{
        Throwable thrown = assertThrows(NullPointerException.class, () -> getFilePath("test3.txt"));
        assertNull(thrown.getMessage());
    }

    @Test
    public void identicallyTest() {
        assertThrows(IOException.class, () -> {
           cd.compare("smallTest_1.txt", "smallTest_1.txt");
        });
    }

    @Test
    public void emptiesTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("NullFile1.txt"), getFilePath("NullFile2.txt"));
        assertEquals(0, lst.size());
    }

    @Test
    @Timeout(4)
    public void compareSmallTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("smallTest_1.txt"), getFilePath("smallTest_2.txt"));
        assertEquals(2, lst.size());
        lst.get(0).equals("Tap");
        lst.get(1).equals("Float");
    }

    @Test
    @Timeout(6)
    public void compareMediumTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("test1.txt"), getFilePath("test2.txt"));
        assertEquals(5, lst.size());
        lst.get(0).equals("Tap");
        lst.get(1).equals("Kukish");
        lst.get(2).equals("fugue");
        lst.get(3).equals("Jiu-jitsu");
        lst.get(4).equals("To_be_snout");
    }

    @Test
    @Timeout(20)
    public void compareBigTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("SuperFile1.txt"), getFilePath("SuperFile2.txt"));
        assertEquals(4, lst.size());
        lst.get(0).equals("It's contempt to punish me...");
        lst.get(1).equals("When I had hope");
        lst.get(2).equals("I beg your defense!");
        lst.get(3).equals("I'm running out... It's scary to recount...");
    }

    @Test
    public void deleteTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing2.txt"), getFilePath("testing1.txt"));
        for (String s : lst) {
            if (s.equals("Delete")) {
                lst.get(0).equals("zzz");
            }
        }
    }

    @Test
    public void insertTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing1.txt"), getFilePath("testing2.txt"));
        for (String s : lst) {
            if (s.equals("Insert")) {
                lst.get(0).equals("zzz");
            }
        }
    }
}
