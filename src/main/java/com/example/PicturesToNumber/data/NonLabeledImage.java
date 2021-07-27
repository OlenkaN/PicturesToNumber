package com.example.PicturesToNumber.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class NonLabeledImage implements Serializable {
    private double[] meanNormalizedPixel;
    private double[] pixels;

    public NonLabeledImage() {
    }

    public double[] getMeanNormalizedPixel() {
        return meanNormalizedPixel;
    }

    public double[] getPixels() {
        return pixels;
    }

    /**
     * Constructor
     *
     * @param imageFile
     * @param targetWidth  parameter to convert image to be suitable for  our neural network
     * @param targetHeight
     */
    public NonLabeledImage(File imageFile, int targetWidth, int targetHeight) {
        NonLabeledImage image = convertImageToNonLabeledImage(imageFile, targetWidth, targetHeight);
        this.pixels = image.getPixels();
        this.meanNormalizedPixel = image.getMeanNormalizedPixel();
    }

    /**
     * Constructor
     *
     * @param imagePath
     * @param targetWidth  parameter to convert image to be suitable for  our neural network
     * @param targetHeight
     */
    public NonLabeledImage(String imagePath, int targetWidth, int targetHeight) {
        this(new File(imagePath), targetWidth, targetHeight);
    }


    /**
     * Constructor
     *
     * @param pixels of image
     */
    public NonLabeledImage(double[] pixels) {
        this.meanNormalizedPixel = meanNormalizeFeatures(pixels);
        this.pixels = pixels;
    }

    /**
     * This method is used to normalize data to have more uniform values between 0 and 1.
     *
     * @param pixels
     * @return normalized array
     */
    double[] meanNormalizeFeatures(double[] pixels) {
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


    /**
     * This method is used to convert file to LabeledImage
     *
     * @param imageFile filepath
     * @return
     */
    public static NonLabeledImage convertImageToNonLabeledImage(File imageFile, int targetWidth, int targetHeight) {
        try {
            // Upload the image
            BufferedImage inputImage = ImageIO.read(imageFile);
            BufferedImage scaleImage = resizeImage(inputImage, targetWidth, targetHeight);
            int width = scaleImage.getWidth();
            int height = scaleImage.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(scaleImage, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();
            return new NonLabeledImage(Arrays.stream(pixels).asDoubleStream().toArray());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;
    }

}
