package com.example.ptn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class is used to run our application.
 */
@SpringBootApplication
@EnableSwagger2
public class PicturesToNumberApplication {
    /**
     * Method to run whole project.
     * @param args parameters
     */
    public static void main(String[] args) {
        SpringApplication.run(PicturesToNumberApplication.class, args);
    }
}
