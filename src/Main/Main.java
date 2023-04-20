package Main;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Particles.*;
import logic.Control;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
    // Fields (Static) below...
    public static Rain rain;
    public static Smoke smoke;
    public static Snow snow;
    private static int[] buffer;
    public static Sprite s;


    public static void main(String[] args) {
        updateArtScript();
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)

        rain = new Rain(-50, 0, 1200, 90, 25, 60, 150);
        smoke = new Smoke(500, 500, 25, 10, 10, 275, 500, true);
        snow = new Snow(-50, 0, 1350, 90, 50, 175, 150);

    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        ctrl.addSpriteToFrontBuffer(0, 0, "forest");

        ParticleSystem rainParticleSystem = rain.getParticleSystem();
        ParticleSystem smokeParticleSystem = smoke.getParticleSystem();
        ParticleSystem snowParticleSystem = snow.getParticleSystem();

        Iterator<Frame> rainParticleSystemParticles = rainParticleSystem.getParticles();
        Iterator<Frame> smokeParticleSystemParticles = smokeParticleSystem.getParticles();
        Iterator<Frame> snowParticleSystemParticles = snowParticleSystem.getParticles();

        while (snowParticleSystemParticles.hasNext()/* && rainParticleSystemParticles.hasNext() && smokeParticleSystemParticles.hasNext()*/) {
            Frame rainPar = rainParticleSystemParticles.next();
            Frame smokePar = smokeParticleSystemParticles.next();
            Frame snowPar = snowParticleSystemParticles.next();
            //ctrl.addSpriteToFrontBuffer(rainPar);
            //ctrl.addSpriteToFrontBuffer(smokePar);
            ctrl.addSpriteToFrontBuffer(snowPar);
        }

    }

    // Additional Static methods below...(if needed)
    public static void updateArtScript() {
        File artDirectory = new File("Art");
        File[] artList = artDirectory.listFiles();
        EZFileWrite updateArt = new EZFileWrite("Art.txt");

        if (artList != null) {
            for (File file : artList) {
                //System.out.println(file.getName());
                updateArt.writeLine("Art/" + file.getName() + "*" + file.getName().substring(0, file.getName().indexOf('.')));
                //System.out.println("Art/" + file.getName() + "*" + file.getName().substring(0, file.getName().indexOf('.')));
            }
        }
        updateArt.saveFile();
    }

    public static void saveData() {
        String out = "";
        for (int i = 0; i < buffer.length; i++)
            out += buffer[i] + "*";
        out = out.substring(0, out.length() - 1);
        EZFileWrite ezw = new EZFileWrite("save.txt");
        ezw.writeLine(out);
        ezw.saveFile();
    }

    public static void loadData() {
        EZFileRead ezr = new EZFileRead("save.txt");
        String raw = ezr.getLine(0);
        StringTokenizer st = new StringTokenizer(raw, "*");
        if (st.countTokens() != buffer.length)
            return;
        for (int i = 0; i < buffer.length; i++) {
            String value = st.nextToken();
            int val = Integer.parseInt(value);
            buffer[i] = val;
        }
    }
}
