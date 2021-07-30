package com.example.PicturesToNumber.config;

import com.example.PicturesToNumber.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@PropertySource(name = "appProperties", value = "application.properties")
public class AppConfiguration {

    @Bean
    public NeuralNetwork neuralNetwork(@Value("${layer}") Integer layerAmount,
                                       @Value("#{'${layerDimension}'}") Integer[] layerDimension,
                                       @Value("${filePath:false}") String filePath) {

        if (filePath.equals("false")) {
            return new NeuralNetwork(layerAmount, layerDimension);
        } else
            return NeuralNetwork.readFromFile(filePath);
    }




}
