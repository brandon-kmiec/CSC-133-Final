// Brandon Kmiec

package Main;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Input.Mouse;
import Inventory.Inventory;
import Levels.*;
import Sound.Sound;
import logic.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class Main {
    // Fields (Static) below...
    private static String s2 = "";
    private static RECT disk;
    private static RECT load;
    private static final int dropShadow = 2;
    private static TitleScreen titleScreen;
    private static Level1 level1;
    private static Level2 level2;
    private static Level3 level3;
    private static FinishScreen finishScreen;
    private static Sprite nextLevel, prevLevel;
    private static RECT nextLevelRect, prevLevelRect;
    private static Inventory inventory;
    private static Instant start;
    private static boolean stopTime = false;
    private static Sound backgroundMusic;
    private static Duration timeElapsed;


    public static void main(String[] args) {
        updateArtScript();
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
        start = Instant.now();

        inventory = new Inventory(ctrl);

        titleScreen = new TitleScreen(ctrl);
        titleScreen.setLevelActive(true);
        level1 = new Level1(ctrl, inventory);
        level2 = new Level2(ctrl, inventory);
        level3 = new Level3(ctrl, inventory);
        finishScreen = new FinishScreen(ctrl);

        backgroundMusic = new Sound("backgroundMusic");
        backgroundMusic.setLoop();

        ctrl.hideDefaultCursor();

        nextLevel = new Sprite(1700, 800, ctrl.getSpriteFromBackBuffer("nextLevel").getSprite(), "nextLevel");
        nextLevelRect = new RECT(1700, 800, 1828, 928, "next Level", "Next Level",
                new Frame(1700, 800, "nextLevelHover"));
        prevLevel = new Sprite(1700, 928, ctrl.getSpriteFromBackBuffer("prevLevel").getSprite(), "prevLevel");
        prevLevelRect = new RECT(1700, 928, 1828, 1056, "previous Level", "Previous Level",
                new Frame(1700, 928, "prevLevelHover"));

        disk = new RECT(50, 50, 114, 114, "savetag", "Save Game",
                new Frame(100, 50, "saveIcon2Hover"));
        load = new RECT(200, 50, 264, 114, "loadtag", "Load Game",
                new Frame(200, 50, "loadIconHover"));
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        Point p = Mouse.getMouseCoords();
        int x = (int) p.getX();
        int y = (int) p.getY();

        drawQuit(ctrl, p);

        if (!titleScreen.isLevelActive() && !level1.isPuzzleActive() && !level2.isPuzzleActive() && !finishScreen.isLevelActive()) {
            inventory.drawInventory();

            ctrl.addSpriteToHudBuffer(nextLevel);
            if (nextLevelRect.isCollision(x, y)) {
                ctrl.addSpriteToHudBuffer(nextLevelRect.getGraphicalHover());
                ctrl.drawHudString(x, (y - 2), nextLevelRect.getHoverLabel(), Color.BLACK);
                ctrl.drawHudString(x - dropShadow, (y - dropShadow) - 2, nextLevelRect.getHoverLabel(), Color.yellow);
            }

            if (!level1.isLevelActive()) {
                ctrl.addSpriteToHudBuffer(prevLevel);
                if (prevLevelRect.isCollision(x, y)) {
                    ctrl.addSpriteToHudBuffer(prevLevelRect.getGraphicalHover());
                    ctrl.drawHudString(x, (y - 2), prevLevelRect.getHoverLabel(), Color.BLACK);
                    ctrl.drawHudString(x - dropShadow, (y - dropShadow) - 2, prevLevelRect.getHoverLabel(), Color.yellow);
                }
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
                level3.setLevelActive(true);
                level2.setNextLevel(false);
            }

            if (Control.getMouseInput() != null) {
                if (nextLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && level2.isComplete()) {
                    level2.setLevelActive(false);
                    level3.setLevelActive(true);
                    level2.setNextLevel(false);
                }
                if (prevLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    level2.setLevelActive(false);
                    level1.setLevelActive(true);
                }
            }
        } else if (level3.isLevelActive()) {
            level3.runLevel();
            if (level3.isNextLevel()) {
                level3.setLevelActive(false);
                finishScreen.setLevelActive(true);
            }

            if (Control.getMouseInput() != null) {
                if (nextLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && level3.isComplete()) {
                    level3.setLevelActive(false);
                    finishScreen.setLevelActive(true);
                    level3.setNextLevel(false);
                }
                if (prevLevelRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    level3.setLevelActive(false);
                    level2.setLevelActive(true);
                }
            }
        } else if (finishScreen.isLevelActive()) {
            finishScreen.runLevel();
            backgroundMusic.pauseWAV();
            if (!stopTime) {
                Instant stop = Instant.now();
                Duration timeDifference = Duration.between(start, stop);
                stopTime = true;
                finishScreen.setCompleteTime(timeDifference, timeElapsed);
            }

            if (finishScreen.isRestartClicked()) {
                timeElapsed = null;
                finishScreen.setLevelActive(false);
                inventory = new Inventory(ctrl);
                titleScreen = new TitleScreen(ctrl);
                titleScreen.setLevelActive(true);
                level1 = new Level1(ctrl, inventory);
                level2 = new Level2(ctrl, inventory);
                level3 = new Level3(ctrl, inventory);
                finishScreen = new FinishScreen(ctrl);
                backgroundMusic.restartWAV();
            }
        }

        String s;
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

        if (artList != null)
            for (File file : artList)
                updateArt.writeLine("Art/" + file.getName() + "*" + file.getName().substring(0, file.getName().indexOf('.')));
        updateArt.saveFile();
    }

    public static void saveData() {
        Instant current = Instant.now();

        if (timeElapsed != null)
            timeElapsed = timeElapsed.plus(Duration.between(start, current));
        else
            timeElapsed = Duration.between(start, current);

        EZFileWrite ezw = new EZFileWrite("save.txt");
        ezw.writeLine(String.valueOf(timeElapsed));
        ezw.writeLine(level1.saveData());
        ezw.writeLine(level2.saveData());
        ezw.writeLine(level3.saveData());
        ezw.writeLine(finishScreen.saveData());
        ezw.saveFile();
    }

    public static void loadData() {
        EZFileRead ezr = new EZFileRead("save.txt");
        if (ezr.getNumLines() > 0) {
            String raw = ezr.getLine(0);
            timeElapsed = Duration.parse(raw);
            raw = ezr.getLine(1);
            level1.loadData(raw);
            raw = ezr.getLine(2);
            level2.loadData(raw);
            raw = ezr.getLine(3);
            level3.loadData(raw);
            raw = ezr.getLine(4);
            finishScreen.loadData(raw);

            titleScreen.setLevelActive(false);
        }
    }

    public static void drawQuit(Control ctrl, Point p) {
        BufferedImage bufferedImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        Font font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 100);
        g.setFont(font);
        Color c = new Color(76, 44, 182);
        g.setColor(c);
        RECT quitRect;
        if (titleScreen.isLevelActive() || finishScreen.isLevelActive()) {
            g.setColor(c);
            quitRect = new RECT(867, 958, 977, 1015, "quit");
            g.fillRect(quitRect.getX1(), quitRect.getY1(), 110, 57);
            g.setColor(Color.BLACK);
            g.drawString("Quit", gameString.getCenteredXPosition(g, font, "Quit", 0, 1850, 1), 1000);
        } else {
            g.setColor(c);
            quitRect = new RECT(200, 57, 304, 115, "quit");
            g.fillRect(quitRect.getX1(), quitRect.getY1(), 110, 57);
            g.setColor(Color.BLACK);
            g.drawString("Quit", 200, 100);
        }

        if (quitRect.isCollision(p.x, p.y)) {
            g.setColor(new Color(0, 0, 0, 80));
            g.fillRect(quitRect.getX1(), quitRect.getY1(), 110, 57);
        }
        if (Control.getMouseInput() != null)
            if (quitRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON))
                System.exit(0);

        g.dispose();
        Sprite sprite = new Sprite(0, 0, bufferedImage, "exitButton");
        ctrl.addSpriteToHudBuffer(sprite);
    }
}
