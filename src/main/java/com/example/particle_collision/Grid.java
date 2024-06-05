package com.example.particle_collision;

public class Grid {
    private double[] Grid;

    // For particle spawning purposes
    // To no have colliding particles
    // Upon the initialization
    public double[] CreateCoordinateGrid(int border_size, double radius){
        int Index = (int)(border_size/(radius*border_size))/2;

        Grid = new double[Index];
        for(int i = 0; i < Index; i++){
            double element = radius + i*2*radius;
            Grid[i] = element;

        }
        return Grid;
    }
}
