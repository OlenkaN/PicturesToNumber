package com.example.PicturesToNumber.сontrollers;


import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.NonLabeledImage;
import com.example.PicturesToNumber.nn.Initialize;
import com.example.PicturesToNumber.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
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
    Initialize initialize;

    /**
     * This method upload image and predict what digit is on it
     *
     * @param multiFile is image that need to be upload and be predicted
     * @param req
     * @return string message of success or not
     */
    @RequestMapping(value = "/fileUpload/predict", method = RequestMethod.POST)
    public @ResponseBody
    String filePredict(@RequestPart("file") MultipartFile multiFile, HttpServletRequest req) {

        File file = null;
        try {
            file = multipartToFile(multiFile);
            System.out.println(initialize.network.predict(new NonLabeledImage(file, initialize.targetWidth, initialize.targetHeight)));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // After operating the above files, you need to delete the temporary files generated in the root directory
            File f = new File(file.toURI());
            f.delete();
        }
        return "fail";


    }

    /**
     * This method upload image and train neural network with it
     * @param multiFile is image that need to train our neural network
     * @param label is digit on image
     * @param req
     * @return string message of success or not
     */
    @RequestMapping(value = "/fileUpload/train", method = RequestMethod.POST)
    public @ResponseBody
    String fileTrain(@RequestPart("file") MultipartFile multiFile,@RequestPart("label") String label,HttpServletRequest req)  {

        File file = null;
        try {
            file = multipartToFile(multiFile);
            System.out.println(label);
            initialize.network.train(new LabeledImage(file,Integer.parseInt(label), initialize.targetWidth, initialize.targetWidth));
            NeuralNetwork.writeToFile(initialize.network,"src/main/resources/testWeights");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // After operating the above files, you need to delete the temporary files generated in the root directory
            File f = new File(file.toURI());
            f.delete();
        }
        return "fail";


    }

    private File multipartToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


}
