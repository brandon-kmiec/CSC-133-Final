package Levels;

import Data.Click;
import Data.RECT;
import Data.Sprite;
import Data.gameString;
import FileIO.EZFileRead;
import FileIO.EZFileWrite;
import Graphics.Graphic;
import Input.Mouse;
import Particles.Firework;
import Particles.UpdateParticles;
import logic.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;

public class FinishScreen {
    // Fields
    private final Control ctrl;
    private boolean levelActive;
    private boolean restartClicked;
    private boolean leaderboardClicked;
    private final RECT leaderboardRect;
    private final RECT restartRect;
    private Duration completeTime;
    private final Sprite mouseCursor;
    private static Firework firework;
    private static String temp;

    // Constructor
    public FinishScreen(Control ctrl) {
        // TODO: 5/1/2023 add a button to allow the user to save the time to a file

        this.ctrl = ctrl;
        levelActive = false;
        leaderboardClicked = false;

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        restartRect = new RECT(378, 757, 569, 804, "restart");
        leaderboardRect = new RECT(1094, 757, 1735, 804, "leaderboard");

        temp = "";
        completeTime = null;

        firework = new Firework(200, 1000, 1720, 100, 25, 75, 5);
    }

    // Methods
    public void setLevelActive(boolean isActive) {
        this.levelActive = isActive;
    }

    public void setCompleteTime(Duration time) {
        this.completeTime = time;
    }

    public boolean isLevelActive() {
        return levelActive;
    }

    public boolean isRestartClicked() {
        return restartClicked;
    }

    public void runLevel() {
        Point p = Mouse.getMouseCoords();

        if (completeTime != null) {
            long milliseconds = completeTime.toMillis() % 1000;
            long seconds = completeTime.toMillis() / 1000 % 60;
            long minutes = completeTime.toMillis() / (60 * 1000) % 60;
            long hours = completeTime.toMillis() / (60 * 60 * 1000) % 24;
            long days = completeTime.toMillis() / (24 * 60 * 60 * 1000);

            temp = hours + ":" + minutes + ":" + seconds + "." + milliseconds;

            BufferedImage finishScreen = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
            Graphics g = finishScreen.getGraphics();
            Font font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 200);
            g.setFont(font);
            g.setColor(new Color(195, 192, 210, 255));
            g.fillRect(0, 0, 1920, 1080);
            g.setColor(Color.BLACK);
            g.drawString("You Win!", gameString.getCenteredXPosition(g, font, "You Win!", 0, 1919, 1), 200);
            g.drawString("Your Time Was: ", gameString.getCenteredXPosition(g, font, "Your Time Was: ", 0, 1919, 1), 400);
            g.drawString(temp, gameString.getCenteredXPosition(g, font, temp, 0, 1919, 1), 600);

            font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 100);
            g.setFont(font);
            g.drawString("Restart", gameString.getCenteredXPosition(g, font, "Restart", 0, 960, 1), 800);
            g.drawString("Add Time to Leaderboard", gameString.getCenteredXPosition(g, font, "Add Time to Leaderboard", 960, 1920, 1), 800);

            font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 32);
            g.setFont(font);
            if (leaderboardClicked)
                g.drawString("Time added to leaderboard", gameString.getCenteredXPosition(g, font, "Time added to leaderboard", 960, 1920, 1), 750);

            g.setColor(new Color(195, 192, 210, 150));
            if (restartRect.isCollision(p.x, p.y)) {
                g.fillRect(restartRect.getX1(), restartRect.getY1(), restartRect.getX2(), restartRect.getY2());
            }
            if (leaderboardRect.isCollision(p.x, p.y)) {
                g.fillRect(leaderboardRect.getX1(), leaderboardRect.getY1(), leaderboardRect.getX2(), leaderboardRect.getY2());
            }

            g.dispose();

            Sprite sprite = new Sprite(0, 0, finishScreen, "finishScreen");
            ctrl.addSpriteToFrontBuffer(sprite);
        }


        // TODO: 5/1/2023 find a way to run a limited num of times (if counter < some number where counter++ is at the end of the if statement body)
        UpdateParticles fireworkParticles = new UpdateParticles(ctrl, true, firework.getParticleSystem());
        for (int i = 0; i < firework.explosions.length; i++)
            if (firework.explosions[i] != null)
                new UpdateParticles(ctrl, false, firework.explosions[i].getParticleSystem());


        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

        if (Control.getMouseInput() != null) {
            if (restartRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                restartClicked = true;
            }
            if (leaderboardRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                leaderboardClicked = true;

                // TODO: 5/1/2023 implement ability to read and store three letter player name with completed time
                EZFileRead ezr = new EZFileRead("leaderboard.txt");
                ArrayList<Integer> temp = new ArrayList<>();

                int size = ezr.getNumLines();
                if (size > 10)
                    size = 10;

                for (int i = 0; i < size; i++) {
                    temp.add(Integer.parseInt(ezr.getLine(i)));
                    if (completeTime.toMillis() < temp.get(i))
                        temp.add(i, (int) completeTime.toMillis());
                    ezr.getNextLine();
                }

                EZFileWrite ezw = new EZFileWrite("leaderboard.txt");
                for (Integer integer : temp)
                    ezw.writeLine(String.valueOf(integer));
                ezw.saveFile();
            }
        }
    }
}
