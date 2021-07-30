package com.example.PicturesToNumber.сontrollers;


import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.NonLabeledImage;
import com.example.PicturesToNumber.dto.RecognitionResultDto;
import com.example.PicturesToNumber.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * this controller is required
 * to handle post, get and other requests from the client
 */
@RestController
@RequestMapping("/api")
public class PictureController {

    @Autowired
    private NeuralNetwork neuralNetwork;

    @Value("${targetWidth}")
    private Integer targetWidth;
    @Value("${targetHeight}")
    private Integer targetHeight;


    /**
     * This method upload image and predict what digit is on it
     *
     * @param multiFile is image that need to be upload and be predicted
     * @param req
     * @return string message of success or not
     */
    @RequestMapping(value = "/fileUpload/predict", method = RequestMethod.POST)
    public @ResponseBody
    RecognitionResultDto filePredict(@RequestPart("file") MultipartFile multiFile, HttpServletRequest req) {
        double[] result = null;
        try {
            result = neuralNetwork.predict(new NonLabeledImage(multiFile, targetWidth, targetHeight));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RecognitionResultDto((int) result[0], result[1]);
    }

    /**
     * This method upload image and train neural network with it
     *
     * @param multiFile is image that need to train our neural network
     * @param label     is digit on image
     * @param req
     * @return string message of success or not
     */
    @RequestMapping(value = "/fileUpload/train", method = RequestMethod.POST)
    public @ResponseBody
    String fileTrain(@RequestPart("file") MultipartFile multiFile, @RequestPart("label") String label, HttpServletRequest req) {
        try {
            neuralNetwork.train(new LabeledImage(new NonLabeledImage(multiFile, targetWidth, targetWidth), Integer.parseInt(label)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


}
