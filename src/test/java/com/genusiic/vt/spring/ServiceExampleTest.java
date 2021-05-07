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
        assertEquals(lst.get(0), "Changes: line: 1, changed: [Taps] to [Tap]");
        assertEquals(lst.get(1), "Changes: line: 4, changed: [Floating] to [Float]");
    }

    @Test
    @Timeout(6)
    public void compareMediumTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("test1.txt"), getFilePath("test2.txt"));
        assertEquals(5, lst.size());
        assertEquals(lst.get(0), "Changes: line: 1, changed: [Taps, Kush] to [Tap, Kukish]");
        assertEquals(lst.get(1), "Changes: line: 6, changed: [Fugue] to [fugue]");
        assertEquals(lst.get(2), "Changes: line: 10, changed: [Jiu-jujitsu, To be snout, Canadians] to [Jiu-jitsu, To_be_snout, Cabardians]");
    }

    @Test
    @Timeout(20)
    public void compareBigTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("SuperFile1.txt"), getFilePath("SuperFile2.txt"));
        assertEquals(4, lst.size());
        assertEquals(lst.get(0), "Changes: line: 3, changed: [It's contempt to punish me.] to [It's contempt to punish me...]");
        assertEquals(lst.get(1), "Changes: line: 10, changed: [When I Had Hope] to [When I had hope]");
        assertEquals(lst.get(2), "Changes: line: 69, changed: [I beg your defense...] to [I beg your defense!]");
        assertEquals(lst.get(3), "Changes: line: 79, changed: [I'm running out! It's scary to recount...] to [I'm running out... It's scary to recount...]");
    }

    @Test
    public void deleteTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing2.txt"), getFilePath("testing1.txt"));
        assertEquals(lst.get(1), "Delete: line: 3, deleted: [zzz]");
    }

    @Test
    public void insertTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing1.txt"), getFilePath("testing2.txt"));
        assertEquals(lst.get(1), "Insert: line: 3, inserted: [zzz]");
    }
}
