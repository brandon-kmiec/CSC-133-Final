package ScriptingEngine;

import Data.Animation;
import Data.Click;
import Data.Command;
import Data.Frame;
import Data.RECT;
import Input.Mouse;
import Sound.Sound;
import logic.Control;

import java.awt.*;
import java.util.ArrayList;

public class Interpreter {
    // Fields
    private static final int dropShadow = 2;
    private Control ctrl;
    private static String hoverLabelStr;
    private static String tagStr;
    private ArrayList<Command> commands;
    private static ArrayList<Command> rectCommands;
    private static boolean musicPlaying;
    private static boolean animationPlaying;
    private static Animation animation;

    // Constructor
    public Interpreter(Control ctrl, ArrayList<Command> commands) {
        this.commands = commands;
        this.ctrl = ctrl;
        hoverLabelStr = "";
        tagStr = "";
        rectCommands = new ArrayList<>();
        musicPlaying = false;
        animationPlaying = false;
    }

    // Methods
    public void checkCommands() {
        for (Command c : commands) {
            if (c.isCommand("show_sprite") && c.getNumParams() == 3)
                showSprite(c);
            else if (c.isCommand("text") && c.getNumParams() == 1)
                showText(c);
            else if (c.isCommand("text") && c.getNumParams() == 3)
                showTextAtCoords(c);
            else if (c.isCommand("text_hover_rect") && c.getNumParams() == 6)
                rectCommands.add(c);
            else if (c.isCommand("load_bg") && c.getNumParams() == 1)
                loadBackground(c);
            else if (c.isCommand("display_overlay_item") && c.getNumParams() == 3)
                displayItem(c);
            else if (c.isCommand("play_music") && c.getNumParams() == 1)
                playMusic(c);
            else if (c.isCommand("play_music_loop") && c.getNumParams() == 1)
                playMusicLoop(c);
            else if (c.isCommand("start_animation") && c.getNumParams() >= 6) {
                int delay = Integer.parseInt(c.getParamByIndex(0));
                boolean isLooping = Boolean.parseBoolean(c.getParamByIndex(1));
                animation = new Animation(delay, isLooping);
                startAnimation(c);
            }
        }

        if (!rectCommands.isEmpty())
            clickableRect(rectCommands);
    }

    private void showSprite(Command c) {
        int sprite_x = Integer.parseInt(c.getParamByIndex(0));
        int sprite_y = Integer.parseInt(c.getParamByIndex(1));
        String tag = c.getParamByIndex(2);
        ctrl.addSpriteToFrontBuffer(sprite_x, sprite_y, tag);
    }

    private void showText(Command c) {
        String display = c.getParamByIndex(0);
        ctrl.drawString(0, 250, display, Color.WHITE);
    }

    private void showTextAtCoords(Command c) {
        int sprite_x = Integer.parseInt(c.getParamByIndex(0));
        int sprite_y = Integer.parseInt(c.getParamByIndex(1));
        String display = c.getParamByIndex(2);
        ctrl.drawString(sprite_x, sprite_y, display, Color.WHITE);
    }

    private void clickableRect(ArrayList<Command> command) {
        Point p = Mouse.getMouseCoords();
        int x = (int) p.getX();
        int y = (int) p.getY();

        for (Command c : command) {
            int x1 = Integer.parseInt(c.getParamByIndex(0));
            int y1 = Integer.parseInt(c.getParamByIndex(1));
            int x2 = Integer.parseInt(c.getParamByIndex(2));
            int y2 = Integer.parseInt(c.getParamByIndex(3));
            String tag = c.getParamByIndex(4);
            String hoverLabel = c.getParamByIndex(5);
            RECT testRect = new RECT(x1, y1, x2, y2, tag, hoverLabel);

            if (testRect.isCollision(x, y))
                hoverLabelStr = testRect.getHoverLabel();
            else
                hoverLabelStr = "";

            if (Control.getMouseInput() != null)
                if (testRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                    tagStr = testRect.getTag() + " was clicked";
                    break;
                } else
                    tagStr = "";

            ctrl.drawString(x, (y - 2), hoverLabelStr, Color.BLACK);
            ctrl.drawString(x - dropShadow, (y - dropShadow) - 2, hoverLabelStr, Color.yellow);
            ctrl.drawString(150, 800, tagStr, Color.WHITE);
        }
    }

    private void loadBackground(Command c) {
        ctrl.addSpriteToFrontBuffer(0, 0, c.getParamByIndex(0));
    }

    private void displayItem(Command c) {
        int x = Integer.parseInt(c.getParamByIndex(0));
        int y = Integer.parseInt(c.getParamByIndex(1));
        String tag = c.getParamByIndex(2);
        ctrl.addSpriteToOverlayBuffer(x, y, tag);
    }

    private void playMusic(Command c) {
        Sound music = new Sound(c.getParamByIndex(0));
        if (!musicPlaying) {
            music.playWAV();
            musicPlaying = true;
        }
        if (music.isFinished())
            musicPlaying = false;
    }

    private void playMusicLoop(Command c) {
        Sound music = new Sound(c.getParamByIndex(0));
        if (!musicPlaying) {
            music.setLoop();
            musicPlaying = true;
        }
    }

    // TODO: 4/20/2023 figure out why animation doesn't hold the frames (draws c_idle then gives null for remaining frames) 
    private void startAnimation(Command c) {
        int x = Integer.parseInt(c.getParamByIndex(2));
        int y = Integer.parseInt(c.getParamByIndex(3));

        String[] spriteTags = new String[c.getNumParams() - 4];
        Frame[] frames = new Frame[c.getNumParams() - 4];


        //Animation animation = new Animation(delay, isLooping);

        if (!animationPlaying) {
            for (int i = 0; i < spriteTags.length; i++) {
                frames[i] = new Frame(x, y, c.getParamByIndex(i + 4));
                spriteTags[i] = c.getParamByIndex(i + 4);
                System.out.println(frames[i].getSpriteTag());
            }
            for (int i = 4; i < c.getNumParams(); i++) {
                animation.addFrame(new Frame(x, y, spriteTags[i - 4]));
                //System.out.println(animation.getCurrentFrame().getSpriteTag());
                //System.out.println(c.getParamByIndex(i));
            }
            animationPlaying = true;
        }

        //Frame currentFrame = animation.getCurrentFrame();
//        if (animation.getCurrentFrame() != null)
//            ctrl.addSpriteToFrontBuffer(animation.getCurrentFrame());

//        System.out.println(animation.getCurrentFrame());

        for (int i = 0; i < 4; i++) {
            Frame currentFrame = animation.getCurrentFrame();
            if (currentFrame != null) {
                ctrl.addSpriteToFrontBuffer(currentFrame);
                System.out.println(currentFrame);
            }
        }

//        if (currentFrame != null)

//        if (currentFrame == null)
//            animation.restartAnim();
//        if (animation.isFinished()) {
//            animation.restartAnim();
//        }
    }
}
