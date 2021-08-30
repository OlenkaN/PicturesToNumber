package com.example.ptn.nn;

import com.example.ptn.data.LabeledImage;
import com.example.ptn.data.Matrix;
import com.example.ptn.data.NonLabeledImage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is a model of neural network.
 */

public class NeuralNetwork {
    public ArrayList<Matrix> weights = new ArrayList<>();
    public ArrayList<Matrix> outLayer = new ArrayList<>();
    public ArrayList<Matrix> bias = new ArrayList<>();

    private int layersAmount;
    private Integer targetWidth;
    private Integer targetHeight;
    private double lRate;
    // createdDateTime (Local date time)
    private UUID id = null;
    private Long version = null;
    private Timestamp create_on;


    public NeuralNetwork() {
    }

    /**
     * Constructor.
     *
     * @param layersAmount   amount of layers (include input and result)
     * @param layerDimension dimension of weights layers
     */
    public NeuralNetwork(int layersAmount, Integer[] layerDimension, int targetWidth, int targetHeight, double lRate) {
        this.lRate = lRate;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.layersAmount = layersAmount;
        for (int i = 0; i < layersAmount - 1; ++i) {
            weights.add(i, new Matrix(layerDimension[i + 1], layerDimension[i]));
            bias.add(i, new Matrix(layerDimension[i + 1], 1));
        }
    }

    /**
     * Constructor.
     *
     * @param layersAmount
     * @param targetWidth
     * @param targetHeight
     * @param lRate
     * @param id
     * @param version
     */
    public NeuralNetwork(int layersAmount,
                         int targetWidth,
                         int targetHeight,
                         double lRate,
                         UUID id,
                         Long version,
                         Timestamp create_on) {
        this.lRate = lRate;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.layersAmount = layersAmount;
        this.id = id;
        this.version = version;
        this.create_on = create_on;
    }


    /**
     * This method is used to predict the result with neural network.
     *
     * @param image to predict
     * @return result(the most likely digit)
     */
    public double[] predict(NonLabeledImage image) throws Exception {
        return predict(image.getMeanNormalizedPixel());
    }

    /**
     * This method is used to predict the result with neural network.
     *
     * @param imagePixels is data of image
     * @return result ( the most likely digit )
     */
    public double[] predict(double[] imagePixels) throws Exception {
        if (imagePixels.length != targetHeight * targetWidth) {
            throw new Exception("Your image has wrong dimension");
        }
        outLayer.add(0, Matrix.fromArray(imagePixels));
        for (int i = 0; i < layersAmount - 1; ++i) {
            Matrix hidden = Matrix.multiply(weights.get(i), outLayer.get(i));
            hidden.add(bias.get(i));
            hidden.sigmoid();
            outLayer.add(i + 1, hidden.clone());
        }
        return NeuralNetwork.maxIndex(outLayer.get(layersAmount - 1).toArray());
    }


    /**
     * Method to find index of max element to predict what digit is the most suitable.
     *
     * @param list of probability
     * @return index (digit)
     */
    public static double[] maxIndex(List<Double> list) {
        Double i = 0.0, maxIndex = -1.0, max = null;
        for (Double x : list) {
            if ((x != null) && ((max == null) || (x > max))) {
                max = x;
                maxIndex = i;
            }
            i++;
        }
        return new double[]{maxIndex, Math.round(max * 100)};
    }


    /**
     * This method is used to train nn.
     * First: predict a result
     * Second: find error
     * Third: change output layer and than hidden layer
     *
     * @param image is our train data
     */
    public void train(LabeledImage image) throws Exception {
        //here we get result from nn
        predict(image.getMeanNormalizedPixel());
        Matrix[] weightDelta = new Matrix[layersAmount - 1];
        Matrix[] biasDelta = new Matrix[layersAmount - 1];

        //after that we need to check how big is difference and than change weighs
        Matrix target = Matrix.fromArray(image.getResult());

        //By chain rule    1               2                      3
        //d(E_total)   d(E_total)        d(out_res)           d(hidden_sum)
        //--------- = ----------   *    ------------    *     ------------
        // d(W_ho1)    d(out_res)      d(hidden_sum)            d(W_ho1)


        //1
        ArrayList<Matrix> EtotalDout = new ArrayList<>(layersAmount);
        Matrix error = Matrix.subtract(target, outLayer.get(layersAmount - 1));
        error.multiply(lRate);
        EtotalDout.add(0, error.clone());
        //3
        Matrix gradient = outLayer.get(layersAmount - 1).dsigmoid();
        //1*2
        gradient.multiply(EtotalDout.get(0));
        EtotalDout.add(1, gradient.clone());

        Matrix hidden_T = Matrix.transpose(outLayer.get(layersAmount - 2));

        //1*2*3
        Matrix who_delta = Matrix.multiply(gradient, hidden_T);
        weightDelta[layersAmount - 2] = who_delta.clone();
        biasDelta[layersAmount - 2] = gradient.clone();


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
            EtotalDout.add(k + 1, h_gradient.clone());

            //5*6
            Matrix i_T = Matrix.transpose(outLayer.get(i - 1));
            Matrix wih_delta = Matrix.multiply(h_gradient, i_T);

            weightDelta[i - 1] = wih_delta.clone();
            biasDelta[i - 1] = h_gradient.clone();

        }
        for (int i = 0; i < layersAmount - 1; ++i) {
            weights.get(i).add(weightDelta[i]);
            bias.get(i).add(biasDelta[i]);
        }


    }

    /**
     * Save nn data to file.
     *
     * @param neuralNetwork to be written
     * @param fileName      to which file write
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
     * Method upload data into nn.
     *
     * @param fileName from which read
     * @return neuralNetwork with initialized  fields
     */
    public static NeuralNetwork readFromFile(String fileName) {
        NeuralNetwork neuralNetwork = null;
        String name = fileName;

        if (fileName == null) {
            name = "nn_data.json";
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public ArrayList<Matrix> getOutLayer() {
        return outLayer;
    }

    public ArrayList<Matrix> getBias() {
        return bias;
    }


    public double getlRate() {
        return lRate;
    }


    public void setlRate(double lRate) {
        this.lRate = lRate;
    }

    public Integer getTargetWidth() {
        return targetWidth;
    }

    public Integer getTargetHeight() {
        return targetHeight;
    }

    public void setTargetWidth(Integer targetWidth) {
        this.targetWidth = targetWidth;
    }

    public void setTargetHeight(Integer targetHeight) {
        this.targetHeight = targetHeight;
    }

    public String getId() {
        return id == null ? null : id.toString();
    }

    public Long getVersion() {
        return version;

    }

    public void setId(String id) {
        if (id == null)
            this.id = null;
        else {
            this.id = UUID.fromString(id);
        }
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setCreate_on(Timestamp create_on) {
        this.create_on = create_on;
    }

    public Timestamp getCreate_on() {
        return create_on;
    }

    public boolean equal(NeuralNetwork neuralNetwork) {
        for (int i = 0; i < layersAmount - 1; ++i) {
            if (!Matrix.equals(this.weights.get(i), neuralNetwork.weights.get(i))
                    || !Matrix.equals(this.bias.get(i), neuralNetwork.bias.get(i))) {
                System.out.println(i);
                return false;
            }
        }
        return true;
    }
}
