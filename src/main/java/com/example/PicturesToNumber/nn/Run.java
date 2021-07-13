package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.LabeledImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class Run {
    public static void main(String[] args) {
        //System.setProperty("hadoop.home.dir", Paths.get("winutils").toAbsolutePath().toString());
       System.setProperty("hadoop.home.dir", "C:\\hadoop");
        try (FileWriter writer = new FileWriter("notes3.txt", true)) {
            writer.write("Start \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        NeuralNetwork test = new NeuralNetwork();
        test.init();
        test.train(3000, 1000);
        try(FileWriter writer = new FileWriter("notes2.txt", true))
        {

            writer.write("Test nn label 4= " +test.predict(convertImageToArray("",0)).getLabel() +"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //convertImageToArray("",0);
        System.out.println("Okay");
    }

    private static LabeledImage convertImageToArray(String imagePath, int label) {
        try {
            System.out.println("Processing the image...");

            // Upload the image
            BufferedImage image = ImageIO.read(new File("src/main/resources/test4.png"));
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();
            return new LabeledImage(4, Arrays.stream(pixels).asDoubleStream().toArray());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}