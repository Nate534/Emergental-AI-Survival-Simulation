package com.simulation.neuralnetwork;



public class GeneticAlgorithm {

    private static NeuralNetwork crossover(NeuralNetwork parent1, NeuralNetwork parent2) {
        NeuralNetwork child = new NeuralNetwork(parent1);
        // Crossover weights from parent1 and parent2
        for (int i = 0; i < parent1.getW1().rows; i++) {
            for (int j = 0; j < parent1.getW1().cols; j++) {
                if (Math.random() > 0.5) {
                    child.getW1().data[i][j] = parent2.getW1().data[i][j];
                }
            }
        }

        for (int i = 0; i < parent1.getW2().rows; i++) {
            for (int j = 0; j < parent1.getW2().cols; j++) {
                if (Math.random() > 0.5) {
                    child.getW2().data[i][j] = parent2.getW2().data[i][j];
                }
            }
        }

        return child;
    }

    private static void mutate(NeuralNetwork nn, double mutationRate) {
        mutateMatrix(nn.getW1(), mutationRate);
        mutateMatrix(nn.getW2(), mutationRate);
    }

    private static void mutateMatrix(Matrix m, double rate) {
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                if (Math.random() < rate) {
                    m.data[i][j] += (Math.random() - 0.5) * 0.1;
                }
            }
        }
    }
}
