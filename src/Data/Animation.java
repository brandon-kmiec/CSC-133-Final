package Data;

import timer.stopWatchX;

import java.util.ArrayList;

public class Animation {
    //Fields
    private ArrayList<Frame> frames;
    private stopWatchX timer;
    private int cursor;
    private boolean isLooping;
    private boolean isFinished;

    //Constructor
    public Animation(int delay, boolean isLooping) {
        frames = new ArrayList<>();
        timer = new stopWatchX(delay);
        this.isLooping = isLooping;
        cursor = 0;
        isFinished = false;
    }

    //Methods
    public void addFrame(Frame newFrame) {
        frames.add(newFrame);
    }

    public Frame getCurrentFrame() {
        if (frames.isEmpty()) return null;
        if (timer.isTimeUp()) {
            timer.resetWatch();
            cursor++;
            if (cursor > (frames.size() - 1)) {
                if (isLooping) cursor = 0;
                else {
                    cursor--;
                    isFinished = true;
                }
            }
        }
        return frames.get(cursor);
    }

    public void restartAnim() {
        cursor = 0;
        isFinished = false;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
