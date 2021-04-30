package com.genusiic.vt.sprng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static java.lang.System.exit;

@SpringBootApplication
public class SprngApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(SprngApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SprngApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2){
            logger.error("Parameters entered incorrectly");
        } else {
            ServiceExample serEx = new ServiceExample();
            List<String> lst = serEx.compare(args[0], args[1]);
            for (String s : lst) {
                System.out.println(s);
            }
        }
        exit(0);
    }

}
