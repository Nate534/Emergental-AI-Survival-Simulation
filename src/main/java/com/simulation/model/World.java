package com.simulation.model;

import com.simulation.neuralnetwork.NeuralNetwork;
import com.simulation.shared.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final int width, height;// World dimensions
    private List<Agent> agents;
    private List<Food> foods;
    private final int maxFood;// Max food allowed
    private final Config config;

    public World(Config config) {
        this.config = config;
        this.width = config.getInt("world.width");
        this.height = config.getInt("world.height");
        this.maxFood = config.getInt("world.maxFood");
        agents = new ArrayList<>();
        foods = new ArrayList<>();
        spawnAgents(config.getInt("world.agentCount"));
        spawnFood();
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void removeFood(Food food) {
        foods.remove(food);
    }

    public List<Food> getFoods() {
        return this.foods;
    }

    private void spawnFood() {
        while (foods.size() < maxFood) {
            addFood(new Food(
                    (int) (Math.random() * width),
                    (int) (Math.random() * height),
                    50.0)); // Nutritional value);
        }
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void removeAgent(Agent agent) {
        agents.remove(agent);
    }

    public List<Agent> getAgents() {
        return this.agents;
    }

    public List<NeuralNetwork> getAgentBrains() {
        List<NeuralNetwork> brains = new ArrayList<>();
        for (Agent agent : agents) {
            brains.add(agent.getBrain());
        }
        return brains;
    }

    private void spawnAgents(int count) {
        for (int i = 0; i < count; i++) {
            addAgent(new Agent(
                    (int) (Math.random() * width),
                    (int) (Math.random() * height),
                    100.0, // Initial energy
                    this.config
            ));
        }
    }

    public void update() {
        // 1. Agents act (move, eat, reproduce)
        for (Agent agent : agents) {
            agent.act(this); // Pass world reference for sensing
        }

        // 2. Remove dead agents
        agents.removeIf(agent -> agent.getEnergy() <= 0);

        // 3. Respawn food periodically
        if (Math.random() < 0.1) spawnFood();
    }

    public void reset() {
        agents.clear();
        foods.clear();
        spawnAgents(config.getInt("world.agentCount"));
        spawnFood();
    }
}
