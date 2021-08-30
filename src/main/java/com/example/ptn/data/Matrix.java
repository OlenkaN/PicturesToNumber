package com.example.ptn.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to save data and manipulate them in matrix form.
 */
public class Matrix implements Cloneable {
    public double[][] data;
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
        this.data = data.clone();
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
     * THis method add number to whole matrix.
     *
     * @param scalar addend
     */
    public void add(int scalar) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += scalar;
            }

        }
    }

    /**
     * Add two matrix.
     *
     * @param m matrix addend
     * @return matrix of result
     */

    public Matrix add(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            System.out.println("Shape Mismatch");
            return null;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] += m.data[i][j];
            }
        }
        return this;
    }

    /**
     * Convert array to matrix.
     *
     * @param x is array of elements
     * @return matrix made from array
     */
    public static Matrix fromArray(double[] x) {
        Matrix temp = new Matrix(x.length, 1);
        for (int i = 0; i < x.length; i++)
            temp.data[i][0] = x[i];
        return temp;

    }

    /**
     * Matrix to array.
     *
     * @return list of double made from our matrix
     */
    public List<Double> toArray() {
        List<Double> temp = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp.add(data[i][j]);
            }
        }
        return temp;
    }

    /**
     * This method subtracts the matrix b from the matrix b.
     *
     * @param a minuend
     * @param b subtrahend
     * @return difference
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
     * Transposition of matrix.
     *
     * @param a matrix to be transpose
     * @return result
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
     * Multiply two matrix and save result to a new one.
     *
     * @param a multiplicand
     * @param b Multiplier
     * @return matrix of product
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
     * Multiply current matrix one by one by the matrix a.
     *
     * @param a multiplier matrix
     */
    public void multiply(Matrix a) {
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                this.data[i][j] *= a.data[i][j];
            }
        }

    }


    /**
     * Multiply matrix by number a.
     *
     * @param a multiplier number
     */
    public void multiply(double a) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] *= a;
            }
        }

    }


    /**
     * Take sigmoid function of each element of matrix.
     */
    public void sigmoid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
        }

    }

    /**
     * Take derivative of a function of each element of matrix.
     */
    public Matrix dsigmoid() {
        Matrix temp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                temp.data[i][j] = this.data[i][j] * (1 - this.data[i][j]);
        }
        return temp;

    }

    /**
     * checks whether the matrices are equal.
     *
     * @param one matrix
     * @param two matrix
     * @return result true or false
     */
    public static boolean equals(Matrix one, Matrix two) {
        if (one.getRows() != two.getRows() || one.getCols() != two.getCols()) {
            return false;
        }
        int count = 0;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/ERRORS"));
            for (int i = 0; i < one.getRows(); ++i) {
                for (int j = 0; j < one.getCols(); ++j) {
                    if (one.data[i][j] != two.data[i][j]) {
                        writer.write("row: " + i + " col: " + j);
                        writer.write(" " + one.data[i][j] + "   " + two.data[i][j] + "\n");
                        writer.write("--------------------------------------------------------------");
                        ++count;

                    }
                }
            }
            writer.close();
            return count == 0 ? true : false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * Deep copy of matrix.
     *
     * @return new matrix
     */
    public Matrix clone() {
        Matrix clone = new Matrix();
        clone.setCols(this.getCols());
        clone.setRows(this.getRows());
        clone.data = new double[rows][];
        for (int i = 0; i < rows; i++)
            clone.data[i] = this.data[i].clone();
        return clone;
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
