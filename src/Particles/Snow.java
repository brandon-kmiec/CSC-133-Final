package Particles;

public class Snow {
    // Fields
    private ParticleSystem parts;
    private String[] spriteTags;
    private static final int swayRange = 25;
    private int leftBound, rightBound;

    // Constructor
    public Snow(int xpos, int ypos, int xRange, int yRange, int minLife, int maxLife, int numParticles) {
        spriteTags = new String[5];
        spriteTags[0] = "snow1";
        spriteTags[1] = "snow2";
        spriteTags[2] = "snow3";
        spriteTags[3] = "snow4";
        spriteTags[4] = "snow5";

        int xSpeed;
        int ySpeed = 5;

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
        for (int i = 0; i < pa.length; i++) {
            int stages = spriteTags.length;
            int life = pa[i].getLifeCycle();
            int range = life / stages;
            int age = pa[i].getAge();

            leftBound = pa[i].getRootX() - swayRange;
            rightBound = pa[i].getRootX() + swayRange;

            if (pa[i].getX() > rightBound)
                pa[i].changeXMove(-5);
            else if (pa[i].getX() < leftBound)
                pa[i].changeXMove(5);

            for (int j = 0; j < stages; j++) {
                if (age >= (range * j) && age < (range * (j + 1))) {
                    pa[i].changeSprite(spriteTags[j]);
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
