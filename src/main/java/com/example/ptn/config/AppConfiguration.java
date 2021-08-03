package com.example.ptn.config;

import com.example.ptn.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
