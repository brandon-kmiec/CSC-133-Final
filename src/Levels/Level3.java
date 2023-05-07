package Levels;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import Graphics.Graphic;
import Input.Mouse;
import Inventory.Inventory;
import ScriptingEngine.Interpreter;
import Sound.Sound;
import logic.Control;

import java.awt.*;
import java.util.ArrayList;

public class Level3 {
    // Fields
    private final Control ctrl;
    private final RECT nextLevelDoor;
    private RECT itemRect;
    private final RECT stickFigureRect;
//    private final Sprite textBox;
//    private final Sprite backgroundSprite;
    private final Sprite itemSprite;
    private final Sprite mouseCursor;
    private final Sprite stickFigure;
//    private final Sprite boat;
    private String doorHoverLabel;
    private String stickFigureHoverLabel;
    private String doorDialog;
    private String puzzleDialog;
    private String itemHoverLabel;
    private String inventoryHoverLabel;
    private boolean levelActive;
    private boolean holdingItem;
    private boolean nextLevel;
    private boolean inInventory;
    private boolean changeMouse;
    private boolean complete;
    private boolean fedBurger;
    private final AText aText;
    private final ArrayList<AText> aTextList;
    private ArrayList<String> wrap;
    private final RECT[] inventorySlots;
    private final ArrayList<Command> commands;
    private final Interpreter interpreter;

    // Constructor
    public Level3(Control ctrl, Inventory inventory) {
        this.ctrl = ctrl;

        inventorySlots = inventory.getInventorySlots();

        levelActive = false;
        holdingItem = false;
        nextLevel = false;
        inInventory = false;
        changeMouse = false;
        complete = false;
        fedBurger = false;

        aText = new AText("", 20, ctrl);
        aTextList = new ArrayList<>();
        wrap = new ArrayList<>();

//        backgroundSprite = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("level3Background").getSprite(),
//                "background");
//        textBox = new Sprite(64, 760, ctrl.getSpriteFromBackBuffer("textBox").getSprite(),
//                "textBox");

        stickFigure = new Sprite(670, 700, ctrl.getSpriteFromBackBuffer("stickFigure").getSprite(),
                "Stick Figure");
        stickFigureRect = new RECT(stickFigure.getX(), stickFigure.getY(), stickFigure.getX() + 128,
                stickFigure.getY() + 128, "stickFigure", "Stick Figure",
                new Frame(stickFigure.getX(), stickFigure.getY(), "stickFigure"));

        itemSprite = new Sprite(1500, 750, ctrl.getSpriteFromBackBuffer("cheeseBurger").getSprite(), "cheeseBurger");
        itemRect = new RECT(-50, -50, -50, -50, "cheeseBurger", "Cheese Burger");

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        doorHoverLabel = "";
        doorDialog = "";
        puzzleDialog = "";
        stickFigureHoverLabel = "";
        itemHoverLabel = "";
        inventoryHoverLabel = "";

//        boat = new Sprite(500, 500, ctrl.getSpriteFromBackBuffer("boat").getSprite(), "boat");
        nextLevelDoor = new RECT(500, 500, 628, 581, "boat", "Boat");

        commands = new ArrayList<>();
        interpreter = new Interpreter(ctrl, commands);
        readCommands();
    }

    // Methods
    public void runLevel() {
        if (levelActive) {
            drawSprites();
            checkCollision();
            checkClicked();
        }
        drawAnimatedText();
    }

    public void setLevelActive(boolean isActive) {
        this.levelActive = isActive;
    }

    public boolean isLevelActive() {
        return levelActive;
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(boolean nextLevel) {
        this.nextLevel = nextLevel;
    }

    public boolean isComplete() {
        return complete;
    }

    private void drawSprites() {
        Point p = Mouse.getMouseCoords();

//        ctrl.addSpriteToFrontBuffer(backgroundSprite);
        interpreter.checkCommand(commands.get(0));

//        ctrl.addSpriteToFrontBuffer(boat);
        interpreter.checkCommand(commands.get(1));

//        ctrl.addSpriteToFrontBuffer(textBox);
        interpreter.checkCommand(commands.get(2));

        ctrl.addSpriteToFrontBuffer(stickFigure);

        if (!fedBurger) {
            if (!holdingItem && !inInventory) {
//                ctrl.addSpriteToFrontBuffer(itemSprite);
                interpreter.checkCommand(commands.get(3));

                itemRect = new RECT(itemSprite.getX(), itemSprite.getY(), itemSprite.getX() + 128,
                        itemSprite.getY() + 128, itemRect.getTag(), itemRect.getHoverLabel());
            } else if (inInventory)
                ctrl.addSpriteToHudBuffer(itemSprite);
            else {
                itemSprite.moveXAbsolute(p.x);
                itemSprite.moveYAbsolute(p.y);
                ctrl.addSpriteToHudBuffer(itemSprite);
            }
        }

        if (changeMouse) {
            ctrl.addSpriteToOverlayBuffer(p.x - 32, p.y - 16, "moveLevelCursor");
        } else {
            mouseCursor.moveXAbsolute(p.x - 16);
            mouseCursor.moveYAbsolute(p.y - 18);
            ctrl.addSpriteToOverlayBuffer(mouseCursor);
        }
    }

    private void drawAnimatedText() {
        for (int i = 0; i < wrap.size(); i++) {
            aTextList.add(new AText(wrap.get(i), 20, ctrl));
            String test = aTextList.get(i).getCurrentStr();
            ctrl.drawString(70, 790 + (i * 20), test, Color.black);
        }
    }

    private void checkCollision() {
        Point p = Mouse.getMouseCoords();

        if (stickFigureRect.isCollision(p.x, p.y)) {
            stickFigureHoverLabel = stickFigureRect.getHoverLabel();
            ctrl.addSpriteToFrontBuffer(stickFigureRect.getGraphicalHover());
        } else
            stickFigureHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), stickFigureHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, stickFigureHoverLabel, Color.YELLOW);

        if (nextLevelDoor.isCollision(p.x, p.y) /*&& startAnim*/) {
            changeMouse = true;
            doorHoverLabel = "Escape";
        } else {
            doorHoverLabel = "";
            changeMouse = false;
        }
        ctrl.drawString(p.x, (p.y - 2), doorHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, doorHoverLabel, Color.YELLOW);

        if (itemRect.isCollision(p.x, p.y))
            itemHoverLabel = itemRect.getHoverLabel();
        else
            itemHoverLabel = "";
        for (RECT inventorySlot : inventorySlots)
            if (inventorySlot.isCollision(p.x, p.y) && inInventory) {
                itemHoverLabel = inventorySlot.getHoverLabel();
                inventoryHoverLabel = "";
                break;
            } else if (inventorySlot.isCollision(p.x, p.y) && !inInventory) {
                itemHoverLabel = "";
                inventoryHoverLabel = inventorySlot.getHoverLabel();
                break;
            } else
                inventoryHoverLabel = "";
        ctrl.drawHudString(p.x, (p.y - 2), itemHoverLabel, Color.BLACK);
        ctrl.drawHudString((p.x - 2), (p.y - 2) - 2, itemHoverLabel, Color.YELLOW);
        ctrl.drawHudString(p.x, (p.y - 2), inventoryHoverLabel, Color.BLACK);
        ctrl.drawHudString((p.x - 2), (p.y - 2) - 2, inventoryHoverLabel, Color.YELLOW);
    }

    private void checkClicked() {
        if (Control.getMouseInput() != null) {
            if (nextLevelDoor.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                if (!fedBurger) {
                    doorDialog = "This is not your boat. Try talking to the stick figure.";
                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog);
                } else {
                    doorDialog = "";
                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog);
                    holdingItem = false;
                    nextLevel = true;
                }
            }

            if (stickFigureRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                if (!fedBurger && !holdingItem) {
                    puzzleDialog = "I will let you use my boat in exchange for a Cheese Burger.";
                    aTextList.clear();
                    wrap = aText.wrapText(puzzleDialog);
                } else if (holdingItem) {
                    puzzleDialog = "Thank you for the food!  You are free to use the boat.";
                    aTextList.clear();
                    wrap = aText.wrapText(puzzleDialog);
                    holdingItem = false;
                    fedBurger = true;
                    complete = true;

                    Sound sound = new Sound("puzzleComplete");
                    sound.resetWAV();
                    sound.playWAV();
                } else {
                    puzzleDialog = "Thank you for the food!  You are free to use the boat.";
                    aTextList.clear();
                    wrap = aText.wrapText(puzzleDialog);
                }
            }

            if (itemRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && !holdingItem) {
                holdingItem = true;
                itemRect = new RECT(-50, -50, -50, -50, itemRect.getTag(), itemRect.getHoverLabel());
            }
            for (int i = 0; i < inventorySlots.length; i++) {
                if (inventorySlots[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && holdingItem) {
                    inInventory = true;
                    holdingItem = false;

                    RECT clickedRect = inventorySlots[i];
                    itemSprite.moveXAbsolute(clickedRect.getX1());
                    itemSprite.moveYAbsolute(clickedRect.getY1());
                    inventorySlots[i].changeHoverLabel(itemRect.getTag());
                } else if (inventorySlots[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && inInventory) {
                    holdingItem = true;
                    inInventory = false;
                    inventorySlots[i].changeHoverLabel("Inventory Slot #" + (i + 1));
                }
            }
        }
    }

    private void readCommands() {
        EZFileRead ezr = new EZFileRead("level3Script.txt");
        for (int i = 0; i < ezr.getNumLines(); i++) {
            String raw = ezr.getLine(i);
            raw = raw.trim();
            if (!raw.equals("")) {
                boolean b = raw.charAt(0) == '#';
                if (!b)
                    commands.add(new Command(raw));
            }
        }
    }
}
