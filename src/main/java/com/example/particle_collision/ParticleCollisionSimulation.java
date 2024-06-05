package com.example.particle_collision;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ParticleCollisionSimulation extends Application {
    private PriorityQueue<Event> eventList = new PriorityQueue<>();
    private double t = 0.0;
    private double limit = Double.MAX_VALUE;
    private List<Particle> particles = new ArrayList<>();
    private static final int BORDERSIZE = 1028;
    private int n = 100; // Particle count
    private double r = 0.04; // Radius
    private double[] grid;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Particle Collision Simulation");
        Canvas canvas = new Canvas(BORDERSIZE, BORDERSIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Grid gridClass = new Grid();
        grid = gridClass.CreateCoordinateGrid(BORDERSIZE, r);

        if(n < Math.pow((int)(BORDERSIZE/(r*BORDERSIZE))/2, 2)) {
            for (int i = 0; i < n; i++) {
                particles.add(new Particle(Color.RED, i, BORDERSIZE, grid, r));
            }

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    handleCollisions(gc);
                }
            }.start();

            Scene scene = new Scene(root, BORDERSIZE, BORDERSIZE);
            primaryStage.setScene(scene);
            primaryStage.show();

            new Thread(this::simulate).start();
        }
        else{
            System.out.println("Particle amount/size exceeed grid limit");
        }
    }

    private void handleCollisions(GraphicsContext gc) {

        Platform.runLater(() -> redraw(gc));
    }

    private void simulate() {
        while (t < limit) {
            //long beginRead = System.currentTimeMillis();
            for (int i = 0; i < particles.size(); i++) {
                collisionCalculation(particles.get(i), limit);
            }
            eventList.add(new Event(0, null, null));

            while (!eventList.isEmpty()) {
                Event event = eventList.poll();
                if (!event.wasSuperevening()) {
                    continue;
                }

                Particle a = event.getParticle1();
                Particle b = event.getParticle2();

                double allDelta = event.getTime() - t;
                double temp;
                while (allDelta != 0){
                    temp = Math.min(allDelta,10);
                    allDelta -= temp;

                    for (int i = 0; i < particles.size(); i++) {
                        particles.get(i).movement(temp);
                    }

                    try {
                        Thread.sleep((long)temp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    redraw(null);

                }
                t = event.getTime();

                if (a != null && b != null) a.bounce(b);
                else if (a != null) a.bounceX();
                else if (b != null) b.bounceY();
                else if (b == null) redraw(null);

                collisionCalculation(a, limit);
                collisionCalculation(b, limit);
            }
        }
    }
    // Drawing the particles on the canvas
    private void redraw(GraphicsContext gc) {
        if (gc != null) {
            gc.clearRect(0, 0, BORDERSIZE, BORDERSIZE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeRect(0, 0, BORDERSIZE, BORDERSIZE);

            for (Particle particle : particles) {
                gc.setFill(particle.getColor());
                gc.fillOval(
                        (particle.getX() - particle.getRadius() / 2) * BORDERSIZE,
                        (particle.getY() - particle.getRadius() / 2) * BORDERSIZE,
                        particle.getRadius() * BORDERSIZE,
                        particle.getRadius() * BORDERSIZE);
            }
        }
    }
    // Calculates which collision are happening
    private void collisionCalculation(Particle a, double limit) {
        if (a == null) return;

        for (int i = 0; i < particles.size(); i++) {
            double dt = a.collides(particles.get(i));
            if (t + dt < limit) {
                eventList.add(new Event(t + dt, a, particles.get(i)));
            }
        }

        double dtX = a.collidesX();
        double dtY = a.collidesY();
        if (t + dtX < limit) {
            eventList.add(new Event(t + dtX, a, null));
        }
        if (t + dtY < limit) {
            eventList.add(new Event(t + dtY, null, a));
        }
    }

}





