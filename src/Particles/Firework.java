package Particles;

import Sound.Sound;

public class Firework {
    // Fields
    private final ParticleSystem parts;
    private final int xPos;
    private final int xRange;
    private final Sound explosion;
    public FireworkExplosion[] explosions;

    // Constructor
    public Firework(int xPos, int yPos, int xRange, int yRange, int minLife, int maxLife, int numParticles) {
        this.xPos = xPos;
        this.xRange = xRange;

        String[] spriteTags = new String[1];
        spriteTags[0] = "fireworkRocket";

        int xSpeed = 0;
        int ySpeed = -10;

        parts = new ParticleSystem(numParticles, xPos, yPos, xRange, yRange, minLife, maxLife, xSpeed, ySpeed,
                32, 80, spriteTags);

        explosion = new Sound("fireworkExplosion");

        explosions = new FireworkExplosion[parts.getParticleArray().length];
    }

    // Methods
    private void updateParticles() {
        Particle[] pa = parts.getParticleArray();

        for (int i = 0; i < pa.length; i++) {
            Particle particle = pa[i];
            int life = particle.getLifeCycle();
            int age = particle.getAge();

            if (age + 1 >= life) {
                explosion.restartWAV();
                explosions[i] = new FireworkExplosion(particle.getX(), particle.getY(), 16, 48, 150);
                particle.changeRootX(Particle.getRandomInt(xPos, xRange));
            }

        }
    }

    public ParticleSystem getParticleSystem() {
        updateParticles();
        return parts;
    }
}
