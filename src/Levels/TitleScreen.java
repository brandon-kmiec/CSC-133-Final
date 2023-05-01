package Levels;

import Data.Click;
import Data.RECT;
import Data.Sprite;
import Input.Mouse;
import logic.Control;
import Graphics.Graphic;

import java.awt.*;

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

    // Constructor
    public TitleScreen(Control ctrl) {
        this.ctrl = ctrl;
        levelActive = false;

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        startRect = new RECT(131, 681, 876, 839, "start");
        leaderboardRect = new RECT(968, 682, 1844, 841, "leaderboard");

        hoverText = "";
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

        ctrl.addSpriteToFrontBuffer(0, 0, "titleScreen");

        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

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

        if (leaderboardClicked)
            displayLeaderboard();
    }

    private void displayLeaderboard() {
        // TODO: 4/25/2023 display the leaderboard (get the top ten times from a file called leaderboard.txt)
        // TODO: 5/1/2023 read times from a file if there are times inside the file (current line != null)
    }
}
