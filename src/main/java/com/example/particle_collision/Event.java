package com.example.particle_collision;

class Event implements Comparable<Event> {
    private  double time;
    private  Particle particle1;
    private  Particle particle2;
    private  int collisionCount1;
    private  int collisionCount2;



    //Collisions event
    public Event(double t, Particle a, Particle b) {
        this.time = t;
        this.particle1 = a;
        this.particle2 = b;
        this.collisionCount1 = (a != null) ? a.getCollisionCount() : -1;
        this.collisionCount2 = (b != null) ? b.getCollisionCount() : -1;

    }

    public double getTime() {
        return time;
    }

    public Particle getParticle1() {
        return particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    public boolean wasSuperevening(){
        if(particle1 != null && particle1.getCollisionCount() != collisionCount1) return false;
        if(particle2 != null && particle2.getCollisionCount() != collisionCount2) return false;
        return true;


    }


}
