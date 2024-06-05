package com.example.particle_collision;

import javafx.scene.paint.Color;
import java.util.*;


// Particle object
public class Particle {
    private double x;
    private double y;
    private double radius;
    private Color color;
    private double velocityX;
    private double velocityY;
    private double mass;
    private int collisionCount;
    private int N = 2;
    private static final Random r = new Random();

    public Particle(Color color, int index, int Grid_Size, double[] grid, double radius) {
        this.radius = radius;
        this.x = grid[(int)(index%((int)(Grid_Size/(this.radius*Grid_Size))/2))];
        this.y = grid[(int)(index/((int)(Grid_Size/(this.radius*Grid_Size))/2))];
        this.color = color;
        this.velocityX = 0.0005 * r.nextDouble() - 0.0005;
        this.velocityY = 0.0005 * r.nextDouble() - 0.0005;
        this.mass = 1;
        this.collisionCount = 0;
    }
    public Particle(double x, double y, double velocityx, double velocityy, double radius, Color color, double  mass) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
        this.velocityX = velocityx;
        this.velocityY = velocityy;
        this.mass = mass;
        this.collisionCount = 0;
    }
    // Collision on x axis wall
    public double collidesX(){
        if(this.velocityX > 0){
            return ((1-this.radius/2-this.x)/this.velocityX);
        }
        if(this.velocityX<0){
            return ((this.radius/2-this.x)/this.velocityX);
        }
        else{
            return Double.MAX_VALUE;
        }
    }
    // Collision on y axis wall
    public double collidesY(){
        if(this.velocityY> 0){
            return ((1-this.radius/2-this.y)/this.velocityY);
        }
        if(this.velocityY<0){
            return ((this.radius/2-this.y)/this.velocityY);
        }
        else{
            return Double.MAX_VALUE;
        }
    }
    // Collision between particles
    public double collides(Particle b){
        double dX;
        double dY;
        double dVX;
        double dVY;
        double d;
        double ro = this.radius/2+b.getRadius()/2;
        dX = this.x - b.getX();
        dY = this.y - b.getY();
        dVX = this.velocityX - b.getVelocityX();
        dVY = this.velocityY - b.getVelocityY();
        d = Math.pow((dVX*dX + dVY*dY),2) - (Math.pow(dVX,2) +Math.pow(dVY,2))*(Math.pow(dX,2)+Math.pow(dY,2)-Math.pow(ro,2));
        if((dVX*dX + dVY*dY) > 0){
            return Double.MAX_VALUE;
        }
        if(d < 0){
            return Double.MAX_VALUE;
        }
        else{
            return -(((dVX*dX + dVY*dY)+Math.sqrt(d))/(Math.pow(dVX,2) +Math.pow(dVY,2)));
        }


    }
    // Bounce logic from x axis wall
    public void bounceX(){
        setVX(-this.velocityX);
        collisionCount++;

    }
    // Bounce logic from y axis wall
    public void bounceY(){
        setVY(-this.velocityY);
        collisionCount++;
    }
    // Bounce logic between particles
    public void bounce(Particle b){
        double dX;
        double dY;
        double dVX;
        double dVY;
        double ro = this.radius/2+b.getRadius()/2;
        double JX;
        double JY;
        dX = b.getX() - this.x;
        dY = b.getY() - this.y;
        dVX = b.getVelocityX() - this.velocityX;
        dVY = b.getVelocityY() - this.velocityY;
        double J = (2*this.mass*b.getMass()*(dVX*dX + dVY*dY))/(ro*(this.mass+ b.getMass()));
        JX = (J*dX)/ro;
        JY = (J*dY)/ro;
        this.setVX(this.velocityX+JX/this.mass);
        this.setVY(this.velocityY+JY/this.mass);
        b.setVX(b.getVelocityX()-JX/b.getMass());
        b.setVY(b.getVelocityY()-JY/b.getMass());
        this.collisionCount++;
        b.setCollisionCount();
    }
    // Particles movement change
    public void movement(double dt){
        this.setX(this.x+this.velocityX*dt);
        this.setY(this.y + this.velocityY*dt);
        //System.out.println(1/dt);
    }
    public int getCollisionCount(){
        return this.collisionCount;
    }
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getRadius() {
        return this.radius;
    }

    public Color getColor() {
        return this.color;
    }
    public double getVelocityX(){
        return this.velocityX;
    }
    public double getVelocityY(){
        return this.velocityY;
    }
    public void setX(double n){
        this.x = n;
    }
    public void setVX(double n){
        this.velocityX = n;
    }
    public void setVY(double n){
        this.velocityY = n;
    }
    public void setY(double n){
        this.y = n;
    }
    public double getMass(){
        return this.mass;
    }
    public void setCollisionCount(){
        this.collisionCount++;
    }
}
