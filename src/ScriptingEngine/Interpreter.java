package ScriptingEngine;

import Data.Click;
import Data.Command;
import Data.RECT;
import Input.Mouse;
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

    // Constructor
    public Interpreter(Control ctrl, ArrayList<Command> commands) {
        this.commands = commands;
        this.ctrl = ctrl;
        hoverLabelStr = "";
        tagStr = "";
        rectCommands = new ArrayList<>();
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
}
