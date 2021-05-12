package com.genusiic.vt.springhtml;

import static org.assertj.core.api.Assertions.assertThat;

import com.genusiic.vt.springhtml.controller.FileController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ControllerTest {
    @Autowired
    private FileController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
