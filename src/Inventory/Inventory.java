package Inventory;

import Data.RECT;
import Data.Sprite;
import Puzzles.PipePuzzle;
import logic.Control;

import java.awt.*;

public class Inventory {
    // Fields
    private final Control ctrl;
    private final Sprite[] inventorySlots = new Sprite[5];
    private final RECT[] rects = new RECT[5];

    // Constructor
    public Inventory(Control ctrl) {
        this.ctrl = ctrl;
        for (int i = 0; i < rects.length; i++) {
            int x1 = (i << 7) + 800;
            int x2 = (i << 7) + 928;
            rects[i] = new RECT(x1, 930, x2, 1058, "slot" + i + 1, "Inventory Slot #" + (i + 1));
            inventorySlots[i] = new Sprite(x1, 930, ctrl.getSpriteFromBackBuffer("inventorySlot").getSprite(), "inventory" + i);
        }
    }

    // Methods
    public void drawInventory() {
        if (!PipePuzzle.isPuzzleActive()) {
            ctrl.addSpriteToHudBuffer(inventorySlots[0]);
            ctrl.drawHudString(inventorySlots[0].getX() + 8, inventorySlots[0].getY() + 24, "1", Color.WHITE);
            ctrl.addSpriteToHudBuffer(inventorySlots[1]);
            ctrl.drawHudString(inventorySlots[1].getX() + 8, inventorySlots[1].getY() + 24, "2", Color.WHITE);
            ctrl.addSpriteToHudBuffer(inventorySlots[2]);
            ctrl.drawHudString(inventorySlots[2].getX() + 8, inventorySlots[2].getY() + 24, "3", Color.WHITE);
            ctrl.addSpriteToHudBuffer(inventorySlots[3]);
            ctrl.drawHudString(inventorySlots[3].getX() + 8, inventorySlots[3].getY() + 24, "4", Color.WHITE);
            ctrl.addSpriteToHudBuffer(inventorySlots[4]);
            ctrl.drawHudString(inventorySlots[4].getX() + 8, inventorySlots[4].getY() + 24, "5", Color.WHITE);
        }
    }

    public RECT[] getInventorySlots() {
        return rects;
    }
}
