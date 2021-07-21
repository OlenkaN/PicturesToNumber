package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.Matrix;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a model of neural network
 */
public class NeuralNetwork {
    ArrayList<Matrix> weights;
    ArrayList<Matrix> netLayer;
    ArrayList<Matrix> outLayer;
    ArrayList<Matrix> bias;
    int layersAmount;


    Matrix weights_ih, weights_ho, bias_h, bias_o;
    double l_rate = 0.5;

    public NeuralNetwork(int layersAmount, ArrayList<Integer> layerDimension) {
        this.layersAmount=layersAmount;
        netLayer = new ArrayList<Matrix>(layersAmount-1);
        outLayer = new ArrayList<Matrix>(layersAmount);
        weights = new ArrayList<Matrix>(layersAmount - 1);
        bias = new ArrayList<Matrix>(layersAmount - 1);
        for (int i =0; i<layerDimension.size(); ++i) {
            weights.add(i,new Matrix(layerDimension.get(i+1),layerDimension.get(i)));
            bias.add(i,new Matrix(layerDimension.get(i+1),1));
        }
    }

    public NeuralNetwork(int i, int h, int o) {

        weights_ih = new Matrix(h, i);
        weights_ho = new Matrix(o, h);


        bias_h = new Matrix(h, 1);
        bias_o = new Matrix(o, 1);
    }

    public NeuralNetwork() {
    }

    /**
     * This method is used to predict the result with nn
     *
     * @param X
     * @return result ( the most likely )
     */
    public double predict(double[] X) {
        outLayer.set(0, Matrix.fromArray(X));
        for(int i=1;i<layersAmount;++i)
        {
            Matrix hidden = Matrix.multiply(weights.get(i), outLayer.get(i-1));
            hidden.add(bias.get(i));
            netLayer.set(i,hidden);
            hidden.sigmoid();
            outLayer.set(i,hidden);
        }


        return NeuralNetwork.maxIndex(outLayer.get(layersAmount-1).toArray());
    }


    /**
     * Method to find index of max element to predict what digit is the most suitable
     *
     * @param list
     * @return index (digit)
     */
    public static Double maxIndex(List<Double> list) {
        Double i = 0.0, maxIndex = -1.0, max = null;
        for (Double x : list) {
            if ((x != null) && ((max == null) || (x > max))) {
                max = x;
                maxIndex = i;
            }
            i++;
        }
        return maxIndex;
    }


    /**
     * This method is used to train nn
     * First: predict a result
     * Second: find error
     * Third: change output layer and than hidden layer
     *
     * @param X is our train data
     * @param Y is what we expect to get
     */
    public void train(double[] X, double[] Y) {
        //here we get result from nn
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden.sigmoid();

        Matrix output = Matrix.multiply(weights_ho, hidden);
        output.add(bias_o);
        output.sigmoid();


        //after that we need to check how big is difference and than change weighs
        Matrix target = Matrix.fromArray(Y);

        //By chain rule    1               2                      3
        //d(E_total)   d(E_total)        d(out_res)           d(hidden_sum)
        //--------- = ----------   *    ------------    *     ------------
        // d(W_ho1)    d(out_res)      d(hidden_sum)            d(W_ho1)

        //1
        Matrix error = Matrix.subtract(target, output); // size r=10 c=1

        //2
        Matrix gradient = output.dsigmoid(); // size r=10 c=1

        //1*2
        gradient.multiply(error);
        gradient.multiply(l_rate);

        Matrix hidden_T = Matrix.transpose(hidden);

        //1*2*3
        Matrix who_delta = Matrix.multiply(gradient, hidden_T);

        //Change weight out
        weights_ho.add(who_delta);
        bias_o.add(gradient);

        //Change hidden layer weight
        //By chain rule    4             5                6
        //d(E_total)  d(E_total)     d(out_hi)         d(input_sum)
        //--------- = ----------  *  -----------   *  ---------
        // d(W_hi1)    d(out_hi)     d(input_sum)      d(W_hi1)
        //4         =  1*2            *     7
        //d(E_total)   d(E_total)         d(hidden_sum)
        //---------  = ------------   *    -------------
        // d(out_hi)   d(hidden_sum)        d(out_hi)

        //7
        Matrix who_T = Matrix.transpose(weights_ho);

        //4 =7*1*2
        Matrix hidden_errors = Matrix.multiply(who_T, gradient);

        //5
        Matrix h_gradient = hidden.dsigmoid();
        //4*5
        h_gradient.multiply(hidden_errors);

        //5*6
        Matrix i_T = Matrix.transpose(input);
        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);

        weights_ih.add(wih_delta);
        bias_h.add(h_gradient);

    }
    /**
     * Save nn data to file
     *
     * @param neuralNetwork
     * @param fileName
     */
    public static void writeToFile(NeuralNetwork neuralNetwork, String fileName) {
        String name = fileName;

        if (fileName == null) {
            name = "nn_data";
        }

        try {
            FileWriter file = new FileWriter(name + ".json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, neuralNetwork);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method upload data into nn
     *
     * @param fileName
     * @return nn with initialized  fields
     */
    public static NeuralNetwork readFromFile(String fileName) {
        NeuralNetwork neuralNetwork = null;
        String name = fileName;

        if (fileName == null) {
            name = "nn_data.json";
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            neuralNetwork = objectMapper.readValue(new File(name), NeuralNetwork.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return neuralNetwork;
    }

    public Matrix getWeights_ih() {
        return weights_ih;
    }

    public Matrix getWeights_ho() {
        return weights_ho;
    }

    public Matrix getBias_h() {
        return bias_h;
    }

    public Matrix getBias_o() {
        return bias_o;
    }

    public double getL_rate() {
        return l_rate;
    }

    public void setWeights_ih(Matrix weights_ih) {
        this.weights_ih = weights_ih;
    }

    public void setWeights_ho(Matrix weights_ho) {
        this.weights_ho = weights_ho;
    }

    public void setBias_h(Matrix bias_h) {
        this.bias_h = bias_h;
    }

    public void setBias_o(Matrix bias_o) {
        this.bias_o = bias_o;
    }

    public void setL_rate(double l_rate) {
        this.l_rate = l_rate;
    }
}
