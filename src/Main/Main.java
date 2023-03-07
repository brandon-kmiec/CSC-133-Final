package Main;

import java.awt.*;

import Data.Click;
import Data.RECT;
import logic.Control;

public class Main {
    // Fields (Static) below...
    public static String str = "";
    public static RECT[] rs;
    // End Static fields...

    public static void main(String[] args) {
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
        rs = new RECT[3];
        rs[0] = new RECT(166, 248, 261, 372, "Tree");
        rs[1] = new RECT(546, 404, 618, 538, "Persephone");
        rs[2] = new RECT(803, 358, 923, 462, "Cheese Burger");
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        ctrl.addSpriteToFrontBuffer(150, 250, "tree");
        ctrl.addSpriteToFrontBuffer(500, 400, "f0");
        ctrl.addSpriteToFrontBuffer(800, 350, "cheeseBurger");

        if (Control.getMouseInput() != null) {
            for (RECT r : rs) {
                if (r.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    str = r.getTag() + " was Clicked.";
                    break;
                }
                else
                    str = "";
            }
        }
        ctrl.drawString(800, 540, str, Color.white);     // Test Only
    }

    // Additional Static methods below...(if needed)

}
