package Main;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Input.Mouse;
import Particles.Particle;
import Particles.ParticleSystem;
import Particles.Rain;
import Particles.Smoke;
import ScriptingEngine.Interpreter;
import logic.Control;
import Graphics.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
    // Fields (Static) below...
    public static Rain rain;
    public static Smoke smoke;
//    public static String s = "";
//    public static String s2 = "";
//    private static int[] buffer;
//    private static RECT disk;
//    private static final int dropShadow = 2;
//    private static Interpreter interpreter;

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
        // TODO: 4/12/2023 try to fix smoke (right now it works better as if it was fire) (change smoke to fire??? and
        //  try to remake smoke)


//        EZFileRead ezr = new EZFileRead("script.txt");
//        ArrayList<Command> commands = new ArrayList<>();
//        for (int i = 0; i < ezr.getNumLines(); i++) {
//            String raw = ezr.getLine(i);
//            raw = raw.trim();
//            if (!raw.equals("")) {
//                boolean b = raw.charAt(0) == '#';
//                if (!b)
//                    commands.add(new Command(raw));
//            }
//        }
//
//        disk = new RECT(101, 52, 162, 112, "savetag", "Save Game");
//        buffer = new int[40];
//        for (int i = 0; i < buffer.length; i++) {
//            int value = (int) (Math.random() * 100);
//            buffer[i] = value;
//        }
//
//        interpreter = new Interpreter(ctrl, commands);
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        ctrl.addSpriteToFrontBuffer(0, 0, "forest");
        ParticleSystem rainParticleSystem = rain.getParticleSystem();
        ParticleSystem smokeParticleSystem = smoke.getParticleSystem();

        Iterator<Sprite> rainParticleSystemParticles = rainParticleSystem.getParticlesSprites(ctrl);
        Iterator<Sprite> smokeParticleSystemParticles = smokeParticleSystem.getParticlesSprites(ctrl);

        while (rainParticleSystemParticles.hasNext() && smokeParticleSystemParticles.hasNext()) {
            Sprite rainPar = rainParticleSystemParticles.next();
            Sprite smokePar = smokeParticleSystemParticles.next();
            //ctrl.addSpriteToFrontBuffer(par2.getX(), par2.getY(), par2.getSpriteTag());
            //ctrl.addSpriteToFrontBuffer(smokePar.getX(), smokePar.getY(), smokePar.getSpriteTag());
            ctrl.addSpriteToFrontBuffer(rainPar);
            ctrl.addSpriteToFrontBuffer(smokePar);
        }

//        Sprite test = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("tree").getSprite(), "tree");
//        ctrl.addSpriteToFrontBuffer(test);
//
//        Sprite test2 = new Sprite(200, 200, ctrl.getSpriteFromBackBuffer("tree").getSprite(), "tree");
//        ctrl.addSpriteToFrontBuffer(test2);


//        interpreter.checkCommands();
//
//        Point p = Mouse.getMouseCoords();
//        int x = (int) p.getX();
//        int y = (int) p.getY();
//
//        ctrl.addSpriteToFrontBuffer(100, 50, "saveicon");
//
//        if (disk.isCollision(x, y))
//            s = disk.getHoverLabel();
//        else
//            s = "";
//
//        ctrl.drawString(x, (y - 2), s, Color.BLACK);
//        ctrl.drawString(x - dropShadow, (y - dropShadow) - 2, s, Color.yellow);
//        if (Control.getMouseInput() != null)
//            if (disk.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
//                saveData();
//                s2 = "Game Saved";
//            }
//        ctrl.drawString(0, 200, s2, Color.WHITE);
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

//    public static void saveData() {
//        String out = "";
//        for (int i = 0; i < buffer.length; i++)
//            out += buffer[i] + "*";
//        out = out.substring(0, out.length() - 1);
//        EZFileWrite ezw = new EZFileWrite("save.txt");
//        ezw.writeLine(out);
//        ezw.saveFile();
//    }
//
//    public static void loadData() {
//        EZFileRead ezr = new EZFileRead("save.txt");
//        String raw = ezr.getLine(0);
//        StringTokenizer st = new StringTokenizer(raw, "*");
//        if (st.countTokens() != buffer.length)
//            return;
//        for (int i = 0; i < buffer.length; i++) {
//            String value = st.nextToken();
//            int val = Integer.parseInt(value);
//            buffer[i] = val;
//        }
//    }
}
