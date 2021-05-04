package com.genusiic.vt.springhtml;

import com.genusiic.vt.springhtml.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringHtmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHtmlApplication.class, args);
    }

}
