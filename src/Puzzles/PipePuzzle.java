package Puzzles;

import Data.Click;
import Data.Frame;
import Data.RECT;
import Data.Sprite;
import Graphics.Graphic;
import Input.Mouse;
import Particles.Particle;
import logic.Control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PipePuzzle {
    // Fields
    private final Control ctrl;
    private static final Sprite[][] pipePuzzle = new Sprite[6][6];
    private static final RECT[][] pipeRect = new RECT[6][6];
    private static final ArrayList<Sprite> swapList = new ArrayList<>();
    private static final ArrayList<Point> swapPoints = new ArrayList<>();
    private boolean solved;
    private static boolean puzzleActive;
    private boolean exitPuzzle;
    private static boolean up, down, left, right;
    private static final DirectionBufferedImage[] images = new DirectionBufferedImage[6];
    private static String s;
    private static int row, column;
    private static DirectionBufferedImage previous;
    private static RECT puzzleSolvedRect;
    private final Sprite mouseCursor;

    // Constructor
    public PipePuzzle(Control ctrl, Sprite mouseCursor) {
        this.mouseCursor = mouseCursor;

        s = "last clicked: ";

        row = 5;
        column = 0;
        previous = null;

        this.ctrl = ctrl;

        solved = false;
        puzzleActive = false;
        exitPuzzle = false;

        up = false;
        down = false;
        left = false;
        right = false;

        puzzleSolvedRect = new RECT(832, 412, 1088, 668, "puzzleSolvedRect");

        images[0] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe90Angle").getSprite(), 0.0));
        images[0].setBottomRight(true);

        images[1] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe90Angle").getSprite(), 90.0));
        images[1].setLeftBottom(true);

        images[2] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe90Angle").getSprite(), 180.0));
        images[2].setLeftTop(true);

        images[3] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe90Angle").getSprite(), 270.0));
        images[3].setTopRight(true);

        images[4] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe180Angle").getSprite(), 0.0));
        images[4].setTopBottom(true);

        images[5] = new DirectionBufferedImage(Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipe180Angle").getSprite(), 90.0));
        images[5].setLeftRight(true);

        Sprite[] sprites = new Sprite[6];
        sprites[0] = new Sprite(0, 0, images[0].getImage(), "bottomRight");
        sprites[1] = new Sprite(0, 0, images[1].getImage(), "bottomLeft");
        sprites[2] = new Sprite(0, 0, images[2].getImage(), "topLeft");
        sprites[3] = new Sprite(0, 0, images[3].getImage(), "topRight");
        sprites[4] = new Sprite(0, 0, images[4].getImage(), "topBottom");
        sprites[5] = new Sprite(0, 0, images[5].getImage(), "leftRight");

        for (int i = 0; i < pipePuzzle.length; i++)
            for (int j = 0; j < pipePuzzle.length; j++) {
                int x = (i << 7) + 576;
                int y = (j << 7) + 156;
                pipePuzzle[i][j] = new Sprite((i << 7), (j << 7), images[j].getImage(), sprites[j].getTag());
                pipeRect[i][j] = new RECT(x, y, x + 128, y + 128, "pipeHover", new Frame(x, y, "boxOutline"));
            }

        for (int i = 0; i < 3; i++)
            randomizeBoard();
    }

    // Methods
    public boolean isPuzzleSolved() {
        return solved;
    }

    public boolean isExitPuzzle() {
        return exitPuzzle;
    }

    public static boolean isPuzzleActive() {
        return puzzleActive;
    }

    public void setPuzzleActive(boolean puzzleActive) {
        this.puzzleActive = puzzleActive;
    }

    private static void randomizeBoard() {
        Sprite temp;
        for (int i = 0; i < pipePuzzle.length; i++)
            for (int j = 0; j < pipePuzzle.length; j++) {
                int r = Particle.rollDie(6) - 1;
                int c = Particle.rollDie(6) - 1;

                temp = pipePuzzle[i][j];
                pipePuzzle[i][j] = pipePuzzle[r][c];
                pipePuzzle[r][c] = temp;
            }
    }

    public void drawPuzzle() {
        Point p = Mouse.getMouseCoords();

        mouseCursor.moveXAbsolute(p.x - 16);
        mouseCursor.moveYAbsolute(p.y - 18);
        ctrl.addSpriteToOverlayBuffer(mouseCursor);

        Sprite pipeStart = new Sprite(448, 796, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipeEnd").getSprite(), 0.0), "start");
        Sprite pipeEnd = new Sprite(1344, 156, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipeEnd").getSprite(), 180.0), "end");

        ctrl.addSpriteToFrontBuffer(pipeStart);
        ctrl.addSpriteToFrontBuffer(pipeEnd);

        for (int i = 0; i < pipePuzzle.length; i++)
            for (int j = 0; j < pipePuzzle.length; j++) {
                int x = (i << 7) + 576;
                int y = (j << 7) + 156;

                pipePuzzle[j][i].moveXAbsolute(x);
                pipePuzzle[j][i].moveYAbsolute(y);
                ctrl.addSpriteToFrontBuffer(pipePuzzle[j][i]);

//                ctrl.drawString(x, (y + 16), "(" + j + ", " + i + ")", Color.white);
                drawGridOutline(i, j, x, y);

                if (pipeRect[i][j].isCollision(p.x, p.y))
                    ctrl.addSpriteToFrontBuffer(pipeRect[i][j].getGraphicalHover());
                if (Control.getMouseInput() != null)
                    if (pipeRect[i][j].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                        swapList.add(pipePuzzle[j][i]);
                        swapPoints.add(new Point(j, i));

                        s = "last clicked: (" + j + ", " + i + ")";
                        if (!solved)
                            if (swapList.size() == 2)
                                swap();
                    }
                ctrl.drawString(576, 146, s, Color.white);
            }

        if (!solved)
            isSolved();
        else {
            ctrl.addSpriteToFrontBuffer(832, 412, "puzzleSolved");
            if (Control.getMouseInput() != null)
                if (puzzleSolvedRect.isClicked(Control.getMouseInput(), Click.LEFT_BUTTON))
                    exitPuzzle = true;
        }
    }

    private void drawGridOutline(int i, int j, int x, int y) {
        Sprite gridOutline = new Sprite(x, y, ctrl.getSpriteFromBackBuffer("boxClickOutline").getSprite(), "outline");
        ctrl.addSpriteToFrontBuffer(gridOutline);

        Sprite gridCorner, gridEdge;
        if (i == 0 && j == 0) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineCorner").getSprite(), 270.0);
            gridCorner = new Sprite(x - 2, y - 2, temp, "topLeft");
            ctrl.addSpriteToFrontBuffer(gridCorner);
        }
        if (i == 5 && j == 0) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineCorner").getSprite(), 0.0);
            gridCorner = new Sprite(x + 2, y - 2, temp, "topRight");
            ctrl.addSpriteToFrontBuffer(gridCorner);
        }
        if (i == 0 && j == 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineCorner").getSprite(), 180.0);
            gridCorner = new Sprite(x - 2, y + 2, temp, "bottomLeft");
            ctrl.addSpriteToFrontBuffer(gridCorner);
        }
        if (i == 5 && j == 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineCorner").getSprite(), 90.0);
            gridCorner = new Sprite(x + 2, y + 2, temp, "bottomRight");
            ctrl.addSpriteToFrontBuffer(gridCorner);
        }

        if (i == 0 && j > 0 && j < 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineEdge").getSprite(), 270.0);
            gridEdge = new Sprite(x - 2, y - 2, temp, "top");
            ctrl.addSpriteToFrontBuffer(gridEdge);

            Sprite tempSprite = new Sprite(gridEdge);
            tempSprite.moveYAbsolute(y + 4);
            ctrl.addSpriteToFrontBuffer(tempSprite);
        }
        if (i == 5 && j > 0 && j < 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineEdge").getSprite(), 90.0);
            gridEdge = new Sprite(x + 2, y - 2, temp, "top");
            ctrl.addSpriteToFrontBuffer(gridEdge);

            Sprite tempSprite = new Sprite(gridEdge);
            tempSprite.moveYAbsolute(y + 4);
            ctrl.addSpriteToFrontBuffer(tempSprite);
        }
        if (j == 0 && i > 0 && i < 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineEdge").getSprite(), 0.0);
            gridEdge = new Sprite(x - 2, y - 2, temp, "top");
            ctrl.addSpriteToFrontBuffer(gridEdge);

            Sprite tempSprite = new Sprite(gridEdge);
            tempSprite.moveXAbsolute(x + 4);
            ctrl.addSpriteToFrontBuffer(tempSprite);
        }
        if (j == 5 && i > 0 && i < 5) {
            BufferedImage temp = Graphic.rotateImageByDegrees(
                    ctrl.getSpriteFromBackBuffer("gridOutlineEdge").getSprite(), 180.0);
            gridEdge = new Sprite(x - 2, y + 2, temp, "top");
            ctrl.addSpriteToFrontBuffer(gridEdge);

            Sprite tempSprite = new Sprite(gridEdge);
            tempSprite.moveXAbsolute(x + 4);
            ctrl.addSpriteToFrontBuffer(tempSprite);
        }
    }

    private void swap() {
        Sprite temp = swapList.get(0);
        pipePuzzle[swapPoints.get(0).x][swapPoints.get(0).y] = swapList.get(1);
        pipePuzzle[swapPoints.get(1).x][swapPoints.get(1).y] = temp;

        swapList.clear();
        swapPoints.clear();
    }

    private void isSolved() {
        row = 5;
        column = 0;
        previous = null;
        for (int count = 0; count < 36; count++) {
            if (row == 0 && column == 6) {
                solved = true;
                break;
            } else if (row == -1 || row == 6 || column == -1 || column == 6) {
                row = 5;
                column = 0;
                break;
            }
            for (DirectionBufferedImage image : images) {
                if (pipePuzzle[row][column].getSprite() == image.getImage()) {
                    if (row == 5 && column == 0) {
                        if (image.isLeftTop()) {
                            row--;
                            previous = image;
                            up = true;
                            down = false;
                            left = false;
                            right = false;
                            break;
                        } else if (image.isLeftRight()) {
                            column++;
                            previous = image;
                            up = false;
                            down = false;
                            left = false;
                            right = true;
                            break;
                        } else if (image.isLeftBottom()) {
                            row++;
                            previous = image;
                            up = false;
                            down = true;
                            left = false;
                            right = false;
                            break;
                        }
                    } else if (row == 0 && column == 5) {
                        if (previous.isBottomRight() && right) {
                            if (image.isLeftRight())
                                column++;
                            break;
                        } else if (previous.isLeftRight() && right) {
                            if (image.isLeftRight())
                                column++;
                            break;
                        } else if (previous.isTopBottom() && up) {
                            if (image.isBottomRight())
                                column++;
                            break;
                        } else if (previous.isLeftTop() && up) {
                            if (image.isBottomRight())
                                column++;
                            break;
                        }
                    } else {
                        if (previous.isTopBottom() && up) {
                            if (image.isTopBottom()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isBottomRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            } else if (image.isLeftBottom()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            }
                        } else if (previous.isTopBottom() && down) {
                            if (image.isTopBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftTop()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            }
                        } else if (previous.isLeftTop() && up) {
                            if (image.isTopBottom()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftBottom()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            } else if (image.isBottomRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            }
                        } else if (previous.isLeftTop() && left) {
                            if (image.isBottomRight()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftRight()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            }
                        } else if (previous.isTopRight() && right) {
                            if (image.isLeftRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            } else if (image.isLeftTop()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            }
                        } else if (previous.isTopRight() && up) {
                            if (image.isTopBottom()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isBottomRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            } else if (image.isLeftBottom()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            }
                        } else if (previous.isBottomRight() && right) {
                            if (image.isLeftRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            } else if (image.isLeftTop()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            }
                        } else if (previous.isBottomRight() && down) {
                            if (image.isTopBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftTop()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            }
                        } else if (previous.isLeftRight() && left) {
                            if (image.isBottomRight()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftRight()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            }
                        } else if (previous.isLeftRight() && right) {
                            if (image.isLeftRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            } else if (image.isLeftTop()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            }
                        } else if (previous.isLeftBottom() && down) {
                            if (image.isTopBottom()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftTop()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                column++;
                                previous = image;
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                                break;
                            }
                        } else if (previous.isLeftBottom() && left) {
                            if (image.isBottomRight()) {
                                row++;
                                previous = image;
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isTopRight()) {
                                row--;
                                previous = image;
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                                break;
                            } else if (image.isLeftRight()) {
                                column--;
                                previous = image;
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
