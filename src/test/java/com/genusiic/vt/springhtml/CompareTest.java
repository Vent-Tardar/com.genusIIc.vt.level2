package com.genusiic.vt.springhtml;

import java.nio.file.Paths;
import java.util.stream.Stream;

import com.genusiic.vt.springhtml.storage.StorageService;
import com.genusiic.vt.springhtml.storage.FileNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CompareTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storageService;

    @Test
    public void shouldListAllFiles() throws Exception {
        given(this.storageService.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

        this.mvc.perform(get("http://localhost:8080")).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost:8080/download/first.txt",
                                "http://localhost:8080/download/second.txt")));
    }

    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.storageService.loadAsResource("test.txt"))
                .willThrow(FileNotFoundException.class);

        this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }
}
