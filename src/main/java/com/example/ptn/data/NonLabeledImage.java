package com.example.ptn.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class NonLabeledImage implements Serializable {
    protected double[] meanNormalizedPixel;
    protected double[] pixels;
    private final static Logger LOGGER = LoggerFactory.getLogger(NonLabeledImage.class);

    public NonLabeledImage() {
    }

    /**
     * Constructor(MultipartFile)
     *
     * @param imageFile    image MultipartFile
     * @param targetWidth  parameter for width to convert image to be suitable for  our neural network
     * @param targetHeight parameter for height to convert image to be suitable for  our neural network
     */
    public NonLabeledImage(MultipartFile imageFile, int targetWidth, int targetHeight) {
        try {
            NonLabeledImage image = convertImageToNonLabeledImage(imageFile, targetWidth, targetHeight);
            this.pixels = image.getPixels();
            this.meanNormalizedPixel = image.getMeanNormalizedPixel();
        } catch (NullPointerException e) {
            LOGGER.error("Request raised " + e.getClass().getSimpleName());
            LOGGER.error("Image is null");
            LOGGER.error(e + "");
        }

    }

    /**
     * Constructor(File)
     *
     * @param imageFile    image File
     * @param targetWidth  parameter for width to convert image to be suitable for  our neural network
     * @param targetHeight parameter for height to convert image to be suitable for  our neural network
     */
    public NonLabeledImage(File imageFile, int targetWidth, int targetHeight) {
        try {
            NonLabeledImage image = convertImageToNonLabeledImage(imageFile, targetWidth, targetHeight);
            this.pixels = image.getPixels();
            this.meanNormalizedPixel = image.getMeanNormalizedPixel();
        } catch (NullPointerException e) {
            LOGGER.error("Request raised " + e.getClass().getSimpleName());
            LOGGER.error("Image is null");
            LOGGER.error(e + "");
        }

    }


    /**
     * Constructor(imagePath)
     *
     * @param imagePath    path to image
     * @param targetWidth  parameter for width to convert image to be suitable for  our neural network
     * @param targetHeight parameter for height to convert image to be suitable for  our neural network
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
     * @param pixels of image
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
     * @param imageFile MultipartFile
     * @return NonLabeledImage from MultipartFile
     */
    public static NonLabeledImage convertImageToNonLabeledImage(MultipartFile imageFile, int targetWidth, int targetHeight) {
        try {
            return convertImageToNonLabeledImage(ImageIO.read(imageFile.getInputStream()), targetWidth, targetHeight);
        } catch (IOException e) {
            LOGGER.error("Request raised " + e.getClass().getSimpleName());
            LOGGER.error("Problem with converting image file to NonLabeledImage");
            LOGGER.error(e + "");
        }
        return null;
    }

    /**
     * This method is used to convert file to LabeledImage
     *
     * @param imageFile File
     * @return NonLabeledImage from File
     */
    public static NonLabeledImage convertImageToNonLabeledImage(File imageFile, int targetWidth, int targetHeight) {
        try {
            return convertImageToNonLabeledImage(ImageIO.read(imageFile), targetWidth, targetHeight);
        } catch (IOException e) {
            LOGGER.error("Request raised " + e.getClass().getSimpleName());
            LOGGER.error("Problem with converting image file to NonLabeledImage");
            LOGGER.error(e + "");
        }
        return null;
    }

    /**
     * This method is used to convert file to LabeledImage
     *
     * @param inputImage filepath
     * @return NonLabeledImage from BufferedImage
     */
    private static NonLabeledImage convertImageToNonLabeledImage(BufferedImage inputImage, int targetWidth, int targetHeight) {
        try {
            // Upload the image
            //BufferedImage inputImage = ImageIO.read(imageFile);
            BufferedImage scaleImage = resizeImage(inputImage, targetWidth, targetHeight);
            int width = scaleImage.getWidth();
            int height = scaleImage.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(scaleImage, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();
            return new NonLabeledImage(Arrays.stream(pixels).asDoubleStream().toArray());

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Request raised " + e.getClass().getSimpleName());
            LOGGER.error("Problem with converting image file to NonLabeledImage");
            LOGGER.error(e + "");
        }
        return null;
    }


    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;
    }

    public double[] getMeanNormalizedPixel() {
        return meanNormalizedPixel;
    }

    public double[] getPixels() {
        return pixels;
    }

}
