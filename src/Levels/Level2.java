package Levels;

import Data.*;
import Data.Frame;
import Graphics.Graphic;
import Input.Mouse;
import Inventory.Inventory;
import Puzzles.SimonSays;
import Sound.Sound;
import logic.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level2 {
    // Fields
    private final Control ctrl;
    private final RECT nextLevelDoor;
    private final RECT puzzle;
    private RECT itemRect;
    private final Sprite puzzleSprite;
    private final Sprite textBox;
    private final Sprite backgroundSprite;
    private final Sprite itemSprite;
    private final Sprite mouseCursor;
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
    private boolean changeMouse;
    private boolean puzzleActive;
    private boolean complete;
    private boolean musicPlaying;
    private final SimonSays simonSays;
    private final AText aText;
    private final ArrayList<AText> aTextList;
    private ArrayList<String> wrap;
    private final RECT[] inventorySlots;
    private final Sound explosion;

    // Constructor
    public Level2(Control ctrl, Inventory inventory) {
        this.ctrl = ctrl;

        inventorySlots = inventory.getInventorySlots();

        levelActive = false;
        startAnim = false;
        holdingItem = false;
        nextLevel = false;
        inInventory = false;
        changeMouse = false;
        puzzleActive = false;
        complete = false;
        musicPlaying = false;

        aText = new AText("", 20);
        aTextList = new ArrayList<>();
        wrap = new ArrayList<>();

        backgroundSprite = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("level2Background").getSprite(),
                "background");
        textBox = new Sprite(64, 760, ctrl.getSpriteFromBackBuffer("textBox").getSprite(),
                "textBox");

        puzzleSprite = new Sprite(320, 560, ctrl.getSpriteFromBackBuffer("simonSaysPuzzle").getSprite(),
                "puzzle");
        puzzle = new RECT(puzzleSprite.getX(), puzzleSprite.getY(), puzzleSprite.getX() + 128,
                puzzleSprite.getY() + 128, "puzzleRect", "Puzzle",
                new Frame(puzzleSprite.getX(), puzzleSprite.getY(), "simonSaysPuzzleHover"));

        itemSprite = new Sprite(1700, 500, ctrl.getSpriteFromBackBuffer("dynamite").getSprite(), "dynamite");
        itemRect = new RECT(-50, -50, -50, -50, "dynamite", "dynamite");

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        closedDoor = new Frame(780, 500, "boulder");

        doorOpen = new Animation(500, false);
        doorOpen.addFrame(closedDoor);
        doorOpen.addFrame(new Frame(780, 500, "rubble"));

        simonSays = new SimonSays(ctrl, mouseCursor);

        doorHoverLabel = "";
        doorDialog = "";
        puzzleDialog = "";
        puzzleHoverLabel = "";
        itemHoverLabel = "";
        inventoryHoverLabel = "";

        nextLevelDoor = new RECT(780, 527, 908, 628, "boulder", doorHoverLabel);

        explosion = new Sound("fireworkExplosion");
    }

    // Methods
    public void runLevel() {
        if (simonSays.isPuzzleSolved() && simonSays.isExitPuzzle()) {
            simonSays.setPuzzleActive(false);
            puzzleActive = false;
        }
        if (simonSays.isPuzzleActive()) {
            puzzleActive = true;

            BufferedImage levelBackground = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
            Graphics g = levelBackground.getGraphics();
            drawSprites();
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, 1920, 1080);
            g.dispose();
            Sprite sprite = new Sprite(0, 0, levelBackground, "level1Background");

            simonSays.drawSequence(sprite);
        } else {
            if (levelActive) {
                drawSprites();
                checkCollision();
                checkClicked();
            }
            drawAnimatedText();
        }
    }

    public boolean isPuzzleActive() {
        return puzzleActive;
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

        ctrl.addSpriteToFrontBuffer(backgroundSprite);

        if (startAnim) {
            if (!musicPlaying) {
                musicPlaying = true;
                Sound sound = new Sound("puzzleComplete");
                sound.resetWAV();
                sound.playWAV();

                explosion.restartWAV();
            }
            doorAnimation();
            complete = true;
        } else
            ctrl.addSpriteToFrontBuffer(closedDoor);

        ctrl.addSpriteToFrontBuffer(textBox);

        ctrl.addSpriteToFrontBuffer(puzzleSprite);

        if (simonSays.isPuzzleSolved() && !holdingItem && !startAnim && !inInventory) {
            ctrl.addSpriteToFrontBuffer(itemSprite);
            itemRect = new RECT(1712, itemSprite.getY(), 1751, itemSprite.getY() + 128, itemRect.getTag(),
                    itemRect.getHoverLabel());
        } else if (simonSays.isPuzzleSolved() && inInventory)
            ctrl.addSpriteToHudBuffer(itemSprite);
        else if (simonSays.isPuzzleSolved() && holdingItem) {
            itemSprite.moveXAbsolute(p.x);
            itemSprite.moveYAbsolute(p.y);
            ctrl.addSpriteToHudBuffer(itemSprite);
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
            aTextList.add(new AText(wrap.get(i), 20));
            String test = aTextList.get(i).getCurrentStr();
            ctrl.drawString(70, 790 + (i * 20), test, Color.black);
        }
    }

    private void checkCollision() {
        Point p = Mouse.getMouseCoords();

        if (puzzle.isCollision(p.x, p.y)) {
            puzzleHoverLabel = puzzle.getHoverLabel();
            ctrl.addSpriteToFrontBuffer(puzzle.getGraphicalHover());
        } else
            puzzleHoverLabel = "";
        ctrl.drawString(p.x, (p.y - 2), puzzleHoverLabel, Color.BLACK);
        ctrl.drawString((p.x - 2), (p.y - 2) - 2, puzzleHoverLabel, Color.YELLOW);

        if (nextLevelDoor.isCollision(p.x, p.y) && startAnim) {
            changeMouse = true;
            doorHoverLabel = "Next Level";
        } else if (nextLevelDoor.isCollision(p.x, p.y)) {
            doorHoverLabel = "Boulder";
            changeMouse = false;
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
                if (!simonSays.isPuzzleSolved()) {
                    doorDialog = "The puzzle has not yet been solved. Try solving the puzzle to open the path.";
                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 183);
                } else if (simonSays.isPuzzleSolved() && !holdingItem && !startAnim) {
                    doorDialog = "An item is needed to open the path. Look around for something that looks " +
                            "like dynamite.";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 150);
                } else if (simonSays.isPuzzleSolved() && holdingItem) {
                    startAnim = true;

                    doorDialog = "";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog, 200);
                    holdingItem = false;
                } else if (simonSays.isPuzzleSolved() && startAnim)
                    nextLevel = true;
            }
            if (puzzle.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                if (!simonSays.isPuzzleSolved())
                    simonSays.setPuzzleActive(true);
                else {
                    puzzleDialog = "The puzzle has already been solved. Find an item to open the path.";

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
                    inventorySlots[i].changeHoverLabel("Inventory Slot #" + (i + 1));
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
