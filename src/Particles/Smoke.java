package Particles;

public class Smoke {
    // Fields
    private ParticleSystem parts;
    private String[] spriteTags;
    private boolean changeDirection;

    // Constructor
    public Smoke(int xpos, int ypos, int xRange, int yRange, int minLife, int maxLife, int numParticles,
                 boolean changeDirection) {
        spriteTags = new String[5];
        spriteTags[0] = "smoke1";
        spriteTags[1] = "smoke2";
        spriteTags[2] = "smoke3";
        spriteTags[3] = "smoke4";
        spriteTags[4] = "smoke5";

        this.changeDirection = changeDirection;

        int xSpeed, ySpeed;

        if (changeDirection)
            xSpeed = 1;
        else
            xSpeed = -1;
        ySpeed = -3;

        parts = new ParticleSystem(numParticles, xpos, ypos, xRange, yRange, minLife, maxLife, xSpeed, ySpeed,
                16, 20, spriteTags);
    }

    // Methods
    private void updateParticleSprites() {
        Particle[] pa = parts.getParticleArray();
        for (Particle particle : pa) {
            int stages = spriteTags.length;
            int life = particle.getLifeCycle();
            int range = life / stages;
            int age = particle.getAge();

            if (changeDirection) {
                if (particle.getY() > 400) {
                    particle.changeXMove(0);
                    particle.changeYMove(-3);
                } else if (particle.getY() > 300 && particle.getY() <= 400) {
                    particle.changeXMove(1);
                    particle.changeYMove(-3);
                } else if (particle.getY() > 200 && particle.getY() <= 300) {
                    particle.changeXMove(2);
                    particle.changeYMove(-2);
                } else if (particle.getY() > 100 && particle.getY() <= 200) {
                    particle.changeXMove(3);
                    particle.changeYMove(-2);
                } else if (particle.getY() > 0 && particle.getY() <= 100) {
                    particle.changeXMove(3);
                    particle.changeYMove(-1);
                }
            } else {
                if (particle.getY() > 400) {
                    particle.changeXMove(0);
                    particle.changeYMove(-3);
                } else if (particle.getY() > 300 && particle.getY() <= 400) {
                    particle.changeXMove(-1);
                    particle.changeYMove(-3);
                } else if (particle.getY() > 200 && particle.getY() <= 300) {
                    particle.changeXMove(-2);
                    particle.changeYMove(-2);
                } else if (particle.getY() > 100 && particle.getY() <= 200) {
                    particle.changeXMove(-3);
                    particle.changeYMove(-2);
                } else if (particle.getY() > 0 && particle.getY() <= 100) {
                    particle.changeXMove(-3);
                    particle.changeYMove(-1);
                }
            }

            for (int j = 0; j < stages; j++) {
                if (age >= (range * j) && age < (range * (j + 1))) {
                    particle.changeSprite(spriteTags[j]);
                    break;
                }
            }
        }
    }

    public ParticleSystem getParticleSystem() {
        updateParticleSprites();
        return parts;
    }
}
