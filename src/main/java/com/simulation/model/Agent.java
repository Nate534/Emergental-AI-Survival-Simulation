package com.simulation.model;

import com.simulation.neuralnetwork.NeuralNetwork;
import com.simulation.shared.Config;
import com.simulation.shared.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private NeuralNetwork brain;
    private Coordinate position;
    private double energy;
    private double speed;
    private int age;

    public Agent(double x, double y, double energy, Config config) {
        this.position = new Coordinate(x, y);
        this.energy = energy;
        this.brain = new NeuralNetwork(config.getInt("neural.inputSize"), config.getInt("neural.hiddenSize"), config.getInt("neural.learningRate"));
        this.speed = config.getDouble("agent.moveSpeed");
        this.age = 0;
    }

    public Agent(double x, double y, double energy, NeuralNetwork brain) {
        this.position = new Coordinate(x, y);
        this.energy = energy;
        this.brain = new NeuralNetwork(brain);
        this.age = 0;
    }

    public NeuralNetwork getBrain() {
        return this.brain;
    }

    public Coordinate getPosition() {
        return this.position;
    }

    public double getEnergy() {
        return this.energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getAge() {
        return this.age;
    }

    // Use NN to decide actions
    public void act(World world) {
        // 1. Gather inputs (sensory data)
        double[] inputs = getSensoryInputs(world);

        // 2. Neural network decides action
        double[] outputs = brain.predict(inputs);

        // 3. Execute actions (e.g., move, eat)
        executeActions(outputs, world);

        // 4. Age and lose energy
        age++;
        energy -= 0.1; // Base metabolic cost
    }

    private double[] getSensoryInputs(World world) {
        List<Double> inputs = new ArrayList<>();

        // Find nearest food
        Food nearestFood = findNearestFood(world);
        inputs.add(nearestFood.getPosition().getX() - this.position.getX()); // Direction to food (x)
        inputs.add(nearestFood.getPosition().getY() - this.position.getY()); // Direction to food (y)
        inputs.add(energy / 100.0); // Normalized energy level

        // Find nearest agent
        Agent nearestAgent = findNearestAgent(world);
        inputs.add(nearestAgent.getPosition().getX() - this.position.getX()); // Direction to agent (x)
        inputs.add(nearestAgent.getPosition().getY() - this.position.getY()); // Direction to agent (y)
        inputs.add(nearestAgent.getEnergy()); // Normalized energy level of nearest agent

        return inputs.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private Food findNearestFood(World world) {
        return world.getFoods().stream()
                .min((f1, f2) -> Double.compare(distanceTo(f1.getPosition()), distanceTo(f2.getPosition())))
                .orElse(null);
    }

    private Agent findNearestAgent(World world) {
        return world.getAgents().stream()
                .min((a1, a2) -> Double.compare(distanceTo(a1.getPosition()), distanceTo(a2.getPosition())))
                .orElse(null);
    }

    private double distanceTo(Coordinate position) {
        return Math.sqrt(Math.pow(position.getX()-this.position.getX(), 2) + Math.pow(position.getY()- this.position.getY(), 2));
    }

    private void executeActions(double[] outputs, World world) {
        // Example: outputs = [dx, dy, eat_probability]
        double dx = outputs[0];
        double dy = outputs[1];
        double eatProbability = outputs[2];

        // Move
        this.position.move(dx, dy, speed);

        // Eat if near food and decides to
        if (eatProbability > 0.5) {
            Food food = findNearestFood(world);
            if (distanceTo(food.getPosition()) < 3) {
                energy += food.getNutrition();
                world.removeFood(food);
            }
        }
    }
}
