package com.example.ptn.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to save data and manipulate them in matrix form.
 */
public class Matrix implements Cloneable {

  public double[][] data;
  public int rows;
  public int cols;

  /**
   * Constructor.
   */
  public Matrix() {
  }

  /**
   * Constructor with random filling.
   *
   * @param rows amount
   * @param cols amount
   */
  public Matrix(final int rows, final int cols) {
    data = new double[rows][cols];
    this.rows = rows;
    this.cols = cols;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        data[i][j] = Math.random() * 2 - 1;
      }
    }
  }

  /**
   * Constructor copy data.
   *
   * @param rows amount
   * @param cols amount
   * @param data to copy
   */
  public Matrix(final int rows, final int cols, final double[][] data) {
    this.data = data.clone();
    this.rows = rows;
    this.cols = cols;
  }

  /**
   * To print method.
   */
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
  public void add(final int scalar) {
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

  public Matrix add(final Matrix m) {
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
  public static Matrix fromArray(final double[] x) {
    Matrix temp = new Matrix(x.length, 1);
    for (int i = 0; i < x.length; i++) {
      temp.data[i][0] = x[i];
    }
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
  public static Matrix subtract(final Matrix a, final Matrix b) {
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
  public static Matrix transpose(final Matrix a) {
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
  public static Matrix multiply(final Matrix a, final Matrix b) {
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
  public void multiply(final Matrix a) {
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
  public void multiply(final double a) {
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
      for (int j = 0; j < cols; j++) {
        this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
      }
    }

  }

  /**
   * Take derivative of a function of each element of matrix.
   *
   * @return Matrix returned
   */
  public Matrix dsigmoid() {
    Matrix temp = new Matrix(rows, cols);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        temp.data[i][j] = this.data[i][j] * (1 - this.data[i][j]);
      }
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
  public static boolean equals(final Matrix one, final Matrix two) {
    if (one.getRows() != two.getRows() || one.getCols() != two.getCols()) {
      return false;
    }
    for (int i = 0; i < one.getRows(); ++i) {
      for (int j = 0; j < one.getCols(); ++j) {
        if (one.data[i][j] != two.data[i][j]) {
          return false;
        }
      }
    }
    return true;

  }

  /**
   * Deep copy of matrix.
   *
   * @return new matrix
   */
  public Matrix clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    Matrix clone = new Matrix();
    clone.setCols(this.getCols());
    clone.setRows(this.getRows());
    clone.data = new double[rows][];
    for (int i = 0; i < rows; i++) {
      clone.data[i] = this.data[i].clone();
    }
    return clone;
  }

  /**
   * Setter for data.
   *
   * @param data to set
   */
  public void setData(final double[][] data) {
    this.data = data;
  }

  /**
   * Setter rows.
   *
   * @param rows to set
   */
  public void setRows(final int rows) {
    this.rows = rows;
  }

  /**
   * Setter for cols.
   *
   * @param cols to set
   */
  public void setCols(final int cols) {
    this.cols = cols;
  }

  /**
   * Getter for data.
   *
   * @return data
   */
  public double[][] getData() {
    return data;
  }

  /**
   * Getter for rows.
   *
   * @return rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Getter for cols.
   *
   * @return cols
   */
  public int getCols() {
    return cols;
  }
}
