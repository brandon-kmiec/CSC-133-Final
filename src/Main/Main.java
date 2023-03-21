package Main;

import java.awt.*;
import java.io.File;

import Data.Animation;
import Data.Click;
import Data.Frame;
import Data.RECT;
import FileIO.EZFileWrite;
import Input.Mouse;
import logic.Control;

public class Main {
    // Fields (Static) below...
    public static String s = "";
    public static RECT r;
    public static final int dropShadow = 2;
    private static Animation anim;

//    //Checkpoint #2
//    public static String str = "";
//    public static RECT[] rs;
    // End Static fields...

    public static void main(String[] args) {
        updateArtScript();
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
//        r = new RECT(46, 3, 107, 123, "Persephone", new Frame(0, 0, "f1"));
        anim = new Animation(120, true);
        anim.addFrame(new Frame(0, 0, "c_idle"));
        anim.addFrame(new Frame(0, 0, "c_leftfoot"));
        anim.addFrame(new Frame(0, 0, "c_idle"));
        anim.addFrame(new Frame(0, 0, "c_rightfoot"));

//        //Checkpoint #2
//        rs = new RECT[3];
//        rs[0] = new RECT(166, 248, 261, 372, "Tree");
//        rs[1] = new RECT(546, 404, 618, 538, "Persephone");
//        rs[2] = new RECT(803, 358, 923, 462, "Cheese Burger");
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        Frame curFrame = anim.getCurrentFrame();
        if (curFrame != null)
            ctrl.addSpriteToFrontBuffer(curFrame);
//        ctrl.addSpriteToFrontBuffer(0, 0, "f0");
//        Point p = Mouse.getMouseCoords();
//        int x = (int) p.getX();
//        int y = (int) p.getY();
//        if (r.isCollision(x, y))
//            ctrl.addSpriteToFrontBuffer(r.getGraphicalHover().getX(), r.getGraphicalHover().getY(),
//                    r.getGraphicalHover().getSpriteTag());
        //s = r.getHoverLabel();
        //else
        //s = "";
//        ctrl.drawString(x, (y - 2), s, Color.BLACK);
//        ctrl.drawString(x - dropShadow, ((y - dropShadow) - 2), s, Color.yellow);

//        //Checkpoint #2
//        ctrl.addSpriteToFrontBuffer(150, 250, "tree");
//        ctrl.addSpriteToFrontBuffer(500, 400, "f0");
//        ctrl.addSpriteToFrontBuffer(800, 350, "cheeseBurger");
//
//        if (Control.getMouseInput() != null) {
//            for (RECT r : rs) {
//                if (r.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
//                    str = r.getTag() + " was Clicked.";
//                    break;
//                }
//                else
//                    str = "";
//            }
//        }
//        ctrl.drawString(800, 540, str, Color.white);     // Test Only
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

}
