package com.simulation.model;

import com.simulation.shared.Coordinate;

public class Food {
    private Coordinate position;
    private double nutrition;

    public Food(double x, double y, double nutrition) {
        this.position = new Coordinate(x, y);
        this.nutrition = nutrition;
    }

    public Coordinate getPosition() {
        return position;
    }

    public double getNutrition() {
        return nutrition;
    }
}
