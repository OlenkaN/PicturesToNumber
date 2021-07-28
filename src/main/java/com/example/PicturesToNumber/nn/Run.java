package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.IdxReader;
import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.NonLabeledImage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to run our neural network
 */
@SpringBootApplication

public class Run {
    public static void main(String[] args) throws Exception {

        NeuralNetwork test = new NeuralNetwork(4, new Integer[]{784, 128, 64, 10});
        List<LabeledImage> labeledImages = IdxReader.loadData(15000);

        for (LabeledImage image : labeledImages) {
            test.train(image);
        }
        /*for(int i=0;i<100;++i)
        {
          test.train(new LabeledImage(new File("src/main/resources/0.jpg"),0, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/2.png"),2, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/3.png"),3, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/4.png"),4, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/6.png"),6, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/6.1.png"),6, 28, 28));
          test.train(new LabeledImage(new File("src/main/resources/5.jpg"),5, 28, 28));

        }
       NeuralNetwork test =NeuralNetwork.readFromFile("src/main/resources/testWeights.json");

        for(int i=0;i<100;++i) {
            test.train(new LabeledImage(new File("src/main/resources/8.png"), 8, 28, 28));
            test.train(new LabeledImage(new File("src/main/resources/9.png"), 9, 28, 28));
        }*/

        System.out.println(test.predict(new NonLabeledImage("src/main/resources/0.jpg", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/2.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/3.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/4.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/5.jpg", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.1.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/8.png", 28, 28)));
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/9.png", 28, 28)));
        //NeuralNetwork.writeToFile(test, "src/main/resources/testWeights");
    }


}