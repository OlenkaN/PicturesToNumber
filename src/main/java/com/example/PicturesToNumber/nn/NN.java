package com.example.PicturesToNumber.nn;

import com.example.PicturesToNumber.data.Matrix;


import java.util.List;

public class NN {


    Matrix weights_ih, weights_ho, bias_h, bias_o;
    double l_rate = 0.5;

    public NN(int i, int h, int o) {
        weights_ih = new Matrix(h, i);
        weights_ho = new Matrix(o, h);
        // weights_ih.print();
        //weights_ho.print();

        bias_h = new Matrix(h, 1);
        bias_o = new Matrix(o, 1);
        //bias_h.print();
        //bias_o.print();

    }

    public NN() {
    }


    public void NNForTest() {
        weights_ih = new Matrix(2, 2, new double[][]{{0.15, 0.20}, {0.25, 0.30}});
        weights_ih.print();
        weights_ho = new Matrix(2, 2, new double[][]{{0.40, 0.45}, {0.50, 0.55}});
        weights_ho.print();
        bias_h = new Matrix(2, 1, new double[][]{{0.35}, {0.35}});
        bias_h.print();
        bias_o = new Matrix(2, 1, new double[][]{{0.60}, {0.60}});
        bias_o.print();
    }


    public double predict(double[] X) {
        Matrix input = Matrix.fromArray(X);
        Matrix hidden = Matrix.multiply(weights_ih, input);
        hidden.add(bias_h);
        hidden.sigmoid();

        Matrix output = Matrix.multiply(weights_ho, hidden);
        output.add(bias_o);
        output.sigmoid();

        return NN.maxIndex(output.toArray());
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
