package Inventory;

import Data.Click;
import Data.RECT;
import Data.Sprite;
import Input.Mouse;
import Levels.Level1;
import logic.Control;

import java.awt.*;

public class Inventory {
    // Fields
    private Control ctrl;
    private Sprite[] sprites = new Sprite[5];
    private RECT[] rects = new RECT[5];

    // Constructor
    public Inventory(Control ctrl) {
        this.ctrl = ctrl;
        for (int i = 0; i < rects.length; i++) {
            int x1 = (i << 7) + 800;
            int x2 = (i << 7) + 928;
            rects[i] = new RECT(x1, 800, x2, 928, "slot" + i + 1);
        }
    }

    // Methods
    public void drawInventory() {
//        for (int i = 0; i < 5; i++){
//            int x1 = (i << 7) + 800;
////            ctrl.addSpriteToHudBuffer(new Sprite(x1, 800, ctrl.getSpriteFromBackBuffer("inventorySlot.png").getSprite(), "test"));
//        }
//        Point p = Mouse.getMouseCoords();
//
//        if (Control.getMouseInput() != null){
//            for (int i = 0; i < rects.length; i++){
//                if (rects[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && sprites[i] != null){
//                    ctrl.addSpriteToOverlayBuffer(p.x, p.y, sprites[i].getTag());
//                }
//                else if (rects[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && sprites[i] == null){
//                    sprites[i] =
//                }
//            }
//        }
    }

    public boolean inInventory(int x, int y) {
        for (int i = 0; i < 5; i++){
            if (rects[i].isCollision(x, y))
                return true;
        }
        return false;
    }
}
