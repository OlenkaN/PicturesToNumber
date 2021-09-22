package com.example.ptn.controllers;


import com.example.ptn.dto.RecognitionResultDto;
import com.example.ptn.service.NeuralNetworkService;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * this controller is required.
 * to handle post, get and other requests from the client
 */
@RestController("PictureControllerMain")
@RequestMapping("/api")
public class PictureController {


  @Autowired
  private NeuralNetworkService neuralNetworkService;
  /**
   * Logger for controller.
   */
  private static final Logger LOGGER =
          LoggerFactory.getLogger(PictureController.class);

  /**
   * Method to test connection.
   *
   * @return current time
   * @throws Exception if cannot connect
   */
  @RequestMapping(value = "/testBase", method = RequestMethod.GET)
  public @ResponseBody
  String testDataBase() throws Exception {

    ResultSet resultSet = neuralNetworkService.testConnection();
    if (resultSet.next()) {
      return resultSet.getString(1);
    }
    return "null";
  }

  /**
   * Method to save neuralNetwork.
   *
   * @return "success or not
   */
  @RequestMapping(value = "/save", method = RequestMethod.GET)
  public @ResponseBody
  String saveNeuralNetwork() {
    return neuralNetworkService.saveNeuralNetwork();
  }


  /**
   * This method upload image and predict what digit is on it.
   *
   * @param multiFile is image that need to be upload and be predicted
   * @return string message of success or not
   */

  @SuppressWarnings("checkstyle:FinalParameters")
  @RequestMapping(value = "/fileUpload/predict", method = RequestMethod.POST)
  public @ResponseBody
  RecognitionResultDto filePredict(
          @RequestPart("file") final MultipartFile multiFile)
          throws Exception {

    double[] result = neuralNetworkService.filePredict(multiFile);
    return new RecognitionResultDto((int) result[0], result[1]);
  }

  /**
   * This method upload image and train neural network with it.
   *
   * @param multiFile is image that need to train our neural network
   * @param label     is digit on image
   * @return string message of success
   */

  @RequestMapping(value = "/fileUpload/train", method = RequestMethod.POST)
  public @ResponseBody
  String fileTrain(@RequestPart("file") final MultipartFile multiFile,
                   @RequestPart("label") final String label)
          throws Exception {
    neuralNetworkService.fileTrain(multiFile, label);
    return "success";
  }

  /**
   * Convert a predefined exception to an HTTP Status code and specify the
   * name of a specific view that will be used to display the error.
   *
   * @param exception from methods
   * @return Exception view.
   */
  @ExceptionHandler({Exception.class})
  public ResponseEntity databaseError(final Exception exception) {
    LOGGER.error("Request raised " + exception.getClass().getSimpleName());
    LOGGER.error(exception + "");
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("\"The are some problem  neural network, make sure  that it was initialize and also that your file is suitable\"");

  }


}
