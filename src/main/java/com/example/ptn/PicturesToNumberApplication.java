package com.example.ptn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class is used to run our application
 */
@SpringBootApplication
@EnableSwagger2
public class PicturesToNumberApplication {
    public static void main(String[] args) {
        SpringApplication.run(PicturesToNumberApplication.class, args);
    }
}
