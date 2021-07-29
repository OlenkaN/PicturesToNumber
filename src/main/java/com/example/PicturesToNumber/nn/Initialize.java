package com.example.PicturesToNumber.nn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(name = "appProperties", value = "application.properties")
public class Initialize {

    public NeuralNetwork network;

    @Value("${targetWidth}")
    public Integer targetWidth;
    @Value("${targetHeight}")
    public Integer targetHeight;


    public Initialize(@Value("${layer}") Integer layerAmount,
                      @Value("#{'${layerDimension}'}") Integer[] layerDimension,
                      @Value("${filePath:false}") String filePath) {

        if (filePath.equals("false")) {
            network = new NeuralNetwork(layerAmount, layerDimension);
        } else
            network = NeuralNetwork.readFromFile(filePath);
    }
}
