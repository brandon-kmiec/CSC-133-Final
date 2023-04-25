package Levels;

import Data.Click;
import Data.RECT;
import Data.Sprite;
import logic.Control;

public class TitleScreen {
    // Fields
    private final Control ctrl;
    private boolean levelActive;
    private boolean startClicked;
    private boolean leaderboardClicked;
    private final RECT startRect;
    private final RECT leaderboardRect;

    // Constructor
    public TitleScreen(Control ctrl) {
        this.ctrl = ctrl;
        levelActive = false;

        startRect = new RECT(131, 681, 876, 839, "start");
        leaderboardRect = new RECT(968, 682, 1844, 841, "leaderboard");
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
        // TODO: 4/24/2023 if startRect is clicked, set startClicked to true and in Main Update set Level1 levelActive
        //  to true if startClicked is true.  Then set levelActive for TitleScreen to false
        ctrl.addSpriteToFrontBuffer(0, 0, "titleScreen");

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
    }
}
