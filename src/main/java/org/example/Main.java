package org.example;

public class Main {
    public static void main(String[] args){
        int[] mapSize = {400, 450};
        Simulation simulation = new Simulation(4, 2, mapSize);
        simulation.run();
    }

    public void getUserParameters() {
        // Get user input for simulation parameters
    }
}