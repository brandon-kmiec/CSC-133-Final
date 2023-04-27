package Particles;

import logic.Control;

public class Firework {
    // Fields
    private final ParticleSystem parts;
    private final String[] spriteTags;
    private int xPos, xRange;
    public FireworkExplosion[] explosions;

    // Constructor
    public Firework(int xPos, int yPos, int xRange, int yRange, int minLife, int maxLife, int numParticles) {
        this.xPos = xPos;
        this.xRange = xRange;

        spriteTags = new String[1];
        spriteTags[0] = "fireworkRocket";

        int xSpeed = 0;
        int ySpeed = -10;

        parts = new ParticleSystem(numParticles, xPos, yPos, xRange, yRange, minLife, maxLife, xSpeed, ySpeed,
                32, 80, spriteTags);

        explosions = new FireworkExplosion[parts.getParticleArray().length];
    }

    // Methods
    private void updateParticles() {
        Particle[] pa = parts.getParticleArray();

        for (int i = 0; i < pa.length; i++) {
            Particle particle = pa[i];
            int stages = spriteTags.length;
            int life = particle.getLifeCycle();
            int range = life / stages;
            int age = particle.getAge();

            if (age + 1 >= life) {
                explosions[i] = new FireworkExplosion(particle.getX(), particle.getY(), 16, 48, 150);
                particle.changeRootX(Particle.getRandomInt(xPos, xRange));

//                fireworkExplosion = new FireworkExplosion(particle.getX(), particle.getY(), 16, 32, 150);
            }

            if (age >= life)


            for (int j = 0; j < stages; j++) {
                if (age >= (range * j) && age < (range * (j + 1))) {
                    particle.changeSprite(spriteTags[j]);
                    break;
                }
            }

//            if (particle.isParticleDead()){
//            if (fireworkExplosion != null) {
//                fireworkExplosion.getParticleSystem();
//            }
        }
    }

    public ParticleSystem getParticleSystem() {
        updateParticles();
        return parts;
    }
}
