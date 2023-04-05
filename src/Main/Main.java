package Main;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
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

    public static String s2 = "";
    private static int[] buffer;
    private static RECT disk;
    private static final int dropShadow = 2;

    private static ArrayList<Command> commands;


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

        EZFileRead ezr = new EZFileRead("script.txt");
        commands = new ArrayList<>();
        for (int i = 0; i < ezr.getNumLines(); i++) {
            String raw = ezr.getLine(i);
            raw = raw.trim();
            if (!raw.equals("")) {
                boolean b = raw.charAt(0) == '#';
                if (!b)
                    commands.add(new Command(raw));
            }
        }

        disk = new RECT(101, 52, 162, 112, "savetag", "Save Game");
        buffer = new int[40];
        for (int i = 0; i < buffer.length; i++) {
            int value = (int) (Math.random() * 100);
            buffer[i] = value;
        }
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


        for (Command c : commands) {
            if (c.isCommand("show_sprite") && c.getNumParams() == 3) {
                int x = Integer.parseInt(c.getParamByIndex(0));
                int y = Integer.parseInt(c.getParamByIndex(1));
                String tag = c.getParamByIndex(2);
                ctrl.addSpriteToFrontBuffer(x, y, tag);
            } else if (c.isCommand("text") && c.getNumParams() == 1) {
                String display = c.getParamByIndex(0);
                ctrl.drawString(0, 250, display, Color.WHITE);
            }
        }

        ctrl.addSpriteToFrontBuffer(100, 50, "saveicon");
        Point p = Mouse.getMouseCoords();
        int x = (int) p.getX();
        int y = (int) p.getY();
        if (disk.isCollision(x, y))
            s = disk.getHoverLabel();
        else
            s = "";
        ctrl.drawString(x, (y - 2), s, Color.BLACK);
        ctrl.drawString(x - dropShadow, (y - dropShadow) - 2, s, Color.yellow);
        if (Control.getMouseInput() != null)
            if (disk.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                saveData();
                s2 = "Game Saved";
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
