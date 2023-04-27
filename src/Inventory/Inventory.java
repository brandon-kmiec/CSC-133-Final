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
    private Sprite[] inventorySlots = new Sprite[5];
    private RECT[] rects = new RECT[5];

    // Constructor
    public Inventory(Control ctrl) {
        this.ctrl = ctrl;
        for (int i = 0; i < rects.length; i++) {
            int x1 = (i << 7) + 800;
            int x2 = (i << 7) + 928;
            rects[i] = new RECT(x1, 930, x2, 1058, "slot" + i + 1, "Inventory Slot " + (i + 1));
            inventorySlots[i] = new Sprite(x1, 930, ctrl.getSpriteFromBackBuffer("inventorySlot").getSprite(), "inventory" + i);
        }
    }

    // Methods
    public void drawInventory() {

        ctrl.addSpriteToHudBuffer(inventorySlots[0]);
        ctrl.addSpriteToHudBuffer(inventorySlots[1]);
        ctrl.addSpriteToHudBuffer(inventorySlots[2]);
        ctrl.addSpriteToHudBuffer(inventorySlots[3]);
        ctrl.addSpriteToHudBuffer(inventorySlots[4]);
    }

    public RECT[] getInventorySlots() {
        return rects;
    }
}
