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
        for (int i = 0; i < pa.length; i++) {
            int stages = spriteTags.length;
            int life = pa[i].getLifeCycle();
            int range = life / stages;
            int age = pa[i].getAge();

            if (changeDirection) {
                if (pa[i].getY() > 400) {
                    pa[i].changeXMove(0);
                    pa[i].changeYMove(-3);
                } else if (pa[i].getY() > 300 && pa[i].getY() <= 400) {
                    pa[i].changeXMove(1);
                    pa[i].changeYMove(-3);
                } else if (pa[i].getY() > 200 && pa[i].getY() <= 300) {
                    pa[i].changeXMove(2);
                    pa[i].changeYMove(-2);
                } else if (pa[i].getY() > 100 && pa[i].getY() <= 200) {
                    pa[i].changeXMove(3);
                    pa[i].changeYMove(-2);
                } else if (pa[i].getY() > 0 && pa[i].getY() <= 100) {
                    pa[i].changeXMove(3);
                    pa[i].changeYMove(-1);
                }
            } else {
                if (pa[i].getY() > 400) {
                    pa[i].changeXMove(0);
                    pa[i].changeYMove(-3);
                } else if (pa[i].getY() > 300 && pa[i].getY() <= 400) {
                    pa[i].changeXMove(-1);
                    pa[i].changeYMove(-3);
                } else if (pa[i].getY() > 200 && pa[i].getY() <= 300) {
                    pa[i].changeXMove(-2);
                    pa[i].changeYMove(-2);
                } else if (pa[i].getY() > 100 && pa[i].getY() <= 200) {
                    pa[i].changeXMove(-3);
                    pa[i].changeYMove(-2);
                } else if (pa[i].getY() > 0 && pa[i].getY() <= 100) {
                    pa[i].changeXMove(-3);
                    pa[i].changeYMove(-1);
                }
            }

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
