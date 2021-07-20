package com.example.PicturesToNumber.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * this class is used to represent an image with a number in the form of a label
 */
public class LabeledImage implements Serializable {
    private final double[] meanNormalizedPixel;
    private final double[] pixels;
    private final double[] result = new double[10];
    private double label;


    public LabeledImage(int label, double[] pixels) {
        meanNormalizedPixel = meanNormalizeFeatures(pixels);
        this.pixels = pixels;
        this.label = label;
        result[(int) this.label] = 1;
    }


    /**
     * This method is used to normalize data to have more uniform values between 0 and 1.
     *
     * @param pixels
     * @return normalized array
     */
    private double[] meanNormalizeFeatures(double[] pixels) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        for (double pixel : pixels) {
            sum = sum + pixel;
            if (pixel > max) {
                max = pixel;
            }
            if (pixel < min) {
                min = pixel;
            }
        }
        double mean = sum / pixels.length;

        double[] pixelsNorm = new double[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            pixelsNorm[i] = (pixels[i] - mean) / (max - min);
        }
        return pixelsNorm;
    }

    public double getLabel() {
        return label;
    }

    public static LabeledImage convertImageToArray(String imagePath, int label) {
        try {
            // Upload the image
            BufferedImage inputImage = ImageIO.read(new File(imagePath));
            BufferedImage scaleImage = resizeImage(inputImage, 28, 28);
            int width = scaleImage.getWidth();
            int height = scaleImage.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(scaleImage, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();
            return new LabeledImage(label, Arrays.stream(pixels).asDoubleStream().toArray());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;
    }

    public double[] getMeanNormalizedPixel() {
        return meanNormalizedPixel;
    }



    public void setLabel(double label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "LabeledImage{" +
                "label=" + label +
                '}';
    }
}
