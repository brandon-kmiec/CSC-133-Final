package Levels;

import Data.Click;
import Data.RECT;
import Data.Sprite;
import Graphics.Graphic;
import Input.Mouse;
import Particles.Firework;
import Particles.UpdateParticles;
import logic.Control;

import java.awt.*;
import java.time.Duration;

public class FinishScreen {
    // Fields
    private final Control ctrl;
    private boolean levelActive;
    private boolean restartClicked;
    //    private boolean leaderboardClicked;
//    private final RECT startRect;
//    private final RECT leaderboardRect;
    private final RECT restartRect;
    private String hoverText;
    private Duration completeTime;
    private final Sprite mouseCursor;
    private static Firework firework;
//    private Font font;

    // Constructor
    public FinishScreen(Control ctrl) {
        // TODO: 5/1/2023 add a button to allow the user to save the time to a file

        this.ctrl = ctrl;
        levelActive = false;

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        // TODO: 5/1/2023 change RECT bounds
        restartRect = new RECT(131, 681, 876, 839, "restart");
//        startRect = new RECT(131, 681, 876, 839, "start");
//        leaderboardRect = new RECT(968, 682, 1844, 841, "leaderboard");

        hoverText = "";
        completeTime = null;

//        font = new Font(ctrl.getFont().getFontName(), ctrl.getFont().getStyle(), 200);
//        font = ctrl.getFont();

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

        // TODO: 5/1/2023 find a way to run a limited num of times (if counter < some number where counter++ is at the end of the if statement body)
        UpdateParticles fireworkParticles = new UpdateParticles(ctrl, true, firework.getParticleSystem());
        for (int i = 0; i < firework.explosions.length; i++)
            if (firework.explosions[i] != null)
                new UpdateParticles(ctrl, false, firework.explosions[i].getParticleSystem());

        // TODO: 5/1/2023 make a sprite for the finish screen
//        ctrl.addSpriteToFrontBuffer(0, 0, "titleScreen");

        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

        // TODO: 5/1/2023 modify for restartRect
//        if (startRect.isCollision(p.x, p.y))
//            hoverText = "Click to Start";
//        else if (leaderboardRect.isCollision(p.x, p.y))
//            hoverText = "Click to view Leaderboard";
//        else
//            hoverText = "";

        ctrl.drawString(p.x, (p.y - 2), hoverText, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, hoverText, Color.YELLOW);

        // TODO: 5/1/2023 modify for restartRect
        if (Control.getMouseInput() != null) {
//            if (startRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
//                startClicked = true;
//            }
//            if (leaderboardRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
//                leaderboardClicked = true;
//            }
        }

        if (completeTime != null) {
            long milliseconds = completeTime.toMillis() % 1000;
            long seconds = completeTime.toMillis() / 1000 % 60;
            long minutes = completeTime.toMillis() / (60 * 1000) % 60;
            long hours = completeTime.toMillis() / (60 * 60 * 1000) % 24;
            long days = completeTime.toMillis() / (24 * 60 * 60 * 1000);

            String temp = days + " " + hours + ":" + minutes + ":" + seconds + "." + milliseconds;
            ctrl.drawHudString(500, 500, temp, Color.WHITE);
        }
    }

}
