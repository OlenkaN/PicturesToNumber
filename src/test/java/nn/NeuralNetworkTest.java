package nn;

import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.Matrix;

import com.example.PicturesToNumber.nn.NeuralNetwork;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;


public class NeuralNetworkTest {
    private NeuralNetwork testNN;

    @Before
    public void setUp() {
        testNN = new NeuralNetwork(3, new Integer[]{2, 2, 2});
        ArrayList<Matrix> weights = new ArrayList<>();
        weights.add(0, new Matrix(2, 2, new double[][]{{0.15, 0.20}, {0.25, 0.30}}));
        weights.add(1, new Matrix(2, 2, new double[][]{{0.40, 0.45}, {0.50, 0.55}}));
        testNN.setWeights(weights);
        ArrayList<Matrix> bias = new ArrayList<>();
        bias.add(0, new Matrix(2, 1, new double[][]{{0.35}, {0.35}}));
        bias.add(1, new Matrix(2, 1, new double[][]{{0.60}, {0.60}}));
        testNN.setBias(bias);
    }

    @Test
    public void predictOnTestData() throws Exception {

        Matrix outHidden = new Matrix(2, 1, new double[][]{{0.5932699921071872}, {0.596884378259767}});
        Matrix outOutput = new Matrix(2, 1, new double[][]{{0.7513650695523157}, {0.7729284653214625}});

        testNN.predict(new double[]{0.05, 0.1});
        assertTrue(Matrix.equals(outHidden, testNN.getOutLayer().get(1)));
        assertTrue(Matrix.equals(outOutput, testNN.getOutLayer().get(2)));
    }

    @Test
    public void trainOnTestData() throws Exception {

        Matrix weightsHidden = new Matrix(2, 2, new double[][]{{0.1497807161327628, 0.19956143226552567}, {0.24975114363236958, 0.29950228726473915}});
        Matrix weightsOutput = new Matrix(2, 2, new double[][]{{0.35891647971788465, 0.4086661860762334}, {0.5113012702387375, 0.5613701211079891}});


        LabeledImage toTrain = new LabeledImage(7, new double[]{0.05, 0.1});
        toTrain.setMeanNormalizedPixel(new double[]{0.05, 0.1});
        toTrain.setResult(new double[]{0.01, 0.99});
        testNN.train(toTrain);

        assertTrue(Matrix.equals(weightsHidden, testNN.getWeights().get(0)));
        assertTrue(Matrix.equals(weightsOutput, testNN.getWeights().get(1)));
    }

    @Test
    public void readFromFile() {
        NeuralNetwork.writeToFile(testNN, "src/main/resources/testNeuralNetwork");
        NeuralNetwork toCompare = NeuralNetwork.readFromFile("src/main/resources/testNeuralNetwork.json");
        assertTrue(testNN.getLayersAmount() == toCompare.getLayersAmount());
        int layerAmount = testNN.getLayersAmount();
        for (int i = 0; i < layerAmount - 2; ++i) {
            assertTrue(Matrix.equals(testNN.getWeights().get(i), toCompare.getWeights().get(i)));
            assertTrue(Matrix.equals(testNN.getBias().get(i), toCompare.getBias().get(i)));

        }

    }
}
