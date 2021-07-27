package com.example.PicturesToNumber;

import com.example.PicturesToNumber.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
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
