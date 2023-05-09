package Puzzles;

import Data.*;
import Data.Frame;
import Input.Mouse;
import Particles.Particle;
import logic.Control;

import java.awt.*;
import java.util.ArrayList;

public class SimonSays {
    // Fields
    private final Control ctrl;
    private static Sprite[] colors;
    private final Sprite playSequence;
    private final RECT playSequenceRect;
    private static RECT[] colorsRect;
    private static ArrayList<String> sequence, userSequence;
    private boolean solved;
    private boolean puzzleActive;
    private boolean exitPuzzle;
    private final Sprite mouseCursor;
    private static Animation sequenceAnim;

    // Constructor
    public SimonSays(Control ctrl, Sprite mouseCursor) {
        this.ctrl = ctrl;
        this.mouseCursor = mouseCursor;

        colors = new Sprite[4];
        colors[0] = new Sprite(500, 700, ctrl.getSpriteFromBackBuffer("red").getSprite(), "red");
        colors[1] = new Sprite(628, 700, ctrl.getSpriteFromBackBuffer("green").getSprite(), "green");
        colors[2] = new Sprite(756, 700, ctrl.getSpriteFromBackBuffer("blue").getSprite(), "blue");
        colors[3] = new Sprite(884, 700, ctrl.getSpriteFromBackBuffer("yellow").getSprite(), "yellow");

        playSequence = new Sprite(300, 500, ctrl.getSpriteFromBackBuffer("playSequence").getSprite(),
                "sequence button");
        playSequenceRect = new RECT(300, 500, 428, 628, "sequence button", new Frame(300, 500,
                "playSequenceHover"));

        colorsRect = new RECT[4];
        colorsRect[0] = new RECT(500, 700, 628, 828, "red", new Frame(500, 700, "redHover"));
        colorsRect[1] = new RECT(628, 700, 756, 828, "green", new Frame(628, 700, "greenHover"));
        colorsRect[2] = new RECT(756, 700, 884, 828, "blue", new Frame(756, 700, "blueHover"));
        colorsRect[3] = new RECT(884, 700, 1012, 828, "yellow", new Frame(884, 700, "yellowHover"));

        sequence = new ArrayList<>();
        userSequence = new ArrayList<>();

        solved = false;
        puzzleActive = false;
        exitPuzzle = false;

        sequenceAnim = new Animation(500, false);

        randomizeSequence();


        for (int i = 0; i < sequence.size(); i++) {
            int x = (i << 7) + 500;
            sequenceAnim.addFrame(new Frame(x, 500, sequence.get(i)));
            sequenceAnim.addFrame(new Frame(x, 500, sequence.get(i)));
        }
    }

    // Methods
    public boolean isPuzzleSolved() {
        return solved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {this.solved = puzzleSolved;}

    public boolean isExitPuzzle() {
        return exitPuzzle;
    }

    public boolean isPuzzleActive() {
        return puzzleActive;
    }

    public void setPuzzleActive(boolean isPuzzleActive) {
        this.puzzleActive = isPuzzleActive;
    }

    private static void randomizeSequence() {
        for (int i = 0; i < 5; i++)
            sequence.add(colors[Particle.rollDie(colors.length) - 1].getTag());
    }

    public void drawSequence(Sprite sprite) {
        ctrl.addSpriteToFrontBuffer(sprite);

        ctrl.drawString(playSequenceRect.getX1(), playSequenceRect.getY1() - 20,
                "Simon Says: Repeat the shown sequence.", Color.WHITE);

        Point p = Mouse.getMouseCoords();

        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

            Frame curFrame = sequenceAnim.getCurrentFrame();
            if (curFrame != null && !sequenceAnim.isFinished())
                ctrl.addSpriteToFrontBuffer(curFrame);

        ctrl.addSpriteToFrontBuffer(playSequence);

        if (playSequenceRect.isCollision(p.x, p.y))
            ctrl.addSpriteToFrontBuffer(playSequenceRect.getGraphicalHover());

        if (sequenceAnim.isFinished()) {

            for (int i = 0; i < colors.length; i++) {
                ctrl.addSpriteToFrontBuffer(colors[i]);
                if (colorsRect[i].isCollision(p.x, p.y))
                    ctrl.addSpriteToFrontBuffer(colorsRect[i].getGraphicalHover());
            }

            if (Control.getMouseInput() != null) {
                if (playSequenceRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    sequenceAnim.restartAnim();
                    userSequence.clear();
                }

                for (RECT rect : colorsRect)
                    if (rect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON))
                        userSequence.add(rect.getTag());
            }
        }

        if (userSequence.size() == sequence.size())
            checkSequence();

        if (isPuzzleSolved())
            exitPuzzle = true;
    }

    private void checkSequence() {
        int count = 0;
        for (int i = 0; i < sequence.size(); i++) {
            if (!userSequence.get(i).equals(sequence.get(i)))
                break;
            else
                count++;
        }

        if (count == sequence.size())
            solved = true;
        else {
            solved = false;
            userSequence.clear();
            sequenceAnim.restartAnim();
        }
    }

}
