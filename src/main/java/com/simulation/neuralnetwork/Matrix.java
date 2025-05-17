package com.simulation.neuralnetwork;

import java.util.function.Function;

public class Matrix {
    public double[][] data;
    public int rows;
    public int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    // Copy constructor
    public Matrix(Matrix other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.data[i], 0, this.data[i], 0, cols);
        }
    }

    // single row constructor
    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public Matrix(double[] inputs, int cols) {
        this.rows = inputs.length / cols;
        this.cols = cols;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = inputs[i * cols + j];
            }
        }
    }

    // Initialize with random values
    public static Matrix random(int rows, int cols, double scale) {
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m.data[i][j] = (Math.random() * 2 - 1) * scale; // [-scale, scale]
            }
        }
        return m;
    }

    // Initialize with zeros
    public static Matrix zeros(int rows, int cols) {
        return new Matrix(rows, cols);
    }

    // Transpose
    public static Matrix transpose(Matrix a) {
        Matrix result = new Matrix(a.cols, a.rows);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                result.data[j][i] = a.data[i][j];
            }
        }
        return result;
    }

    // Matrix multiplication
    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix result = new Matrix(a.rows, b.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < b.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    // Element-wise addition (matrix + matrix)
    public static Matrix add(Matrix a, Matrix b) {
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                result.data[i][j] = a.data[i][j] + b.data[i][j];
            }
        }
        return result;
    }

    // Element-wise function (e.g., ReLU)
    public static Matrix applyFunction(Matrix a, Function<Double, Double> func) {
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                result.data[i][j] = func.apply(a.data[i][j]);
            }
        }
        return result;
    }

    // Apply ReLU activation function
    public Matrix applyReLU() {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = Math.max(0, data[i][j]);
            }
        }
        return result;
    }

    // Apply Sigmoid activation function
    public Matrix applySigmoid() {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = 1 / (1 + Math.exp(-data[i][j]));
            }
        }
        return result;
    }
}
