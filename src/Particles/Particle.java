package Particles;

import Data.Frame;
import timer.stopWatchX;

public class Particle {
    // Fields
    private int x, y;
    private String particleSpriteTag;
    private int lifeCycle;
    private int age;
    private int xMove, yMove;
    private stopWatchX timer;
    private int rootX, rootY;
    private boolean isReset;

    //Constructor
    public Particle(int minX, int maxX, int minY, int maxY, String particleSpriteTag, int minLife, int maxLife,
                    int xMove, int yMove, int minDelay, int maxDelay) {
        this.particleSpriteTag = particleSpriteTag;
        this.x = getRandomInt(minX, maxX);
        this.y = getRandomInt(minY, maxY);
        lifeCycle = getRandomInt(minLife, maxLife);
        this.xMove = xMove;
        this.yMove = yMove;
        int delay = getRandomInt(minDelay, maxDelay);
        timer = new stopWatchX(delay);
        rootX = x;
        rootY = y;
    }

    // Methods
    public boolean hasBeenReset() {
        return isReset;
    }

    public void changeX(int newX) {
        x = newX;
    }

    public void changeY(int newY) {
        y = newY;
    }

    public void changeXMove(int newXMove) {
        xMove = newXMove;
    }

    public void changeYMove(int newYMove) {
        yMove = newYMove;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLifeCycle() {
        return lifeCycle;
    }

    public int getAge() {
        return age;
    }

    public void changeSprite(String newSprite) {
        particleSpriteTag = newSprite;
    }

    public boolean isParticleDead() {
        if (age >= lifeCycle) return true;
        if (y > 1080 || x > 1919) return true;
        return false;
    }

    public void simulateAge() {
        age++;
        y += yMove;
        x += xMove;
        if (isParticleDead()) {
            x = rootX;
            y = rootY;
            age = 0;
            isReset = true;
        }
    }

    public Frame getCurrentFrame() {
        if (timer.isTimeUp()) {
            age++;
            x += xMove;
            y += yMove;
            if (isParticleDead()) {
                x = rootX;
                y = rootY;
                age = 0;
                isReset = true;
            }
            timer.resetWatch();
        }
        return new Frame(x, y, particleSpriteTag);
    }

    public static int getRandomInt(int first, int last) {
        int diff = last - first;
        double num = Math.random() * diff;
        int intNum = (int) num;
        return first + intNum;
    }

    public static int rollDie(int dieSides) {
        double result = Math.random() * dieSides;
        int res = (int) result;
        res++;
        return res;
    }
}
