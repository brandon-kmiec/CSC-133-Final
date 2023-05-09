package Levels;

import Data.*;
import FileIO.EZFileRead;
import Input.Mouse;
import ScriptingEngine.Interpreter;
import logic.Control;
import Graphics.Graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TitleScreen {
    // Fields
    private final Control ctrl;
    private boolean levelActive;
    private boolean startClicked;
    private boolean leaderboardClicked;
    private final RECT startRect;
    private final RECT leaderboardRect;
    private String hoverText;
    private final Sprite mouseCursor;
    private final ArrayList<Command> commands;
    private final Interpreter interpreter;

    // Constructor
    public TitleScreen(Control ctrl) {
        this.ctrl = ctrl;
        levelActive = false;

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        startRect = new RECT(131, 681, 876, 839, "start");
        leaderboardRect = new RECT(968, 682, 1844, 841, "leaderboard");

        hoverText = "";

        commands = new ArrayList<>();
        interpreter = new Interpreter(ctrl, commands);
        readCommands();
    }

    // Methods
    public void setLevelActive(boolean isActive) {
        this.levelActive = isActive;
    }

    public boolean isLevelActive() {
        return levelActive;
    }

    public boolean isStartClicked() {
        return startClicked;
    }

    public void runLevel() {
        Point p = Mouse.getMouseCoords();


//        ctrl.addSpriteToFrontBuffer(0, 0, "titleScreen");
        interpreter.checkCommand(commands.get(0));

        ctrl.drawString(0, 1070, "Brandon Kmiec", Color.WHITE);

        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

        if (!leaderboardClicked) {
            if (startRect.isCollision(p.x, p.y))
                hoverText = "Click to Start";
            else if (leaderboardRect.isCollision(p.x, p.y))
                hoverText = "Click to view Leaderboard";
            else
                hoverText = "";

            ctrl.drawString(p.x, (p.y - 2), hoverText, Color.BLACK);
            ctrl.drawString((p.x - 2), (p.y - 2) - 2, hoverText, Color.YELLOW);

            if (Control.getMouseInput() != null) {
                if (startRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    startClicked = true;
                }
                if (leaderboardRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    leaderboardClicked = true;
                }
            }
        } else
            displayLeaderboard();
    }

    private void displayLeaderboard() {
        Point p = Mouse.getMouseCoords();

        BufferedImage leaderboard = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics g = leaderboard.getGraphics();
        Font font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 200);
        g.setFont(font);

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, 1920, 1080);

        g.setColor(Color.WHITE);

        g.drawString("Leaderboard", gameString.getCenteredXPosition(g, font, "Leaderboard", 0, 1919, 1), 100);

        font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 100);
        g.setFont(font);

        // TODO: 5/1/2023 implement ability to read and store three letter player name with completed time
        EZFileRead ezr = new EZFileRead("leaderboard.txt");
        int size = ezr.getNumLines();
        if (size > 10)
            size = 10;

        for (int i = 0; i < size; i++) {
            int y = (i << 6) + 200;

            int time = Integer.parseInt(ezr.getLine(i));
            long milliseconds = time % 1000;
            long seconds = time / 1000 % 60;
            long minutes = time / (60 * 1000) % 60;
            long hours = time / (60 * 60 * 1000) % 24;

            // TODO: 5/8/2023 Implement a way to add a name with the time on the leaderboard
            String temp = hours + ":" + minutes + ":" + seconds + "." + milliseconds;

            g.drawString(temp, gameString.getCenteredXPosition(g, font, temp, 0, 1919, 1), y);
            ezr.getNextLine();
        }

        font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 50);
        g.setFont(font);
        g.drawString("Exit Leaderboard", 1600, 50);

        RECT exitRect = new RECT(1600, 26, 1814, 50, "exitRect");
        g.setColor(new Color(0, 0, 0, 200));

        if (exitRect.isCollision(p.x, p.y))
            g.fillRect(1600, 26, 214, 25);

        if (Control.getMouseInput() != null)
            if (exitRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON))
                leaderboardClicked = false;

        g.dispose();

        Sprite sprite = new Sprite(0, 0, leaderboard, "leaderboard");
        ctrl.addSpriteToFrontBuffer(sprite);
    }

    private void readCommands() {
        EZFileRead ezr = new EZFileRead("titleScreenScript.txt");
        for (int i = 0; i < ezr.getNumLines(); i++) {
            String raw = ezr.getLine(i);
            raw = raw.trim();
            if (!raw.equals("")) {
                boolean b = raw.charAt(0) == '#';
                if (!b)
                    commands.add(new Command(raw));
            }
        }
    }
}
