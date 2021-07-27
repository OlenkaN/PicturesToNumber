package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.IdxReader;
import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.NonLabeledImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to run our neural network
 */
public class Run {
    public static void main(String[] args) throws Exception {


        NeuralNetwork test = new NeuralNetwork(4, new Integer[]{784, 128, 64, 10});
        List<LabeledImage> labeledImages = IdxReader.loadData(10000);

        for (LabeledImage image : labeledImages) {
            test.train(image);
        }
        NeuralNetwork.writeToFile(test,"src/main/resources/testWeights");
        //NeuralNetwork test =NeuralNetwork.readFromFile("src/main/resources/testWeights.json");

        System.out.println(test.predict(new NonLabeledImage("src/main/resources/0.jpg", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/2.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/3.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/4.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/5.jpg", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.1.png", 28, 28)));

    }


}