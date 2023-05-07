package Data;

import logic.Control;
import timer.stopWatchX;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AText {
    // Fields
    private String srcStr;      // The string to print
    private String destStr;     // The part of the string currently being displayed
    private stopWatchX timer;   // This controls the speed of the text
    private boolean isFinished; // Flag for when animation is over
    private int cursor;         // Track the index into the srcStr
    private Control ctrl;

    // Constructor
    public AText(String srcStr, int delay, Control ctrl) {
        this.srcStr = srcStr;
        this.ctrl = ctrl;
        timer = new stopWatchX(delay);
        destStr = "";
        isFinished = false;
        cursor = 0;
    }

    // Methods
    public String getCurrentStr() {
        if (isFinished)
            return destStr;
        if (timer.isTimeUp()) {
            if (cursor < srcStr.length())
                destStr += srcStr.charAt(cursor++);
            if (cursor >= srcStr.length())
                isFinished = true;
            timer.resetWatch();
        }
        return destStr;
    }

    public boolean isAnimationDone() {
        return isFinished;
    }

    public void resetAnimation() {
        isFinished = false;
        destStr = "";
        cursor = 0;
        timer.resetWatch();
    }

    public void changeStr(String newStr) {
        this.srcStr = newStr;
    }

    public ArrayList<String> wrapText(String raw) {
//        String[] words = raw.split(" ");
//        ArrayList<String> lines = new ArrayList<>();
//        int count = 0;
//        String current = "";
//        Queue<String > q = new LinkedList<>();
//        for (String word : words)
//            q.add(word);
//
//        while (q.peek() != null) {
//            if (q.peek().length() + count < n){
//                current += q.poll() + " ";
//                count += current.length();
//            } else {
//                lines.add(current);
//                current = "";
//                count = 0;
//            }
//        }
//        lines.add(current);
        Graphics g = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Font font = ctrl.getFont();
        String[] words = raw.split(" ");
        String[] ret = gameString.wordWrapX(words, 500, g, font);

        return new ArrayList<>(Arrays.asList(ret));
    }
}
