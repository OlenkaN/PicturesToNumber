package nn;

import com.example.PicturesToNumber.data.Matrix;
import com.example.PicturesToNumber.data.NonLabeledImage;
import com.example.PicturesToNumber.nn.NeuralNetwork;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetworkTest {
    private NeuralNetwork testNN;
    @Before
    public void setUp()  {
        testNN = new NeuralNetwork(3,new Integer[]{2,2,2});
        ArrayList<Matrix> weights=new ArrayList<>();
        weights.add(0,new Matrix(2, 2, new double[][]{{0.15, 0.20}, {0.25, 0.30}}));
        weights.add(1, new Matrix(2, 2, new double[][]{{0.40, 0.45}, {0.50, 0.55}}));
        testNN.setWeights(weights);
        ArrayList<Matrix> bias=new ArrayList<>();
        bias.add(0,new Matrix(2, 1, new double[][]{{0.35}, {0.35}}));
        bias.add(1,new Matrix(2, 1, new double[][]{{0.60}, {0.60}}));
        testNN.setBias(bias);
    }
    @Test
    public void predictOnTestData() throws Exception {
        Matrix nethidden=new Matrix(2,1,new double[][]{{0.3775},{0.39249999999999996}});
        testNN.predict(new double[]{0.05,0.1});
        assertTrue(Arrays.equals(nethidden.getData(),testNN.getNetLayer().get(0).getData()));
    }
}
