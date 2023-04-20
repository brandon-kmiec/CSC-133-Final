package Particles;

public class Snow {
    // Fields
    private ParticleSystem parts;
    private String[] spriteTags;
    private static final int swayRange = Particle.rollDie(25) + 25;

    // Constructor
    public Snow(int xpos, int ypos, int xRange, int yRange, int minLife, int maxLife, int numParticles) {
        spriteTags = new String[5];
        spriteTags[0] = "snow1";
        spriteTags[1] = "snow2";
        spriteTags[2] = "snow3";
        spriteTags[3] = "snow4";
        spriteTags[4] = "snow5";

        int xSpeed;
        int ySpeed = 3;

        if (Particle.rollDie(100) >= 50)
            xSpeed = 3;
        else
            xSpeed = -3;

        parts = new ParticleSystem(numParticles, xpos, ypos, xRange, yRange, minLife, maxLife, xSpeed, ySpeed,
                32, 80, spriteTags);
    }

    // Methods
    private void updateParticleSprites() {
        Particle[] pa = parts.getParticleArray();
        for (Particle particle : pa) {
            int stages = spriteTags.length;
            int life = particle.getLifeCycle();
            int range = life / stages;
            int age = particle.getAge();

            int leftBound = particle.getRootX() - swayRange;
            int rightBound = particle.getRootX() + swayRange;

            if (particle.getX() > rightBound)
                particle.changeXMove(-3);
            else if (particle.getX() < leftBound)
                particle.changeXMove(3);

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
