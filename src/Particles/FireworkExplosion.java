package Particles;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;

public class FireworkExplosion {
    // Fields
    private final ParticleSystem parts;
    private final String[] spriteTags;

    // Constructor
    public FireworkExplosion(int xPos, int yPos, int minLife, int maxLife, int numParticles) {
        spriteTags = new String[6];
        spriteTags[0] = "firework1";
        spriteTags[1] = "firework2";
        spriteTags[2] = "firework3";
        spriteTags[3] = "firework4";
        spriteTags[4] = "firework5";
        spriteTags[5] = "firework6";

        String[] tags = getTags();

        int xSpeed = 0;
        int ySpeed = 0;

        parts = new ParticleSystem(numParticles, xPos, yPos, minLife, maxLife, xSpeed, ySpeed, 32, 64,
                tags);
    }

    // Methods
    private String[] getTags() {
//        Set<ArrayList<String>> set = subset();
//        ArrayList<ArrayList<String>> subset = new ArrayList<>(set);
//
//        int randomPos = Particle.getRandomInt(0, subset.size() - 1);
//        ArrayList<String> temp = subset.get(randomPos);
//        String[] tags = new String[temp.size()];

        ArrayList<String> temp = new ArrayList<>(Arrays.asList(spriteTags));
        int size = Particle.rollDie(spriteTags.length);
        String[] tags = new String[size];
        for (int i = 0; i < size; i++) {
            int randomPos = Particle.rollDie(temp.size()) - 1;
            tags[i] = temp.get(randomPos);
            temp.remove(randomPos);
        }

//        for (int i = 0; i < temp.size(); i++)
//            tags[i] = temp.get(i);

        return tags;
    }

//    private Set<ArrayList<String>> subset() {
//        int numElem = (int) Math.pow(2, spriteTags.length);
//        ArrayList<ArrayList<String>> retList = new ArrayList<>();
//
//        for (int i = 0; i < numElem; i++) {
//            retList.add(new ArrayList<>());
//            for (int j = 0; j < spriteTags.length; j++)
//                if ((i & (1 << j)) == 0)
//                    retList.get(i).add(spriteTags[j]);
//        }
//        retList.remove(retList.size() - 1);
//
//        return new HashSet<>(retList);
//    }

    public ParticleSystem getParticleSystem() {
        return parts;
    }
}
