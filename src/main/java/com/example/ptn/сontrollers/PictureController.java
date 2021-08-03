package com.example.ptn.сontrollers;


import com.example.ptn.data.LabeledImage;
import com.example.ptn.data.NonLabeledImage;
import com.example.ptn.dto.RecognitionResultDto;
import com.example.ptn.nn.NeuralNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(PictureController.class);


    /**
     * This method upload image and predict what digit is on it
     *
     * @param multiFile is image that need to be upload and be predicted
     * @return string message of success or not
     */

    @RequestMapping(value = "/fileUpload/predict", method = RequestMethod.POST)
    public @ResponseBody
    RecognitionResultDto filePredict(@RequestPart("file") MultipartFile multiFile) throws Exception {
        double[] result = neuralNetwork.predict(new NonLabeledImage(multiFile, targetWidth, targetHeight));
        return new RecognitionResultDto((int) result[0], result[1]);
    }

    /**
     * This method upload image and train neural network with it
     *
     * @param multiFile is image that need to train our neural network
     * @param label     is digit on image
     * @return string message of success or not
     */

    @RequestMapping(value = "/fileUpload/train", method = RequestMethod.POST)
    public @ResponseBody
    String fileTrain(@RequestPart("file") MultipartFile multiFile, @RequestPart("label") String label) throws Exception {
        neuralNetwork.train(new LabeledImage(new NonLabeledImage(multiFile, targetWidth, targetHeight), Integer.parseInt(label)));
        return "success";
    }

    /**
     * Convert a predefined exception to an HTTP Status code and specify the
     * name of a specific view that will be used to display the error.
     *
     * @return Exception view.
     */
    @ExceptionHandler({Exception.class})
    public String databaseError(Exception exception) {
        LOGGER.error("Request raised " + exception.getClass().getSimpleName());
        LOGGER.error(exception + "");
        return "The are some problem with neural network, make sure that it was initialize and also that your file is suitable ";
    }


}
