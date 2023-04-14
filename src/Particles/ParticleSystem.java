package Particles;

import Data.Frame;

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
