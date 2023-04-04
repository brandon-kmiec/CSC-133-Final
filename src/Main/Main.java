package Main;

import java.awt.*;
import java.io.File;

import Data.Animation;
import Data.Command;
import Data.Frame;
import Data.RECT;
import FileIO.EZFileWrite;
import Input.Mouse;
import logic.Control;

public class Main {
    // Fields (Static) below...
    public static String s = "";
//    public static RECT r;
//    public static RECT chicken;
//    public static final int dropShadow = 2;
//    private static Animation anim;

    private static Command c;
    private static Command c2;


    public static void main(String[] args) {
        updateArtScript();
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
//        r = new RECT(46, 3, 107, 123, "Persephone", new Frame(0, 0, "f1"));
//        chicken = new RECT(1792, 0, 1910, 128, "Chicken", "Chicken");
//
//        anim = new Animation(128, true);
//        anim.addFrame(new Frame(1792, 0, "c_idle"));
//        anim.addFrame(new Frame(1792, 0, "c_leftfoot"));
//        anim.addFrame(new Frame(1792, 0, "c_idle2"));
//        anim.addFrame(new Frame(1792, 0, "c_rightfoot"));

        String raw = "show_sprite: 100, 100, f0";
        String raw2 = "text: Howdy Partner!";
        c = new Command(raw);
        c2 = new Command(raw2);
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
//        ctrl.addSpriteToFrontBuffer(0, 0, "f0");
//
//        Frame curFrame = anim.getCurrentFrame();
//        if (curFrame != null)
//            ctrl.addSpriteToFrontBuffer(curFrame);
//
//        Point p = Mouse.getMouseCoords();
//        int x = (int) p.getX();
//        int y = (int) p.getY();
//
//        if (r.isCollision(x, y)) {
//            ctrl.addSpriteToFrontBuffer(r.getGraphicalHover().getX(), r.getGraphicalHover().getY(),
//                    r.getGraphicalHover().getSpriteTag());
//        }
//        if (chicken.isCollision(x, y))
//            s = chicken.getHoverLabel();
//        else
//            s = "";
//
//        ctrl.drawString(x, (y - 2), s, Color.BLACK);
//        ctrl.drawString(x - dropShadow, ((y - 2) - dropShadow), s, Color.YELLOW);


        if (c.isCommand("show_sprite") && c.getNumParams() == 3){
            int x = Integer.parseInt(c.getParamByIndex(0));
            int y = Integer.parseInt(c.getParamByIndex(1));
            String tag = c.getParamByIndex(2);
            ctrl.addSpriteToFrontBuffer(x, y, tag);
        }
        if (c2.isCommand("text") && c2.getNumParams() == 1){
            String display = c2.getParamByIndex(0);
            ctrl.drawString(0, 250, display, Color.WHITE);
        }
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
