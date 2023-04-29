package Main;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Input.Mouse;
import Inventory.Inventory;
import Levels.Level1;
import Levels.Level2;
import Levels.TitleScreen;
import Particles.*;
import Puzzles.PipePuzzle;
import Puzzles.SimonSays;
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
    public static Firework firework;

    public static String s = "";
    public static String s2 = "";
    private static int[] buffer;
    private static RECT disk;
    private static RECT load;

    private static final int dropShadow = 2;
    private static Interpreter interpreter;
    public static AText aText = new AText("This is a test of text in the console box...", 20);
    public static Sound song = new Sound("persephone_farewell");
    public static Sound sfx = new Sound("funny_death");

    public static Sprite rotatedImage, scaledImage;
    public static stopWatchX timer1 = new stopWatchX(10);
    public static stopWatchX timer2 = new stopWatchX(100);
    public static double rotate = 0.0;
    public static double scale = 1.0;
    public static boolean isScaleUp = true;

    public static TitleScreen titleScreen;
    public static Level1 level1;
    public static Level2 level2;

    public static Sprite nextLevel, prevLevel;
    public static RECT nextLevelRect, prevLevelRect;

    public static Inventory inventory;


    public static void main(String[] args) {
        updateArtScript();
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
        inventory = new Inventory(ctrl);

        titleScreen = new TitleScreen(ctrl);
        titleScreen.setLevelActive(true);
        level1 = new Level1(ctrl, inventory);
        level2 = new Level2(ctrl, inventory);
//        level2.setLevelActive(true);


        rain = new Rain(-50, 0, 1200, 90, 25, 60, 150);
        smoke = new Smoke(500, 500, 25, 10, 10, 275, 500, true);
        snow = new Snow(-50, 0, 1350, 90, 50, 250, 150);
        firework = new Firework(200, 1000, 1720, 100, 25, 75, 5);

//        ctrl.hideDefaultCursor();

        nextLevel = new Sprite(1700, 800, ctrl.getSpriteFromBackBuffer("nextLevel").getSprite(), "nextLevel");
        nextLevelRect = new RECT(1700, 800, 1828, 928, "next Level", new Frame(1700, 800, "nextLevelHover"));
        prevLevel = new Sprite(1700, 928, ctrl.getSpriteFromBackBuffer("prevLevel").getSprite(), "prevLevel");
        prevLevelRect = new RECT(1700, 928, 1828, 1056, "previous Level", new Frame(1700, 928, "prevLevelHover"));

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
        disk = new RECT(50, 50, 114, 114, "savetag", "Save Game",
                new Frame(100, 50, "saveIcon2Hover"));
        load = new RECT(200, 50, 264, 114, "loadtag", "Load Game",
                new Frame(200, 50, "loadIconHover"));
        buffer = new int[40];
        for (int i = 0; i < buffer.length; i++) {
            int value = (int) (Math.random() * 100);
            buffer[i] = value;
        }
//
//        interpreter = new Interpreter(ctrl, commands);
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        Point p = Mouse.getMouseCoords();
        int x = (int) p.getX();
        int y = (int) p.getY();

        if (!titleScreen.isLevelActive() && !level1.isPuzzleActive() && !level2.isPuzzleActive()/*
        && !level3.isPuzzleActive && !finishScreen.isLevelActive()*/) {
            inventory.drawInventory();

//            if (!level3.isLevelActive()) {
            ctrl.addSpriteToHudBuffer(nextLevel);
            if (nextLevelRect.isCollision(x, y))
                ctrl.addSpriteToHudBuffer(nextLevelRect.getGraphicalHover());
//        }
            if (!level1.isLevelActive()) {
                ctrl.addSpriteToHudBuffer(prevLevel);
                if (prevLevelRect.isCollision(x, y))
                    ctrl.addSpriteToHudBuffer(prevLevelRect.getGraphicalHover());
            }
        }
        if (titleScreen.isLevelActive()) {
            titleScreen.runLevel();
            if (titleScreen.isStartClicked()) {
                titleScreen.setLevelActive(false);
                level1.setLevelActive(true);
            }
        } else if (level1.isLevelActive()) {
            level1.runLevel();
            if (level1.isNextLevel()) {
                level1.setLevelActive(false);
                level2.setLevelActive(true);
                level1.setNextLevel(false);
            }

            if (Control.getMouseInput() != null) {
                if (nextLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && level1.isComplete()) {
                    level1.setLevelActive(false);
                    level2.setLevelActive(true);
                    level1.setNextLevel(false);
                }
            }
        } else if (level2.isLevelActive()) {
            level2.runLevel();
            if (level2.isNextLevel()) {
                level2.setLevelActive(false);
//                level3.setLevelActive(true);
                level2.setNextLevel(false);
            }

            if (Control.getMouseInput() != null) {
                if (nextLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && level2.isComplete()) {
                    level2.setLevelActive(false);
//                    level3.setLevelActive(true);
                    level2.setNextLevel(false);
                }
                if (prevLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    level2.setLevelActive(false);
                    level1.setLevelActive(true);
                }
            }
        }
//        else if (level3.isLevelActive()) {
//            level3.runLevel();
//            if (level3.isNextLevel()){
//                level3.setLevelActive(false);
//                finishScreen.setLevelActive(true);
//            }

//           if (Control.getMouseInput() != null) {
//                if (prevLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
//                    level3.setLevelActive(false);
//                    level2.setLevelActive(true);
//                }
//            }
//        }
//        else if (finishScreen.isLevelActive()) {
//            finishScreen.runLevel();
//            if (finishScreen.restartClicked()){
//                  finishScreen.setLevelActive(false);
//                  inventory = new Inventory(ctrl);
//                  titleScreen = new TitleScreen(ctrl);
//                  titleScreen.setLevelActive(true);
//                  level1 = new Level1(ctrl, inventory);
//                  level2 = new Level2(ctrl, inventory);
//                  level3 = new Level3(ctrl, inventory);
//                  finishScreen = new FinishScreen(ctrl);
//            }
//        }


//        for (int i = 0; i < 5; i++) {
//            int x1 = (i << 7) + 800;
//            ctrl.addSpriteToHudBuffer(new Sprite(x1, 900, ctrl.getSpriteFromBackBuffer("inventorySlot").getSprite(), "test"));
//        }

//        ctrl.addSpriteToFrontBuffer(0, 0, "forest");

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
        //UpdateParticles rainParticles = new UpdateParticles(ctrl, true, rain.getParticleSystem());
        //UpdateParticles smokeParticles = new UpdateParticles(ctrl, true, smoke.getParticleSystem());
//        UpdateParticles snowParticles = new UpdateParticles(ctrl, true, snow.getParticleSystem());

        // TODO: 4/26/2023 Firework Particles
//        UpdateParticles fireworkParticles = new UpdateParticles(ctrl, true, firework.getParticleSystem());
//        for (int i = 0; i < firework.explosions.length; i++)
//            if (firework.explosions[i] != null)
//                new UpdateParticles(ctrl, false, firework.explosions[i].getParticleSystem());


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


        if (!titleScreen.isLevelActive()) {
            ctrl.addSpriteToFrontBuffer(50, 50, "saveIcon2");

            if (disk.isCollision(x, y)) {
                s = disk.getHoverLabel();
                ctrl.addSpriteToFrontBuffer(disk.getX1(), disk.getY1(), disk.getGraphicalHover().getSpriteTag());
            } else
                s = "";

            ctrl.drawString(x, (y - 2), s, Color.BLACK);
            ctrl.drawString(x - dropShadow, (y - dropShadow) - 2, s, Color.yellow);
            if (Control.getMouseInput() != null)
                if (disk.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    saveData();
                    s2 = "Game Saved";
                } else
                    s2 = "";
        } else {
            ctrl.addSpriteToFrontBuffer(200, 50, "loadIcon");

            if (load.isCollision(x, y)) {
                s = load.getHoverLabel();
                ctrl.addSpriteToFrontBuffer(load.getX1(), load.getY1(), load.getGraphicalHover().getSpriteTag());
            } else
                s = "";

            ctrl.drawString(x, (y - 2), s, Color.BLACK);
            ctrl.drawString(x - dropShadow, (y - dropShadow) - 2, s, Color.yellow);
            if (Control.getMouseInput() != null)
                if (load.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    loadData();
                    s2 = "Game Loaded";
                } else
                    s2 = "";
        }
        ctrl.drawString(0, 200, s2, Color.WHITE);
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
