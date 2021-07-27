package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.LabeledImage;
import com.example.PicturesToNumber.data.Matrix;
import com.example.PicturesToNumber.data.NonLabeledImage;
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
    ArrayList<Matrix> weights = new ArrayList<Matrix>();
    ArrayList<Matrix> netLayer = new ArrayList<Matrix>();
    ArrayList<Matrix> outLayer = new ArrayList<Matrix>();
    ArrayList<Matrix> bias = new ArrayList<Matrix>();
    int layersAmount;
    int imageArraySize;

    double l_rate = 0.5;

    public NeuralNetwork() {
    }

    /**
     * Constructor
     * @param layersAmount amount of layers (include input and result)
     * @param layerDimension dimension of weights layers
     */
    public NeuralNetwork(int layersAmount, Integer[] layerDimension) {
        this.imageArraySize=layerDimension[0];
        this.layersAmount = layersAmount;
        for (int i = 0; i < layersAmount - 1; ++i) {
            weights.add(i, new Matrix(layerDimension[i + 1], layerDimension[i]));
            bias.add(i, new Matrix(layerDimension[i + 1], 1));
        }
    }


    /**
     * This method is used to predict the result with neural network
     * @param image
     * @return result( the most likely digit)
     */
    public double predict(NonLabeledImage image) throws Exception {
        return  predict(image.getMeanNormalizedPixel());
    }

    /**
     * This method is used to predict the result with neural network
     * @param imagePixels is data of image
     * @return result ( the most likely digit )
     */
    private double predict(double[] imagePixels) throws Exception {
        if(imagePixels.length!=imageArraySize)
        {
            throw  new Exception("Your image has wrong dimension");
        }
        outLayer.add(0, Matrix.fromArray(imagePixels));
        for (int i = 0; i < layersAmount - 1; ++i) {
            Matrix hidden = Matrix.multiply(weights.get(i), outLayer.get(i));
            hidden.add(bias.get(i));
            netLayer.add(i, hidden);
            hidden.sigmoid();
            outLayer.add(i + 1, hidden);
        }


        return NeuralNetwork.maxIndex(outLayer.get(layersAmount - 1).toArray());
    }


    /**
     * Method to find index of max element to predict what digit is the most suitable
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
     * @param image is our train data
     *
     */
    public void train(LabeledImage image) throws Exception {
        //here we get result from nn
        predict(image.getMeanNormalizedPixel());
        Matrix[] weightDelte = new Matrix[layersAmount - 1];
        Matrix[] biasDelta = new Matrix[layersAmount - 1];
        ;

        //after that we need to check how big is difference and than change weighs
        Matrix target = Matrix.fromArray(image.getResult());

        //By chain rule    1               2                      3
        //d(E_total)   d(E_total)        d(out_res)           d(hidden_sum)
        //--------- = ----------   *    ------------    *     ------------
        // d(W_ho1)    d(out_res)      d(hidden_sum)            d(W_ho1)


        //1
        ArrayList<Matrix> EtotalDout = new ArrayList<>(layersAmount);
        Matrix error = Matrix.subtract(target, outLayer.get(layersAmount - 1));
        error.multiply(l_rate);
        EtotalDout.add(0, error);
        //3
        Matrix gradient = outLayer.get(layersAmount - 1).dsigmoid();
        //1*2
        gradient.multiply(EtotalDout.get(0));
        EtotalDout.add(1, gradient);

        Matrix hidden_T = Matrix.transpose(outLayer.get(layersAmount - 2));

        //1*2*3
        Matrix who_delta = Matrix.multiply(gradient, hidden_T);
        weightDelte[layersAmount - 2] = who_delta;
        biasDelta[layersAmount - 2] = gradient;


        //for rest layers
        //Change hidden layer weight
        //By chain rule    4             5                6
        //d(E_total)  d(E_total)     d(out_hi)         d(input_sum)
        //--------- = ----------  *  -----------   *  ---------
        // d(W_hi1)    d(out_hi)     d(input_sum)      d(W_hi1)
        //4         =  1*2            *     7
        //d(E_total)   d(E_total)         d(hidden_sum)
        //---------  = ------------   *    -------------
        // d(out_hi)   d(hidden_sum)        d(out_hi)
        for (int i = layersAmount - 2, k = 1; i > 0; --i, ++k) {

            //7
            Matrix who_T = Matrix.transpose(weights.get(i));

            //4 =7*1*2
            Matrix hidden_errors = Matrix.multiply(who_T, EtotalDout.get(k));

            //5
            Matrix h_gradient = outLayer.get(i).dsigmoid();
            //4*5
            h_gradient.multiply(hidden_errors);
            EtotalDout.add(k + 1, h_gradient);

            //5*6
            Matrix i_T = Matrix.transpose(outLayer.get(i - 1));
            Matrix wih_delta = Matrix.multiply(h_gradient, i_T);

            weightDelte[i - 1] = wih_delta;
            biasDelta[i - 1] = h_gradient;

        }
        for(int i=0;i<layersAmount-1;++i)
        {
            weights.get(i).add(weightDelte[i]);
            bias.get(i).add(biasDelta[i]);
        }


    }

    /**
     * Save nn data to file
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
    public int getLayersAmount() {
        return layersAmount;
    }

    public void setWeights(ArrayList<Matrix> weights) {
        this.weights = weights;
    }

    public void setNetLayer(ArrayList<Matrix> netLayer) {
        this.netLayer = netLayer;
    }

    public void setOutLayer(ArrayList<Matrix> outLayer) {
        this.outLayer = outLayer;
    }

    public void setBias(ArrayList<Matrix> bias) {
        this.bias = bias;
    }

    public void setLayersAmount(int layersAmount) {
        this.layersAmount = layersAmount;
    }

    public ArrayList<Matrix> getWeights() {
        return weights;
    }

    public ArrayList<Matrix> getNetLayer() {
        return netLayer;
    }

    public ArrayList<Matrix> getOutLayer() {
        return outLayer;
    }

    public ArrayList<Matrix> getBias() {
        return bias;
    }


    public double getL_rate() {
        return l_rate;
    }


    public void setL_rate(double l_rate) {
        this.l_rate = l_rate;
    }

    public int getImageArraySize() {
        return imageArraySize;
    }

    public void setImageArraySize(int imageArraySize) {
        this.imageArraySize = imageArraySize;
    }
}
