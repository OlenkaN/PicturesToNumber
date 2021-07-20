package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.IdxReader;
import com.example.PicturesToNumber.data.LabeledImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Run {
    public static void main(String[] args) {

        NN test = FileNNDataReaderAndWriter.readFromFile("jacksonTest.json");
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