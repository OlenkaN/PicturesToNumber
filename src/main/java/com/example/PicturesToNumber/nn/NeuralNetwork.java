package com.example.PicturesToNumber.nn;

/**
 * Created by klevis.ramo on 11/27/2017.
 */

import com.example.PicturesToNumber.data.IdxReader;
import com.example.PicturesToNumber.data.LabeledImage;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NeuralNetwork {

    private final static Logger LOGGER = LoggerFactory.getLogger(NeuralNetwork.class);

    private SparkSession sparkSession;
    private MultilayerPerceptronClassificationModel model;

    public void init() {
        //System.setProperty("hadoop.home.dir", "/usr/hdp/2.6.4.0-91/hadoop");
        initSparkSession();
        if (model == null) {
           LOGGER.info("Loading the Neural Network from saved model ... ");
            model = MultilayerPerceptronClassificationModel.load("C:\\Users\\Admin\\Documents\\program67\\Java\\PicturesToNumber\\PicturesToNumber\\src\\main\\resources\\nnTrainedModels\\ModelWith60000");
            LOGGER.info("Loading from saved model is done");
        }
    }

    public void train(Integer trainData, Integer testFieldValue) {

        initSparkSession();

        List<LabeledImage> labeledImages = IdxReader.loadData(trainData);
        List<LabeledImage> testLabeledImages = IdxReader.loadTestData(testFieldValue);

        Dataset<Row> train = sparkSession.createDataFrame(labeledImages, LabeledImage.class).checkpoint();
        Dataset<Row> test = sparkSession.createDataFrame(testLabeledImages, LabeledImage.class).checkpoint();

        // specify layers for the neural network:
        // input layer of size 784 , two intermediate of size 128 and 64
        // and output of size 10
        int[] layers = new int[]{784, 128, 64, 10};

        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setBlockSize(128) //Param for block size for stacking input data in matrices
                .setSeed(1234L)
                .setMaxIter(100);
        // train the model
        model = trainer.fit(train);

        evalOnTest(test);
        evalOnTest(train);
        try(FileWriter writer = new FileWriter("notes3.txt", true))
        {
            writer.write("Test label= " +testLabeledImages.get(1).getLabel() +"\n");
            LabeledImage temp=predict(testLabeledImages.get(1));
            writer.write("Test nn label= " +temp.getLabel() +"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void evalOnTest(Dataset<Row> test) {
        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        LOGGER.info("Test set accuracy = " + evaluator.evaluate(predictionAndLabels));
        try(FileWriter writer = new FileWriter("notes3.txt", true))
        {
            writer.write("Test set accuracy = " + evaluator.evaluate(predictionAndLabels)+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initSparkSession() {
        if (sparkSession == null) {
            sparkSession = SparkSession.builder()
                    .master("local[*]")
                    .appName("Digit Recognizer")
                    .getOrCreate();
        }

        sparkSession.sparkContext().setCheckpointDir("checkPoint");
    }

    public LabeledImage predict(LabeledImage labeledImage) {
        double predict = model.predict(labeledImage.getFeatures());
        labeledImage.setLabel(predict);
        return labeledImage;
    }
}
