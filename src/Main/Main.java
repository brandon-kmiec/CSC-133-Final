package Main;

import java.awt.*;
import java.awt.image.BufferedImage;

import Data.Sprite;
import logic.Control;

public class Main {
    // Fields (Static) below...
    public static Sprite s;
    // End Static fields...

    public static void main(String[] args) {
        Control ctrl = new Control();                // Do NOT remove!
        ctrl.gameLoop();                            // Do NOT remove!
    }

    /* This is your access to things BEFORE the game loop starts */
    public static void start(Control ctrl) {
        // TODO: Code your starting conditions here...NOT DRAW CALLS HERE! (no addSprite or drawString)
        BufferedImage pImage = ctrl.getSpriteFromBackBuffer("tree").getSprite();
        BufferedImage bi2 = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

        Graphics g = bi2.getGraphics();
        Font temp = ctrl.getFont().deriveFont(Font.BOLD);
        g.setFont(temp);
        g.setColor(Color.green);
        g.drawString("My tile-based tree level", 800, 540);
        for (int i = 0; i < 15; i++) {
            int x = i << 7, x2 = 0, x3 = 1792;
            int y = 0, y2 = 1024, y3 = i << 7;
            BufferedImage pCopy = pImage.getSubimage(0, 0, 128, 128);
            g.drawImage(pCopy, x, y, null);     //Top
            g.drawImage(pCopy, x, y2, null);    //Bottom
            g.drawImage(pCopy, x2, y3, null);   //Left
            g.drawImage(pCopy, x3, y3, null);   //Right
        }
        g.dispose();
        s = new Sprite(0, 0, bi2, "bi2");
    }

    /* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
    public static void update(Control ctrl) {
        // TODO: This is where you can code! (Starting code below is just to show you how it works)
        ctrl.addSpriteToFrontBuffer(s);
    }

    // Additional Static methods below...(if needed)

}
