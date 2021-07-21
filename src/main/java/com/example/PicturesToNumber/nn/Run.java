package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.IdxReader;
import com.example.PicturesToNumber.data.LabeledImage;

import java.util.List;

/**
 * Class to run our nn
 */
public class Run {
    public static void main(String[] args) {

        NeuralNetwork test = NeuralNetwork.readFromFile("jacksonTest.json");
        List<LabeledImage> labeledImages = IdxReader.loadData(10);

        for (LabeledImage image : labeledImages) {
            System.out.println(image.getLabel());
            System.out.println(test.predict(image.getMeanNormalizedPixel())+"\n");
        }

        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/0.jpg", 0).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/2.png", 2).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/3.png", 3).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/4.png", 4).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/5.jpg", 5).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/6.png", 6).getMeanNormalizedPixel())+"\n");
        System.out.println(test.predict(LabeledImage.convertImageToArray("src/main/resources/6.1.png", 6).getMeanNormalizedPixel())+"\n");
    }


}