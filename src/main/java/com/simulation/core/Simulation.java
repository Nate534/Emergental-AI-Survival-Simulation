package com.simulation.core;

import com.simulation.model.World;
import com.simulation.neuralnetwork.GeneticAlgorithm;
import com.simulation.neuralnetwork.NeuralNetwork;
import com.simulation.shared.Config;

import java.util.List;

public class Simulation {
    public static void main(String[] args) {
        World world = new World(new Config("src/main/resources/config.properties")); // Width, height, agents, food

        for (int generation = 0; generation < 1000; generation++) {
            // Simulate for 1000 steps per generation
            for (int step = 0; step < 1000; step++) {
                world.update();
            }

            // Evolve agents
            List<NeuralNetwork> nextGen = GeneticAlgorithm.evolve(
                    world.getAgentBrains(), 0.01
            );

        }
    }
}
