package com.example.ptn.data;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class is used to represent an image
 * with a number in the form of a label.
 */
public class LabeledImage extends NonLabeledImage implements Serializable {

  private final int amountOfNumbers = 10;
  private double[] result = new double[amountOfNumbers];
  private double label;

  /**
   * Constructor(array).
   *
   * @param label  the digit that is on image
   * @param pixels of image
   */
  public LabeledImage(final int label, final double[] pixels) {

    meanNormalizedPixel = meanNormalizeFeatures(pixels);
    this.pixels = pixels;
    this.label = label;
    result[(int) this.label] = 1;
  }

  /**
   * Constructor(file).
   *
   * @param image        file
   * @param label        the digit that is on image
   * @param targetWidth  parameter to convert image
   *                     to be suitable for our neural network
   * @param targetHeight parameter to convert image
   *                     to be suitable for our neural network
   */
  public LabeledImage(
          final File image,
          final int label,
          final int targetWidth,
          final int targetHeight) {
    this(Objects.requireNonNull(
            convertImageToNonLabeledImage(image, targetWidth, targetHeight)), label);
  }

  /**
   * Constructor(NonLabeledImage).
   *
   * @param image with no label
   * @param label the digit that is on image
   */
  public LabeledImage(final NonLabeledImage image, final int label) {

    meanNormalizedPixel = image.getMeanNormalizedPixel().clone();
    pixels = image.getPixels().clone();
    this.label = label;
    result[(int) this.label] = 1;

  }


  /**
   * Getter for label.
   *
   * @return label
   */
  public double getLabel() {
    return label;
  }

  /**
   * Getter for result.
   *
   * @return result
   */
  public double[] getResult() {
    return result;
  }

  /**
   * Getter for MeanNormalizedPixel.
   *
   * @return MeanNormalizedPixel
   */
  public double[] getMeanNormalizedPixel() {
    return meanNormalizedPixel;
  }

  /**
   * Setter for label.
   *
   * @param label to set
   */
  public void setLabel(final double label) {
    this.label = label;
  }

  /**
   * Setter for meanNormalizedPixel.
   *
   * @param meanNormalizedPixel to set
   */
  public void setMeanNormalizedPixel(final double[] meanNormalizedPixel) {
    this.meanNormalizedPixel = meanNormalizedPixel;
  }

  /**
   * Setter for pixels.
   *
   * @param pixels to set
   */
  public void setPixels(final double[] pixels) {
    this.pixels = pixels;
  }

  /**
   * Setter for result.
   *
   * @param result to set
   */
  public void setResult(final double[] result) {
    this.result = result;
  }

  /**
   * To string method.
   *
   * @return string
   */
  @Override
  public String toString() {
    return "LabeledImage{"
            + "label=" + label + '}';
  }
}
