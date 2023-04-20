package Main;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Input.Mouse;
import Particles.*;
import ScriptingEngine.Interpreter;
import Sound.Sound;
import logic.Control;
import Graphics.Sprites;
import timer.stopWatchX;
import Graphics.Graphic;

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
    public static Snow snow;
    //    public static String s = "";
//    public static String s2 = "";
    private static int[] buffer;
    //    private static RECT disk;
//    private static final int dropShadow = 2;
//    private static Interpreter interpreter;
    public static AText aText = new AText("This is a test of text in the console box...", 20);
    public static Sound song = new Sound("persephone_farewell");
    public static Sound sfx = new Sound("funny_death");

    public static Sprite rotatedImage, scaledImage;
    public static stopWatchX timer1 = new stopWatchX(10);
    public static stopWatchX timer2 = new stopWatchX(100);
    public static double rotate = 0.0;
    public static double scale = 1.0;
    public static boolean isScaleUp = true;


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
        snow = new Snow(-50, 0, 1350, 90, 50, 250, 150);

        //ctrl.hideDefaultCursor();

        // TODO: 4/19/2023 Affine Transform
//        BufferedImage wheel = ctrl.getSpriteFromBackBuffer("wheel").getSprite();
//        rotatedImage = new Sprite(100, 100, wheel, "rWheel");
//        scaledImage = new Sprite(500, 100, wheel, "sWheel");


        // TODO: 4/19/2023 sound
//        song.setLoop();


        // TODO: 4/19/2023 interpreter and save
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


        //ctrl.addSpriteToFrontBuffer(0, 0, "forest");

        // TODO: 4/19/2023 Affine Transform
//        if (timer1.isTimeUp()) {
//            rotate += 1.0;
//            if (rotate > 360.0)
//                rotate = 0.0;
//            BufferedImage newRotate = Graphic.rotateImageByDegrees(ctrl.getSpriteFromBackBuffer("wheel").getSprite(), rotate);
//            rotatedImage = new Sprite(100, 100, newRotate, "rWheel");
//            timer1.resetWatch();
//        }
//        if (timer2.isTimeUp()) {
//            if (isScaleUp) {
//                scale += 0.05;
//                if (scale >= 1.25) {
//                    scale = 1.25;
//                    isScaleUp = !isScaleUp;
//                }
//            } else {
//                scale -= 0.05;
//                if (scale < 0.75) {
//                    scale = 0.75;
//                    isScaleUp = !isScaleUp;
//                }
//            }
//            BufferedImage newScale = Graphic.scale(ctrl.getSpriteFromBackBuffer("wheel").getSprite(), scale);
//            scaledImage = new Sprite(500, 100, newScale, "sWheel");
//            timer2.resetWatch();
//        }
//        ctrl.addSpriteToFrontBuffer(rotatedImage);
//        ctrl.addSpriteToFrontBuffer(scaledImage);
//        ctrl.drawString(260, 25, "Affine Transforms", Color.white);
//        ctrl.drawString(125, 235, "Rotate", Color.white);
//        ctrl.drawString(525, 235, "Scale", Color.white);


        // TODO: 4/19/2023 particles
        //UpdateParticles rainParticles = new UpdateParticles(ctrl, rain.getParticleSystem());
        //UpdateParticles smokeParticles = new UpdateParticles(ctrl, smoke.getParticleSystem());
        //UpdateParticles snowParticles = new UpdateParticles(ctrl, snow.getParticleSystem());


        // TODO: 4/19/2023 HUD
//        ctrl.drawString(150, 300, "Text Underneath", Color.red);
//        ctrl.addSpriteToHudBuffer(200, 200, "my_hud");
//        ctrl.drawHudString(220, 270, "HUD data here...", Color.white);


        // TODO: 4/19/2023 animated text
//        ctrl.addSpriteToFrontBuffer(0, 0, "gui_bg");
//
//        String s = aText.getCurrentStr();
//        ctrl.drawString(20, 480, s, Color.black);
//
//        Point p = Mouse.getMouseCoords();
//        ctrl.addSpriteToOverlayBuffer(p.x, p.y, "cursor");

        // TODO: 4/19/2023 interpreter and save
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
