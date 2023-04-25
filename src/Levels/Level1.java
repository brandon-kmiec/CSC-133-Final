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
    private Levels firstLevel;

    private final Control ctrl;
    private final RECT nextLevelDoor;
    private final RECT puzzle;
    private final RECT itemRect;
    private final Sprite puzzleSprite;
    private final Sprite textBox;
    private final Sprite backgroundSprite;
    private final Sprite itemSprite;
    private String doorHoverLabel;
    private String puzzleHoverLabel;
    private String doorDialog;
    private String puzzleDialog;
    private String itemHoverLabel;
    private final Frame closedDoor;
    private final Animation doorOpen;
    private boolean levelActive;
    private boolean startAnim;
    private boolean holdingItem;
    private boolean nextLevel;
    private final PipePuzzle pipePuzzle;
    private final AText aText;
    private final ArrayList<AText> aTextList;
    private ArrayList<String> wrap;

    private final Inventory inventory;

    // Constructor
    public Level1(Control ctrl, Inventory inventory) {
        this.ctrl = ctrl;
        this.inventory = inventory;

        levelActive = false;
        startAnim = false;
        holdingItem = false;
        nextLevel = false;

        aText = new AText("", 20);
        aTextList = new ArrayList<>();
        wrap = new ArrayList<>();

        backgroundSprite = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("level1Background").getSprite(), "background");
        textBox = new Sprite(64, 760, ctrl.getSpriteFromBackBuffer("textBox").getSprite(), "textBox");

        puzzleSprite = new Sprite(320, 160, ctrl.getSpriteFromBackBuffer("puzzleSprite").getSprite(), "puzzle");
        puzzle = new RECT(puzzleSprite.getX(), puzzleSprite.getY(), puzzleSprite.getX() + 128,
                puzzleSprite.getY() + 128, "puzzleRect", "Click to attempt to solve the puzzle");

        itemSprite = new Sprite(1700, 500, ctrl.getSpriteFromBackBuffer("key").getSprite(), "key");
        itemRect = new RECT(-50, -50, -50, -50, "key", "key");


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

        nextLevelDoor = new RECT(860, 46, 1197, 435, "nextLevel", doorHoverLabel);
        // TODO: 4/23/2023 display "door to next level" when hovered over.  When clicked, say "puzzle not solved" if puzzle is not solved.
        //  When clicked and puzzle solved, say "find an item to open the door" if clicked without proper item.
        //  When clicked with proper item and puzzle solved, activate animation and allow player to go to next room.
    }

    // Methods
    public void setLevelActive(boolean isActive) {
        this.levelActive = isActive;
    }

    public boolean isLevelActive() {
        return levelActive;
    }

    public boolean isNextLevel() {
        return nextLevel;
    }

    public boolean isHoldingItem() {
        return holdingItem;
    }

    public void runLevel() {

        if (pipePuzzle.isPuzzleSolved() && pipePuzzle.isExitPuzzle())
            pipePuzzle.setPuzzleActive(false);
        if (pipePuzzle.isPuzzleActive())
            pipePuzzle.drawPuzzle();
        else {
            if (isLevelActive()) {
                drawSprites();

                checkCollision();

                checkClicked();
            }
            drawAnimatedText();
        }
    }

    private void drawSprites() {
        Point p = Mouse.getMouseCoords();

        if (startAnim) {
            doorAnimation();
        } else {
            ctrl.addSpriteToFrontBuffer(closedDoor);
        }

        ctrl.addSpriteToFrontBuffer(backgroundSprite);
        ctrl.addSpriteToFrontBuffer(textBox);

        ctrl.addSpriteToFrontBuffer(puzzleSprite);

        if (pipePuzzle.isPuzzleSolved() && !holdingItem && !startAnim) {
            // TODO: 4/24/2023 spawn an item that can be clicked
            ctrl.addSpriteToFrontBuffer(itemSprite);
            itemRect.changeLocation(1700, 516, 1827, 549);
        }
        if (pipePuzzle.isPuzzleSolved() && holdingItem) {
            itemSprite.moveXAbsolute(p.x);
            itemSprite.moveYAbsolute(p.y);
            ctrl.addSpriteToFrontBuffer(itemSprite);
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

        if (puzzle.isCollision(p.x, p.y) && pipePuzzle.isPuzzleSolved()) {
            puzzleHoverLabel = "The Puzzle has been solved";
        } else if (puzzle.isCollision(p.x, p.y)) {
            puzzleHoverLabel = puzzle.getHoverLabel();
        } else
            puzzleHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), puzzleHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, puzzleHoverLabel, Color.YELLOW);

        if (nextLevelDoor.isCollision(p.x, p.y) && startAnim) {
            ctrl.addSpriteToOverlayBuffer(p.x - 32, p.y - 16, "moveLevelCursor");
            doorHoverLabel = "Open Door";
        } else if (nextLevelDoor.isCollision(p.x, p.y)) {
            doorHoverLabel = "Closed Door";
        } else
            doorHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), doorHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, doorHoverLabel, Color.YELLOW);

        if (itemRect.isCollision(p.x, p.y)) {
            itemHoverLabel = itemRect.getHoverLabel() + "(Send to inventory)";
        } else
            itemHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), itemHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, itemHoverLabel, Color.YELLOW);
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
                    // TODO: 4/24/2023 change cursor back to game cursor (do not put item back in inventory)
                    doorDialog = "";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 200);
                    holdingItem = false;
                } else if (pipePuzzle.isPuzzleSolved() && startAnim) {
                    nextLevel = true;
                }
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
            if (itemRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) &&
                    !inventory.inInventory(itemSprite.getX(), itemSprite.getY())) {

                itemSprite.moveXAbsolute(1184);
                itemSprite.moveYAbsolute(900);

                itemRect.changeLocation(1184, 916, 1311, 949);
//                holdingItem = true;
                // TODO: 4/24/2023 change itemRect to be at the inventory location of item
                // TODO: 4/24/2023 change absolute x and y of item sprite to the location in the inventory
            } else if (itemRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) &&
                    inventory.inInventory(itemSprite.getX(), itemSprite.getY())) {
                holdingItem = true;
            }
        }
    }

    private void doorAnimation() {
        Frame curFrame = doorOpen.getCurrentFrame();
        if (curFrame != null)
            ctrl.addSpriteToFrontBuffer(curFrame);
    }
}

