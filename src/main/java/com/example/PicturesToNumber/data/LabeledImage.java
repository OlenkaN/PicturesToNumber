package com.example.PicturesToNumber.data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class is used to represent an image with a number in the form of a label
 */
public class LabeledImage extends NonLabeledImage implements Serializable {
    private  double[] meanNormalizedPixel;
    private  double[] pixels;
    private  double[] result = new double[10];
    private double label;

    /**
     * Constructor
     * @param label the digit that is on image
     * @param pixels of image
     */
    public LabeledImage(int label, double[] pixels) {

        meanNormalizedPixel = meanNormalizeFeatures(pixels);
        this.pixels = pixels;
        this.label = label;
        result[(int) this.label] = 1;
    }

    /**
     * Constructor
     * @param imagePath
     * @param label the digit that is on image
     * @param targetWidth parameter to convert image to be suitable for  our neural network
     * @param targetHeight
     */
    public LabeledImage(String imagePath,int label,int targetWidth, int targetHeight)
    {
        this(Objects.requireNonNull(convertImageToNonLabeledImage(imagePath, targetWidth, targetHeight)),label);
    }

    /**
     * Constructor
     * @param image with no label
     * @param label
     */
    public LabeledImage(NonLabeledImage image,int label)
    {

        meanNormalizedPixel=image.getMeanNormalizedPixel();
        pixels=image.getPixels();
        this.label=label;
        result[(int) this.label] = 1;

    }


    public double getLabel() {
        return label;
    }

    public double[] getResult() {
        return result;
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
