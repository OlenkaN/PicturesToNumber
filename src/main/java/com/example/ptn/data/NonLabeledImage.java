package com.example.ptn.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * Class for image with no label to predict by neuralNetwork.
 */
public class NonLabeledImage implements Serializable {
  protected double[] meanNormalizedPixel;
  protected double[] pixels;
  /**
   * Logger for NonLabelImage.
   */
  private static final Logger LOGGER =
          LoggerFactory.getLogger(NonLabeledImage.class);

  /**
   * Constructor.
   */
  public NonLabeledImage() {
  }

  /**
   * Constructor(MultipartFile).
   *
   * @param imageFile    image MultipartFile
   * @param targetWidth  parameter for width to convert
   *                     image to be suitable for  our neural network.
   * @param targetHeight parameter for height to convert image
   *                     to be suitable for  our neural network.
   */
  public NonLabeledImage(
          final MultipartFile imageFile,
          final int targetWidth,
          final int targetHeight) {
    try {
      NonLabeledImage image =
              convertImageToNonLabeledImage(
                      imageFile, targetWidth, targetHeight);
      assert image != null;
      this.pixels = image.getPixels();
      this.meanNormalizedPixel = image.getMeanNormalizedPixel();
    } catch (NullPointerException e) {
      LOGGER.error("Request raised " + e.getClass().getSimpleName());
      LOGGER.error("Image is null");
      LOGGER.error(e + "");
    }

  }

  /**
   * Constructor(File).
   *
   * @param imageFile    image File
   * @param targetWidth  parameter for width to convert image
   *                     to be suitable for  our neural network
   * @param targetHeight parameter for height to convert image
   *                     to be suitable for  our neural network
   */
  public NonLabeledImage(
          final File imageFile,
          final int targetWidth,
          final int targetHeight) {
    try {
      NonLabeledImage image =
              convertImageToNonLabeledImage(
                      imageFile, targetWidth, targetHeight);
      assert image != null;
      this.pixels = image.getPixels();
      this.meanNormalizedPixel = image.getMeanNormalizedPixel();
    } catch (NullPointerException e) {
      LOGGER.error("Request raised " + e.getClass().getSimpleName());
      LOGGER.error("Image is null");
      LOGGER.error(e + "");
    }

  }


  /**
   * Constructor(imagePath).
   *
   * @param imagePath    path to image
   * @param targetWidth  parameter for width to convert image
   *                     to be suitable for  our neural network
   * @param targetHeight parameter for height to convert image
   *                     to be suitable for  our neural network
   */
  public NonLabeledImage(
          final String imagePath,
          final int targetWidth,
          final int targetHeight) {
    this(new File(imagePath), targetWidth, targetHeight);
  }


  /**
   * Constructor.
   *
   * @param pixels of image
   */
  public NonLabeledImage(final double[] pixels) {
    this.meanNormalizedPixel = meanNormalizeFeatures(pixels);
    this.pixels = pixels;
  }

  /**
   * This method is used to normalize data
   * to have more uniform values between 0 and 1.
   *
   * @param pixels of image
   * @return normalized array
   */
  double[] meanNormalizeFeatures(final double[] pixels) {
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
   * This method is used to convert file to LabeledImage.
   *
   * @param imageFile    MultipartFile
   * @param targetWidth  of image
   * @param targetHeight of image
   * @return NonLabeledImage from MultipartFile
   */
  public static NonLabeledImage convertImageToNonLabeledImage(
          final MultipartFile imageFile,
          final int targetWidth,
          final int targetHeight) {
    try {
      return convertImageToNonLabeledImage(
              ImageIO.read(imageFile.getInputStream()),
              targetWidth,
              targetHeight);
    } catch (IOException e) {
      LOGGER.error("Request raised " + e.getClass().getSimpleName());
      LOGGER.error(
              "Problem with converting image file to NonLabeledImage");
      LOGGER.error(e + "");
    }
    return null;
  }

  /**
   * This method is used to convert file to LabeledImage.
   *
   * @param imageFile    File
   * @param targetWidth  of image
   * @param targetHeight of image
   * @return NonLabeledImage from File
   */
  public static NonLabeledImage convertImageToNonLabeledImage(
          final File imageFile,
          final int targetWidth,
          final int targetHeight) {
    try {
      return convertImageToNonLabeledImage(
              ImageIO.read(imageFile), targetWidth, targetHeight);
    } catch (IOException e) {
      LOGGER.error("Request raised " + e.getClass().getSimpleName());
      LOGGER.error(
              "Problem with converting image file to NonLabeledImage");
      LOGGER.error(e + "");
    }
    return null;
  }

  /**
   * This method is used to convert file to LabeledImage.
   *
   * @param inputImage   filepath
   * @param targetWidth  of image
   * @param targetHeight of image
   * @return NonLabeledImage from BufferedImage
   */
  private static NonLabeledImage convertImageToNonLabeledImage(
          final BufferedImage inputImage,
          final int targetWidth,
          final int targetHeight) {
    try {
      // Upload the image
      //BufferedImage inputImage = ImageIO.read(imageFile);
      BufferedImage scaleImage =
              resizeImage(inputImage, targetWidth, targetHeight);
      int width = scaleImage.getWidth();
      int height = scaleImage.getHeight();
      int[] pixels = new int[width * height];

      // Retrieve pixel info and store in 'pixels' variable
      PixelGrabber pgb =
              new PixelGrabber(
                      scaleImage, 0, 0, width, height, pixels, 0, width);
      pgb.grabPixels();
      return new NonLabeledImage(
              Arrays.stream(pixels).asDoubleStream().toArray());

    } catch (IOException | InterruptedException e) {
      LOGGER.error("Request raised " + e.getClass().getSimpleName());
      LOGGER.error(
              "Problem with converting image file to NonLabeledImage");
      LOGGER.error(e + "");
    }
    return null;
  }


  private static BufferedImage resizeImage(
          final BufferedImage originalImage,
          final int targetWidth,
          final int targetHeight) throws IOException {
    Image resultingImage =
            originalImage.getScaledInstance(
                    targetWidth, targetHeight, Image.SCALE_DEFAULT);
    BufferedImage outputImage =
            new BufferedImage(
                    targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    outputImage
            .getGraphics()
            .drawImage(resultingImage, 0, 0, null);

    return outputImage;
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
   * Getter for Pixels.
   *
   * @return Pixels
   */
  public double[] getPixels() {
    return pixels;
  }

}
