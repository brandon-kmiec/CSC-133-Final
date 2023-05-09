package Levels;

import Data.*;
import Data.Frame;
import FileIO.EZFileRead;
import Input.Mouse;
import Inventory.Inventory;
import Puzzles.PipePuzzle;
import ScriptingEngine.Interpreter;
//import Sound.Sound;
import logic.Control;
import Graphics.Graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Level1 {
    // Fields
    private final Control ctrl;
    private final RECT nextLevelDoor;
    private final RECT puzzle;
    private RECT itemRect;
    private final Sprite puzzleSprite;
//    private final Sprite textBox;
//    private final Sprite backgroundSprite;
    private final Sprite itemSprite;
    private final Sprite mouseCursor;
    private String doorHoverLabel;
    private String puzzleHoverLabel;
    private String doorDialog;
    private String puzzleDialog;
    private String itemHoverLabel;
    private String inventoryHoverLabel;
    private final Frame closedDoor;
//    private final Animation doorOpen;
    private boolean levelActive;
    private boolean startAnim;
    private boolean holdingItem;
    private boolean nextLevel;
    private boolean inInventory;
    private boolean changeMouse;
    private boolean puzzleActive;
    private boolean complete;
    private boolean musicPlaying;
    private final PipePuzzle pipePuzzle;
    private final AText aText;
    private final ArrayList<AText> aTextList;
    private ArrayList<String> wrap;
    private final RECT[] inventorySlots;
    private final ArrayList<Command> commands;
    private final Interpreter interpreter;
    private int invSlotNum;

    // Constructor
    public Level1(Control ctrl, Inventory inventory) {
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

        aText = new AText("", 20, ctrl);
        aTextList = new ArrayList<>();
        wrap = new ArrayList<>();

//        backgroundSprite = new Sprite(0, 0, ctrl.getSpriteFromBackBuffer("level1Background").getSprite(),
//                "background");
//        textBox = new Sprite(64, 760, ctrl.getSpriteFromBackBuffer("textBox").getSprite(),
//                "textBox");

        puzzleSprite = new Sprite(320, 160, ctrl.getSpriteFromBackBuffer("puzzleSprite").getSprite(),
                "puzzle");
        puzzle = new RECT(puzzleSprite.getX(), puzzleSprite.getY(), puzzleSprite.getX() + 128,
                puzzleSprite.getY() + 128, "puzzleRect", "Puzzle");

        itemSprite = new Sprite(1700, 500, ctrl.getSpriteFromBackBuffer("key").getSprite(), "key");
        itemRect = new RECT(-50, -50, -50, -50, "key", "Key");

        mouseCursor = new Sprite(0, 0, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("moveLevelCursor").getSprite(), -45), "mouseCursor");

        closedDoor = new Frame(830, 0, "level1Door1");

//        doorOpen = new Animation(500, false);
//        doorOpen.addFrame(closedDoor);
//        doorOpen.addFrame(new Frame(830, 0, "level1Door2"));
//        doorOpen.addFrame(new Frame(830, 0, "level1Door3"));
//        doorOpen.addFrame(new Frame(830, 0, "level1Door4"));
//        doorOpen.addFrame(new Frame(830, 30, "level1Door5"));

        pipePuzzle = new PipePuzzle(ctrl, mouseCursor);

        doorHoverLabel = "";
        doorDialog = "";
        puzzleDialog = "";
        puzzleHoverLabel = "";
        itemHoverLabel = "";
        inventoryHoverLabel = "";

        nextLevelDoor = new RECT(860, 46, 1197, 435, "nextLevel", doorHoverLabel);

        commands = new ArrayList<>();
        interpreter = new Interpreter(ctrl, commands);
        readCommands();
    }

    // Methods
    public void runLevel() {
        if (pipePuzzle.isPuzzleSolved() && pipePuzzle.isExitPuzzle()) {
            pipePuzzle.setPuzzleActive(false);
            puzzleActive = false;
        }
        if (PipePuzzle.isPuzzleActive() || puzzleActive) {
            puzzleActive = true;

            BufferedImage levelBackground = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
            Graphics g = levelBackground.getGraphics();
            drawSprites();
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, 1920, 1080);
            g.dispose();
            Sprite sprite = new Sprite(0, 0, levelBackground, "level1Background");

            pipePuzzle.drawPuzzle(sprite);
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

        if (startAnim) {
            if (!musicPlaying) {
                musicPlaying = true;
//                Sound sound = new Sound("puzzleComplete");
//                sound.resetWAV();
//                sound.playWAV();
                interpreter.checkCommand(commands.get(1));
            }

            doorAnimation();
            complete = true;
        } else
            ctrl.addSpriteToFrontBuffer(closedDoor);

//        ctrl.addSpriteToFrontBuffer(backgroundSprite);
        interpreter.checkCommand(commands.get(0));

//        ctrl.addSpriteToFrontBuffer(textBox);
        interpreter.checkCommand(commands.get(4));
        interpreter.checkCommand(commands.get(5));

        ctrl.addSpriteToFrontBuffer(puzzleSprite);

        if (pipePuzzle.isPuzzleSolved() && !holdingItem && !startAnim && !inInventory) {
            interpreter.checkCommand(commands.get(3));
//            ctrl.addSpriteToFrontBuffer(itemSprite);
            itemRect = new RECT(itemSprite.getX(), 516, 1764, 548, itemRect.getTag(),
                    itemRect.getHoverLabel());

        } else if (pipePuzzle.isPuzzleSolved() && inInventory)
            ctrl.addSpriteToHudBuffer(itemSprite);
        else if (pipePuzzle.isPuzzleSolved() && holdingItem) {
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
            aTextList.add(new AText(wrap.get(i), 20, ctrl));
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
            changeMouse = true;
            doorHoverLabel = "Next Level";
        } else if (nextLevelDoor.isCollision(p.x, p.y)) {
            doorHoverLabel = "Closed Door";
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
                if (!pipePuzzle.isPuzzleSolved()) {
                    doorDialog = "The puzzle has not yet been solved. Try solving the puzzle to open the door.";
                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog);
                } else if (pipePuzzle.isPuzzleSolved() && !holdingItem && !startAnim) {
                    doorDialog = "An item is needed to open this door. Look around for something that looks " +
                            "like a gold key.";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog);
                } else if (pipePuzzle.isPuzzleSolved() && holdingItem) {
                    startAnim = true;

                    doorDialog = "";

                    aTextList.clear();
                    wrap = aText.wrapText(doorDialog);
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
                    invSlotNum = i;

                    RECT clickedRect = inventorySlots[i];
                    itemSprite.moveXAbsolute(clickedRect.getX1() + 32);
                    itemSprite.moveYAbsolute(clickedRect.getY1() + 32);
                    inventorySlots[i].changeHoverLabel(itemRect.getHoverLabel());
                } else if (inventorySlots[i].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON) && inInventory && invSlotNum == i) {
                    holdingItem = true;
                    inInventory = false;
                    inventorySlots[i].changeHoverLabel("Inventory Slot #" + (i + 1));
                }
            }
        }
    }

    private void doorAnimation() {
//        Frame curFrame = doorOpen.getCurrentFrame();
//        if (curFrame != null)
//            ctrl.addSpriteToFrontBuffer(curFrame);
        interpreter.checkCommand(commands.get(2));
    }

    private void readCommands() {
        EZFileRead ezr = new EZFileRead("level1Script.txt");
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

    public String saveData() {
        return levelActive + "*" + startAnim + "*" + holdingItem + "*" + nextLevel + "*" + inInventory + "*" +
                changeMouse + "*" + puzzleActive + "*" + pipePuzzle.isPuzzleSolved() + "*" + complete + "*" +
                musicPlaying + "*" + itemSprite.getX() + "*" + itemSprite.getY() + "*" + invSlotNum;
    }

    public void loadData(String str) {
        StringTokenizer st = new StringTokenizer(str, "*");
        levelActive = Boolean.parseBoolean(st.nextToken());
        startAnim = Boolean.parseBoolean(st.nextToken());
        holdingItem = Boolean.parseBoolean(st.nextToken());
        nextLevel = Boolean.parseBoolean(st.nextToken());
        inInventory = Boolean.parseBoolean(st.nextToken());
        changeMouse = Boolean.parseBoolean(st.nextToken());
        puzzleActive = Boolean.parseBoolean(st.nextToken());
        pipePuzzle.setPuzzleSolved(Boolean.parseBoolean(st.nextToken()));
        complete = Boolean.parseBoolean(st.nextToken());
        musicPlaying = Boolean.parseBoolean(st.nextToken());

        if (inInventory) {
            itemSprite.moveXAbsolute(Integer.parseInt(st.nextToken()));
            itemSprite.moveYAbsolute(Integer.parseInt(st.nextToken()));
            invSlotNum = Integer.parseInt(st.nextToken());
            inventorySlots[invSlotNum].changeHoverLabel(itemRect.getHoverLabel());
        }
    }
}

