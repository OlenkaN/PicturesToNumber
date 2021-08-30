package com.example.ptn.nn;

import com.example.ptn.data.IdxReader;
import com.example.ptn.data.LabeledImage;
import com.example.ptn.data.NonLabeledImage;

import java.io.File;
import java.util.List;

/**
 * Class to run our neural network.
 */
public class Run {
    public static void main(String[] args) throws Exception {


        NeuralNetwork test = new NeuralNetwork(4, new Integer[]{16,14,12,10}, 4, 4, 0.5);
        NeuralNetwork.writeToFile(test, "src/main/resources/smallTest");
        /*List<LabeledImage> labeledImages = IdxReader.loadData(15000);

        for (LabeledImage image : labeledImages) {
            test.train(image);
        }

        for (int i = 0; i < 100; ++i) {
            test.train(new LabeledImage(new File("src/main/resources/0.jpg"),
                    0, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/2.png"),
                    2, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/3.png"),
                    3, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/4.png"),
                    4, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/6.png"),
                    6, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/6.1.png"),
                    6, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/5.jpg"),
                    5, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/8.png"),
                    8, test.getTargetWidth(), test.getTargetHeight()));
            test.train(new LabeledImage(new File("src/main/resources/9.png"),
                    9, test.getTargetWidth(), test.getTargetHeight()));

        }
        NeuralNetwork.writeToFile(test, "src/main/resources/testWeights");

        //NeuralNetwork test = NeuralNetwork.readFromFile("src/main/resources/testWeights.json");
        List<LabeledImage> testData = IdxReader.loadTestData(100);

        int count = 0;
        for (LabeledImage image : testData) {
            if (test.predict(image.getMeanNormalizedPixel())[0] == image.getLabel()) {
                continue;
            }
            ++count;
        }
        System.out.println(count);

        System.out.println(test.predict(new NonLabeledImage("src/main/resources/0.jpg",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/2.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/3.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/4.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/5.jpg",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/6.1.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/8.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
        System.out.println(test.predict(new NonLabeledImage("src/main/resources/9.png",
                test.getTargetWidth(), test.getTargetHeight()))[0]);
       */
    }


}