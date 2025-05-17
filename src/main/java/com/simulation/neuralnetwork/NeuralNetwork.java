package com.simulation.neuralnetwork;

public class NeuralNetwork {
    Matrix W1, b1, W2, b2;// Weights and biases

    public NeuralNetwork(int inputSize, int hiddenSize, double learningRate) {
        // Initialize parameters (small random values and zeros)
        W1 = Matrix.random(hiddenSize, inputSize, learningRate);
        b1 = Matrix.zeros(hiddenSize, 1);
        W2 = Matrix.random(1, hiddenSize, learningRate);
        b2 = Matrix.zeros(1, 1);
    }

    public NeuralNetwork(NeuralNetwork nn) {
        this.W1 = nn.W1;
        this.b1 = nn.b1;
        this.W2 = nn.W2;
        this.b2 = nn.b2;
    }

    public NeuralNetwork cloner() throws CloneNotSupportedException {
        return (NeuralNetwork) this.clone();
    }

    public Matrix getW1() {
        return W1;
    }

    public Matrix getB1() {
        return b1;
    }

    public Matrix getW2() {
        return W2;
    }

    public Matrix getB2() {
        return b2;
    }

    // Forward propagation
    public Matrix[] forward(Matrix X) {
        // Hidden layer
        Matrix Z1 = Matrix.add(Matrix.multiply(W1, X), b1);
        Matrix A1 = Matrix.applyFunction(Z1, x -> Math.max(0, x)); // ReLU

        // Output layer
        Matrix Z2 = Matrix.add(Matrix.multiply(W2, A1), b2);
        Matrix A2 = Matrix.applyFunction(Z2, x -> 1 / (1 + Math.exp(-x))); // Sigmoid

        return new Matrix[]{Z1, A1, Z2, A2};
    }

    // Compute binary cross-entropy loss
    public double computeLoss(Matrix A2, Matrix Y) {
        double loss = 0;
        for (int i = 0; i < A2.rows; i++) {
            for (int j = 0; j < A2.cols; j++) {
                double y = Y.data[i][j];
                double a = A2.data[i][j];
                loss += y * Math.log(a) + (1 - y) * Math.log(1 - a);
            }
        }
        return -loss / A2.cols; // Average over samples
    }

    // Backward propagation
    public Matrix[] backward(Matrix X, Matrix Y, Matrix Z1, Matrix A1, Matrix A2) {
        int m = X.cols; // Number of samples

        // Gradients for output layer
        Matrix dZ2 = Matrix.add(A2, Matrix.applyFunction(Y, y -> -y)); // A2 - Y
        Matrix dW2 = Matrix.multiply(dZ2, Matrix.transpose(A1));
        dW2 = Matrix.applyFunction(dW2, x -> x / m); // Scale by 1/m

        Matrix db2 = new Matrix(1, 1);
        for (int j = 0; j < dZ2.cols; j++) {
            db2.data[0][0] += dZ2.data[0][j];
        }
        db2.data[0][0] /= m;

        // Gradients for hidden layer
        Matrix dA1 = Matrix.multiply(Matrix.transpose(W2), dZ2);
        Matrix dZ1 = new Matrix(Z1.rows, Z1.cols);
        for (int i = 0; i < Z1.rows; i++) {
            for (int j = 0; j < Z1.cols; j++) {
                dZ1.data[i][j] = dA1.data[i][j] * (Z1.data[i][j] > 0 ? 1 : 0); // ReLU derivative
            }
        }

        Matrix dW1 = Matrix.multiply(dZ1, Matrix.transpose(X));
        dW1 = Matrix.applyFunction(dW1, x -> x / m);

        Matrix db1 = new Matrix(b1.rows, 1);
        for (int i = 0; i < dZ1.rows; i++) {
            for (int j = 0; j < dZ1.cols; j++) {
                db1.data[i][0] += dZ1.data[i][j];
            }
            db1.data[i][0] /= m;
        }

        return new Matrix[]{dW1, db1, dW2, db2};
    }

    // Update parameters using gradient descent
    public void updateParameters(Matrix dW1, Matrix db1, Matrix dW2, Matrix db2, double lr) {
        W1 = Matrix.applyFunction(W1, (w -> w - lr * dW1.data[w.i][w.j]));
        b1 = Matrix.applyFunction(b1, (b -> b - lr * db1.data[b.i][b.j]));
        W2 = Matrix.applyFunction(W2, (w -> w - lr * dW2.data[w.i][w.j]));
        b2 = Matrix.applyFunction(b2, (b -> b - lr * db2.data[b.i][b.j]));
    }

    public double[] predict(double[] inputs) {
        Matrix X = new Matrix(inputs, 1);
        Matrix[] forwardResult = forward(X);
        Matrix A2 = forwardResult[3];
        return A2.data[0];
    }
}
