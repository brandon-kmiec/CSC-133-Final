package Levels;

import Data.*;
import Data.Frame;
import Input.Mouse;
import Inventory.Inventory;
import Puzzles.PipePuzzle;
import logic.Control;

import java.awt.*;
import java.util.ArrayList;

public class Level1 {
    // Fields
    private final Control ctrl;
    private final RECT nextLevelDoor;
    private final RECT puzzle;
    private RECT itemRect;
    private final Sprite puzzleSprite;
    private final Sprite textBox;
    private final Sprite backgroundSprite;
    private final Sprite itemSprite;
    private String doorHoverLabel;
    private String puzzleHoverLabel;
    private String doorDialog;
    private String puzzleDialog;
    private String itemHoverLabel;
    private String inventoryHoverLabel;
    private final Frame closedDoor;
    private final Animation doorOpen;
    private boolean levelActive;
    private boolean startAnim;
    private boolean holdingItem;
    private boolean nextLevel;
    private boolean inInventory;
    private final PipePuzzle pipePuzzle;
    private final AText aText;
    private final ArrayList<AText> aTextList;
    private ArrayList<String> wrap;
    private final RECT[] inventorySlots;

    // Constructor
    public Level1(Control ctrl, Inventory inventory) {
        this.ctrl = ctrl;

        inventorySlots = inventory.getInventorySlots();

        levelActive = false;
        startAnim = false;
        holdingItem = false;
        nextLevel = false;
        inInventory = false;

        aText = new AText("", 20);
        aTextList = new ArrayList<>();
        wrap = new ArrayList<>();

        backgroundSprite = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("level1Background").getSprite(),
                "background");
        textBox = new Sprite(64, 760, ctrl.getSpriteFromBackBuffer("textBox").getSprite(),
                "textBox");

        puzzleSprite = new Sprite(320, 160, ctrl.getSpriteFromBackBuffer("puzzleSprite").getSprite(),
                "puzzle");
        puzzle = new RECT(puzzleSprite.getX(), puzzleSprite.getY(), puzzleSprite.getX() + 128,
                puzzleSprite.getY() + 128, "puzzleRect", "Puzzle");

        itemSprite = new Sprite(1700, 500, ctrl.getSpriteFromBackBuffer("key").getSprite(), "key");
        itemRect = new RECT(-50, -50, -50, -50, "key", "Key");

        closedDoor = new Frame(830, 0, "level1Door1");

        doorOpen = new Animation(500, false);
        doorOpen.addFrame(closedDoor);
        doorOpen.addFrame(new Frame(830, 0, "level1Door2"));
        doorOpen.addFrame(new Frame(830, 0, "level1Door3"));
        doorOpen.addFrame(new Frame(830, 0, "level1Door4"));
        doorOpen.addFrame(new Frame(830, 30, "level1Door5"));

        pipePuzzle = new PipePuzzle(ctrl);

        doorHoverLabel = "";
        doorDialog = "";
        puzzleDialog = "";
        puzzleHoverLabel = "";
        itemHoverLabel = "";
        inventoryHoverLabel = "";

        nextLevelDoor = new RECT(860, 46, 1197, 435, "nextLevel", doorHoverLabel);
    }

    // Methods
    public void runLevel() {
        if (pipePuzzle.isPuzzleSolved() && pipePuzzle.isExitPuzzle())
            pipePuzzle.setPuzzleActive(false);
        if (pipePuzzle.isPuzzleActive())
            pipePuzzle.drawPuzzle();
        else {
            if (levelActive) {
                drawSprites();
                checkCollision();
                checkClicked();
            }
            drawAnimatedText();
        }
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

    private void drawSprites() {
        Point p = Mouse.getMouseCoords();

        if (startAnim)
            doorAnimation();
        else
            ctrl.addSpriteToFrontBuffer(closedDoor);

        ctrl.addSpriteToFrontBuffer(backgroundSprite);
        ctrl.addSpriteToFrontBuffer(textBox);

        ctrl.addSpriteToFrontBuffer(puzzleSprite);

        if (pipePuzzle.isPuzzleSolved() && !holdingItem && !startAnim && !inInventory) {
            ctrl.addSpriteToFrontBuffer(itemSprite);
            itemRect = new RECT(itemSprite.getX(), 516, 1764, 548, itemRect.getTag(),
                    itemRect.getHoverLabel());
        } else if (pipePuzzle.isPuzzleSolved() && inInventory)
            ctrl.addSpriteToHudBuffer(itemSprite);
        else if (pipePuzzle.isPuzzleSolved() && holdingItem) {
            itemSprite.moveXAbsolute(p.x);
            itemSprite.moveYAbsolute(p.y);
            ctrl.addSpriteToHudBuffer(itemSprite);
        }
    }

    private void drawAnimatedText() {
        for (int i = 0; i < wrap.size(); i++) {
            aTextList.add(new AText(wrap.get(i), 20));
            String test = aTextList.get(i).getCurrentStr();
            ctrl.drawString(70, 790 + (i * 20), test, Color.black);
        }
    }

    private void checkCollision() {
        Point p = Mouse.getMouseCoords();

        if (puzzle.isCollision(p.x, p.y))
            puzzleHoverLabel = puzzle.getHoverLabel();
        else
            puzzleHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), puzzleHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, puzzleHoverLabel, Color.YELLOW);

        if (nextLevelDoor.isCollision(p.x, p.y) && startAnim) {
            ctrl.addSpriteToOverlayBuffer(p.x - 32, p.y - 16, "moveLevelCursor");
            doorHoverLabel = "Next Level";
        } else if (nextLevelDoor.isCollision(p.x, p.y))
            doorHoverLabel = "Closed Door";
        else
            doorHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), doorHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, doorHoverLabel, Color.YELLOW);

        if (itemRect.isCollision(p.x, p.y))
            itemHoverLabel = itemRect.getHoverLabel();
        else
            itemHoverLabel = "";
        for (RECT inventorySlot : inventorySlots) {
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
        }
        ctrl.drawHudString(p.x, (p.y - 2), itemHoverLabel, Color.BLACK);
        ctrl.drawHudString((p.x - 2), (p.y - 2) - 2, itemHoverLabel, Color.YELLOW);
        ctrl.drawHudString(p.x, (p.y - 2), inventoryHoverLabel, Color.BLACK);
        ctrl.drawHudString((p.x - 2), (p.y - 2) - 2, inventoryHoverLabel, Color.YELLOW);
    }

    private void checkClicked() {
        if (Control.getMouseInput() != null) {
            if (nextLevelDoor.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                if (!pipePuzzle.isPuzzleSolved()) {
                    doorDialog = "The puzzle has not yet been solved. Try solving the puzzle to open the door.";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 183);
                } else if (pipePuzzle.isPuzzleSolved() && !holdingItem && !startAnim) {
                    doorDialog = "An item is needed to open this door. Look around for something that looks " +
                            "like a gold key.";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 200);
                } else if (pipePuzzle.isPuzzleSolved() && holdingItem) {
                    startAnim = true;

                    doorDialog = "";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 200);
                    holdingItem = false;
                } else if (pipePuzzle.isPuzzleSolved() && startAnim)
                    nextLevel = true;
            }
            if (puzzle.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                if (!pipePuzzle.isPuzzleSolved())
                    pipePuzzle.setPuzzleActive(true);
                else {
                    puzzleDialog = "The puzzle has already been solved. Find an item to open the door.";

                    aTextList.clear();
                    wrap = aText.wrapText(puzzleDialog, 200);
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
                    itemSprite.moveXAbsolute(clickedRect.getX1() + 32);
                    itemSprite.moveYAbsolute(clickedRect.getY1() + 32);
                    inventorySlots[i].changeHoverLabel(itemRect.getTag());
                } else if (inventorySlots[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && inInventory) {
                    holdingItem = true;
                    inInventory = false;
                    inventorySlots[i].changeHoverLabel("Inventory Slot" + (i + 1));
                }
            }
        }
    }

    private void doorAnimation() {
        Frame curFrame = doorOpen.getCurrentFrame();
        if (curFrame != null)
            ctrl.addSpriteToFrontBuffer(curFrame);
    }
}

