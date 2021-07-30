package com.example.PicturesToNumber.data;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class is used to represent an image with a number in the form of a label
 */
public class LabeledImage extends NonLabeledImage implements Serializable {

    private double[] result = new double[10];
    private double label;

    /**
     * Constructor
     *
     * @param label  the digit that is on image
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
     *
     * @param image
     * @param label        the digit that is on image
     * @param targetWidth  parameter to convert image to be suitable for  our neural network
     * @param targetHeight
     */
    public LabeledImage(File image, int label, int targetWidth, int targetHeight) {
        this(Objects.requireNonNull(convertImageToNonLabeledImage(image, targetWidth, targetHeight)), label);
    }

    /**
     * Constructor
     *
     * @param image with no label
     * @param label
     */
    public LabeledImage(NonLabeledImage image, int label) {

        meanNormalizedPixel = image.getMeanNormalizedPixel().clone();
        pixels = image.getPixels().clone();
        this.label = label;
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

    public void setMeanNormalizedPixel(double[] meanNormalizedPixel) {
        this.meanNormalizedPixel = meanNormalizedPixel;
    }

    public void setPixels(double[] pixels) {
        this.pixels = pixels;
    }

    public void setResult(double[] result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LabeledImage{" +
                "label=" + label +
                '}';
    }
}
