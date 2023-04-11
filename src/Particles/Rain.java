package Particles;

public class Rain {
    // Fields
    private ParticleSystem parts;
    private String[] spriteTags;

    // Constructor
    public Rain(int xpos, int ypos, int xRange, int yRange, int minLife, int maxLife, int numParticles) {
        spriteTags = new String[5];
        spriteTags[0] = "raindrop1";
        spriteTags[1] = "raindrop2";
        spriteTags[2] = "raindrop3";
        spriteTags[3] = "raindrop4";
        spriteTags[4] = "raindrop5";
        int xSpeed = 6;
        int ySpeed = 16;
        parts = new ParticleSystem(numParticles, xpos, ypos, xRange, yRange, minLife, maxLife, xSpeed, ySpeed,
                16, 18, spriteTags);
    }

    // Methods
    private void updateParticleSprites(){
        Particle[] pa = parts.getParticleArray();
        for (int i = 0; i < pa.length; i++) {
            int stages = spriteTags.length;
            int life = pa[i].getLifeCycle();
            int range = life / stages;
            int age = pa[i].getAge();
            for (int j = 0; j < stages; j++) {
                if (age >= (range * j) && age < (range * (j + 1))){
                    pa[i].changeSprite(spriteTags[j]);
                    break;
                }
            }
        }
    }

    public ParticleSystem getParticleSystem(){
        updateParticleSprites();
        return parts;
    }
}
