package Particles;

import Data.Frame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleSystem {
    // Fields
    private Particle[] particles;
    private int x, y;
    private int xRange, yRange;
    private int maxLife;
    private String[] spriteTags;

    // Constructor
    public ParticleSystem(int numParticles, int x, int y, int xRange, int yRange, int minLife, int maxLife, int xMove,
                          int yMove, int minDelay, int maxDelay, String[] spriteTags) {
        this.xRange = xRange;
        this.yRange = yRange;
        this.x = x;
        this.y = y;
        this.maxLife = maxLife;
        particles = new Particle[numParticles];
        this.spriteTags = spriteTags;
        initParticles(xMove, yMove, minDelay, maxDelay, minLife);
    }

    public ParticleSystem(int numParticles, int x, int y, int minLife, int maxLife, int xMove,
                          int yMove, int minDelay, int maxDelay, String[] spriteTags) {
        this.xRange = 0;
        this.yRange = 0;
        this.x = x;
        this.y = y;
        this.maxLife = maxLife;
        particles = new Particle[numParticles];
        this.spriteTags = spriteTags;
        initParticles(minDelay, maxDelay, minLife);
    }

    // Methods
    private void initParticles(int xMove, int yMove, int minDelay, int maxDelay, int minLife) {
        for (int i = 0; i < particles.length; i++) {
            int n = spriteTags.length;
            int index = Particle.getRandomInt(0, n - 1);
            particles[i] = new Particle(x, (x + xRange), y, (y + yRange), spriteTags[index], minLife, maxLife, xMove,
                    yMove, minDelay, maxDelay);
        }
        boolean isDone = false;
        while (!isDone) {
            isDone = true;
            for (int i = 0; i < particles.length; i++) {
                particles[i].simulateAge();
                if (!particles[i].hasBeenReset())
                    isDone = false;
            }
        }
    }

    private void initParticles(int minDelay, int maxDelay, int minLife) {
        for (int i = 0; i < particles.length; i++) {
            int n = spriteTags.length;
            int index = Particle.getRandomInt(0, n - 1);

            Point speed = getSpeed();
            particles[i] = new Particle(x, (x + xRange), y, (y + yRange), spriteTags[index], minLife, maxLife, speed.x,
                    speed.y, minDelay, maxDelay);
        }
    }

    private Point getSpeed() {
        int xSpeed = 0, ySpeed = 0;
        switch (Particle.rollDie(16)){
            case 1:
                xSpeed = 3;
                break;
            case 2:
                xSpeed = 3;
                ySpeed = -1;
                break;
            case 3:
                xSpeed = 2;
                ySpeed = -2;
                break;
            case 4:
                xSpeed = 1;
                ySpeed = -3;
                break;
            case 5:
                ySpeed = -3;
                break;
            case 6:
                xSpeed = -1;
                ySpeed = -3;
                break;
            case 7:
                xSpeed = -2;
                ySpeed = -2;
                break;
            case 8:
                xSpeed = -3;
                ySpeed = -1;
                break;
            case 9:
                xSpeed = -3;
                break;
            case 10:
                xSpeed = -3;
                ySpeed = 1;
                break;
            case 11:
                xSpeed = -2;
                ySpeed = 2;
                break;
            case 12:
                xSpeed = -1;
                ySpeed = 3;
                break;
            case 13:
                ySpeed = 3;
                break;
            case 14:
                xSpeed = 1;
                ySpeed = 3;
                break;
            case 15:
                xSpeed = 2;
                ySpeed = 2;
                break;
            case 16:
                xSpeed = 3;
                ySpeed = 1;
                break;
        }

        return new Point(xSpeed, ySpeed);
    }

    public Particle[] getParticleArray() {
        return particles;
    }

    public Iterator<Frame> getParticles() {
        List<Frame> parts = new ArrayList<>();
        for (int i = 0; i < particles.length; i++) {
            Frame tmp = particles[i].getCurrentFrame();
            parts.add(tmp);
        }
        return parts.iterator();
    }

    public Iterator<Frame> getParticlesNoReset() {
        List<Frame> parts = new ArrayList<>();
        for (int i = 0; i < particles.length; i++) {
            Frame tmp = particles[i].getCurrentFrameNoReset();
            parts.add(tmp);
        }
        return parts.iterator();
    }

//    public Iterator<Sprite> getParticles(Control ctrl) {
//        List<Sprite> parts = new ArrayList<>();
//        for (int i = 0; i < particles.length; i++) {
//            Frame tmp = particles[i].getCurrentFrame();
//            Sprite temp = new Sprite(tmp.getX(), tmp.getY(),
//                    ctrl.getSpriteFromBackBuffer(tmp.getSpriteTag()).getSprite(), tmp.getSpriteTag());
//            parts.add(temp);
//        }
//        return parts.iterator();
//    }
}
