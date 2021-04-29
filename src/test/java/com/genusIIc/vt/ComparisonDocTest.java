package com.genusIIc.vt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
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

    private String getFilePath (String filename) {
        return new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile()).getAbsolutePath();
    }

    //Проверка существования файла
    @Test
    public void pathTest() throws NullPointerException{
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            getFilePath("test3.txt");
        });
        assertNull(thrown.getMessage());
    }

    //Проверка на одинаковость файлов
    @Test
    public void identicallyTest() {
        Throwable thrown = assertThrows(IOException.class, () -> {
            List<String> lst = cd.compare(getFilePath("smallTest_1.txt"), getFilePath("smallTest_1.txt"));
        });
        assertNotNull(thrown.getMessage());
    }

    //Проверка пустых файлов
    @Test
    public void emptiesTest() throws NullPointerException {
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            List<String> lst = cd.compare(getFilePath("NullFile1.txt"), getFilePath("NullFile2.txt"));
        });
    }

    //Проверка сравнения на маленьких файлах
    @Test
    @Timeout(4)
    public void compareSmallTest() throws IOException {
        List<String> lst = cd.compare(getFilePath("smallTest_1.txt"), getFilePath("smallTest_2.txt"));
        assertEquals(2, lst.size());
        lst.get(0).equals("Tap");
        lst.get(1).equals("Float");
    }

    //Проверка сравнения на средних файлах
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

    //Проверка сравнения на больших файлах
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

    //Проверка внесения параметров
    @Test
    public void checkingParametersTest() throws ArrayIndexOutOfBoundsException{
        List<String> test_1 = new ArrayList<>();
        test_1.add("bigTest_1.txt");
        test_1.add("bigTest_1.txt");
        test_1.add("bigTest_1.txt");
        if (test_1.size() != 2){
            Throwable thrown = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                throw new ArrayIndexOutOfBoundsException();
            });
            assertNull(thrown.getMessage());
        }
    }

    //Проверка на количество строк(удаление)
    @Test
    public void deleteTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing2.txt"), getFilePath("testing1.txt"));
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).equals("Delete")){}
        }
    }

    //Проверка на количество строк(изменение)
    @Test
    public void insertTest() throws IOException{
        List<String> lst = cd.compare(getFilePath("testing1.txt"), getFilePath("testing2.txt"));
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).equals("Insert")){}
        }
    }
}
