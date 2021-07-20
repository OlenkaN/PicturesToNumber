package com.example.PicturesToNumber.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to save data and manipulate them in matrix form
 */
public class Matrix {
    double[][] data;
    int rows, cols;

    public Matrix() {
    }

    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    public Matrix(int rows, int cols, double[][] data) {
        this.data = data;
        this.rows = rows;
        this.cols = cols;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(this.data[i][j] + " ");
            }
            System.out.println();
        }
    }


    /**
     * THis method add number to whole matrix
     *
     * @param scalar
     */
    public void add(int scalar) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += scalar;
            }

        }
    }

    /**
     * Add two matrix
     *
     * @param m
     */
    public void add(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            System.out.println("Shape Mismatch");
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += m.data[i][j];
            }
        }
    }

    /**
     * Convert array to matrix
     *
     * @param x
     * @return
     */
    public static Matrix fromArray(double[] x) {
        Matrix temp = new Matrix(x.length, 1);
        for (int i = 0; i < x.length; i++)
            temp.data[i][0] = x[i];
        return temp;

    }

    /**
     * Matrix to array
     *
     * @return
     */
    public List<Double> toArray() {
        List<Double> temp = new ArrayList<Double>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.add(data[i][j]);
            }
        }
        return temp;
    }

    /**
     * This method subtracts the matrix b from the matrix b
     *
     * @param a
     * @param b
     * @return
     */
    public static Matrix subtract(Matrix a, Matrix b) {
        Matrix temp = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[i][j] = a.data[i][j] - b.data[i][j];
            }
        }
        return temp;
    }


    /**
     * Transposition of matrix
     *
     * @param a
     * @return
     */
    public static Matrix transpose(Matrix a) {
        Matrix temp = new Matrix(a.cols, a.rows);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                temp.data[j][i] = a.data[i][j];
            }
        }
        return temp;
    }

    /**
     * Multiply two matrix and save result to a new one
     *
     * @param a
     * @param b
     * @return
     */
    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix temp = new Matrix(a.rows, b.cols);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                temp.data[i][j] = sum;
            }
        }
        return temp;
    }

    /**
     * Multiply current matrix one by one by the matrix a
     *
     * @param a
     */
    public void multiply(Matrix a) {
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                this.data[i][j] *= a.data[i][j];
            }
        }

    }


    /**
     * Multiply matrix by number a
     *
     * @param a
     */
    public void multiply(double a) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] *= a;
            }
        }

    }


    /**
     * Take sigmoid function of each element of matrix
     */
    public void sigmoid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
        }

    }

    /**
     * Take derivative of a function of each element of matrix
     */
    public Matrix dsigmoid() {
        Matrix temp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                temp.data[i][j] = this.data[i][j] * (1 - this.data[i][j]);
        }
        return temp;

    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public double[][] getData() {
        return data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
