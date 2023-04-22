package Puzzles;

import java.awt.image.BufferedImage;

public class DirectionBufferedImage {
    // Fields
    private final BufferedImage image;
    private boolean up, down, left, right;     // Directions
    private boolean bottomRight, topRight, topBottom, leftBottom, leftTop, leftRight;   // Connections

    // Constructor
    public DirectionBufferedImage(BufferedImage image) {
        this.image = image;
        up = false;
        down = false;
        left = false;
        right = false;

        bottomRight = false;
        topRight = false;
        topBottom = false;
        leftBottom = false;
        leftTop = false;
        leftRight = false;
    }

    // Methods
    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean bottom) {
        this.down = bottom;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(boolean bottomRight) {
        this.bottomRight = bottomRight;
    }

    public boolean isTopRight() {
        return topRight;
    }

    public void setTopRight(boolean topRight) {
        this.topRight = topRight;
    }

    public boolean isTopBottom() {
        return topBottom;
    }

    public void setTopBottom(boolean topBottom) {
        this.topBottom = topBottom;
    }

    public boolean isLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(boolean leftBottom) {
        this.leftBottom = leftBottom;
    }

    public boolean isLeftTop() {
        return leftTop;
    }

    public void setLeftTop(boolean leftTop) {
        this.leftTop = leftTop;
    }

    public boolean isLeftRight() {
        return leftRight;
    }

    public void setLeftRight(boolean leftRight) {
        this.leftRight = leftRight;
    }
}
