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
    private Control ctrl;
    private static Sprite[][] pipePuzzle = new Sprite[6][6];
    private static RECT[][] pipeRect = new RECT[6][6];
    private static ArrayList<Sprite> swapList = new ArrayList<>();
    private static ArrayList<Point> swapPoints = new ArrayList<>();
    private static boolean solved;
    private static DirectionBufferedImage[] images = new DirectionBufferedImage[6];
    private static boolean up, down, left, right;
    private static String s, s2;
    private static int row, column;
    private static DirectionBufferedImage previous;

    // Constructor
    public PipePuzzle(Control ctrl) {
        s = "last clicked: ";
        s2 = "";

        row = 5;
        column = 0;
        previous = null;

        this.ctrl = ctrl;

        solved = false;

        up = false;
        down = false;
        left = false;
        right = false;

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
        sprites[0] = new Sprite(0, 0, images[0].getImage(), "Pipe0");
        sprites[1] = new Sprite(0, 0, images[1].getImage(), "Pipe1");
        sprites[2] = new Sprite(0, 0, images[2].getImage(), "Pipe2");
        sprites[3] = new Sprite(0, 0, images[3].getImage(), "Pipe3");
        sprites[4] = new Sprite(0, 0, images[4].getImage(), "Pipe4");
        sprites[5] = new Sprite(0, 0, images[5].getImage(), "Pipe5");

        for (int i = 0; i < pipePuzzle.length; i++) {
            for (int j = 0; j < pipePuzzle.length; j++) {
                int x = (i << 7) + 576;
                int y = (j << 7) + 156;

                pipePuzzle[i][j] = new Sprite((i * 128), (j * 128), images[i].getImage(), sprites[i].getTag());
                pipeRect[i][j] = new RECT(x, y, x + 128, y + 128, "pipeHover", new Frame(x, y, "boxOutline"));
            }
        }

        for (int i = 0; i < 3; i++)
            randomizeBoard();
    }

    // Methods
    private static void randomizeBoard() {
        Sprite temp;
        for (int i = 0; i < pipePuzzle.length; i++) {
            for (int j = 0; j < pipePuzzle.length; j++) {
                int r = Particle.rollDie(6) - 1;
                int c = Particle.rollDie(6) - 1;

                temp = pipePuzzle[i][j];
                pipePuzzle[i][j] = pipePuzzle[r][c];
                pipePuzzle[r][c] = temp;
            }
        }
    }

    public void drawPuzzle() {
        Point p = Mouse.getMouseCoords();

        Sprite pipeStart = new Sprite(448, 796, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipeEnd").getSprite(), 0.0), "start");
        Sprite pipeEnd = new Sprite(1344, 156, Graphic.rotateImageByDegrees(
                ctrl.getSpriteFromBackBuffer("pipeEnd").getSprite(), 180.0), "end");

        ctrl.addSpriteToFrontBuffer(pipeStart);
        ctrl.addSpriteToFrontBuffer(pipeEnd);


        for (int i = 0; i < pipePuzzle.length; i++) {
            for (int j = 0; j < pipePuzzle.length; j++) {
                int x = (i << 7) + 576;
                int y = (j << 7) + 156;

                pipePuzzle[i][j].moveXAbsolute(x);
                pipePuzzle[i][j].moveYAbsolute(y);
                ctrl.addSpriteToFrontBuffer(pipePuzzle[i][j]);

                drawGridOutline(i, j, x, y);

                if (pipeRect[i][j].isCollision(p.x, p.y)) {
                    ctrl.addSpriteToFrontBuffer(pipeRect[i][j].getGraphicalHover());
                }
                if (Control.getMouseInput() != null) {
                    if (pipeRect[i][j].isClicked(Control.getMouseInput(), Click.LEFT_BUTTON)) {
                        swapList.add(pipePuzzle[i][j]);
                        swapPoints.add(new Point(i, j));

                        s = "last clicked: (" + i + ", " + j + ")";

                        if (swapList.size() == 2)
                            swap();
                    }
                }
                ctrl.drawString(576, 146, s, Color.white);
            }
        }

        // TODO: 4/21/2023 find out why var solved never updates to true
        //  (issue between pipePuzzle[row][column].getSprite() being a sprite and images[k].getImage() being a buffered image???)
        //  (change pipePuzzle to DirectionBufferedImage type and change DirectionBufferedImage to use sprite instead of buffered image???)
        if (!solved) {
            isSolved();
            s2 = "puzzle not solved";
        } else
            s2 = "puzzle solved";

        ctrl.drawString(576, 126, s2, Color.white);
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
//        int row = 5;
//        int column = 0;
        System.out.println(row + " " + column);
//        DirectionBufferedImage previous = null;

//        while (row != 0 && column != 5 && !solved) {
        if (row != -1 || row != 6 || column != -1 || column != 6) {
            if (row == 5 && column == 0) {
                for (int k = 0; k < images.length; k++) {
                    if (pipePuzzle[row][column].getSprite() == images[k].getImage()) {
                        if (images[k].isLeftTop()) {
                            row--;
                            previous = images[k];
                            up = true;
                            down = false;
                            left = false;
                            right = false;
                        } else if (images[k].isLeftRight()) {
                            column++;
                            previous = images[k];
                            up = false;
                            down = false;
                            left = false;
                            right = true;
                        } else if (images[k].isLeftBottom()) {
                            row++;
                            previous = images[k];
                            up = false;
                            down = true;
                            left = false;
                            right = false;
                        }
                    }
                }
            } else if (row == 0 && column == 5) {
                for (int k = 0; k < images.length; k++) {
                    if (pipePuzzle[row][column].getSprite() == images[k].getImage()) {
                        if (previous.isBottomRight() && right) {
                            if (images[k].isLeftRight())
                                solved = true;
                            else
                                break;
                        }
                        if (previous.isLeftRight() && right) {
                            if (images[k].isLeftRight())
                                solved = true;
                            else
                                break;
                        }
                        if (previous.isTopBottom() && up) {
                            if (images[k].isTopBottom())
                                solved = true;
                            else
                                break;
                        }
                        if (previous.isLeftTop() && up) {
                            if (images[k].isTopBottom())
                                solved = true;
                            break;
                        }
                    }
                }
            } else {
                for (int k = 0; k < images.length; k++) {
                    if (pipePuzzle[row][column].getSprite() == images[k].getImage()) {
                        if (previous.isTopBottom() && up) {
                            if (images[k].isTopBottom()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isBottomRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isLeftBottom()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isTopBottom() && down) {
                            if (images[k].isTopBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftTop()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isLeftTop() && up) {
                            if (images[k].isTopBottom()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftBottom()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isBottomRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isLeftTop() && left) {
                            if (images[k].isBottomRight()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftRight()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                        }
                        if (previous.isTopRight() && right) {
                            if (images[k].isLeftRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isLeftTop()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                        }
                        if (previous.isTopRight() && up) {
                            if (images[k].isTopBottom()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isBottomRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isLeftBottom()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isBottomRight() && right) {
                            if (images[k].isLeftRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isLeftTop()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                        }
                        if (previous.isBottomRight() && down) {
                            if (images[k].isTopBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftTop()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isLeftRight() && left) {
                            if (images[k].isBottomRight()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftRight()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                        }
                        if (previous.isLeftRight() && right) {
                            if (images[k].isLeftRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                            if (images[k].isLeftTop()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                        }
                        if (previous.isLeftBottom() && down) {
                            if (images[k].isTopBottom()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftTop()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                column++;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = false;
                                right = true;
                            }
                        }
                        if (previous.isLeftBottom() && left) {
                            if (images[k].isBottomRight()) {
                                row++;
                                previous = images[k];
                                up = false;
                                down = true;
                                left = false;
                                right = false;
                            }
                            if (images[k].isTopRight()) {
                                row--;
                                previous = images[k];
                                up = true;
                                down = false;
                                left = false;
                                right = false;
                            }
                            if (images[k].isLeftRight()) {
                                column--;
                                previous = images[k];
                                up = false;
                                down = false;
                                left = true;
                                right = false;
                            }
                        }
                    }
                }
            }
        }

//        while (i != 0 && j != 5 && !solved) {
//
//            if (i == -1 || i == 6 || j == -1 || j == 6)
//                break;
//
//            if (i == 5 && j == 0) {
//                for (int k = 0; k < images.length; k++) {
//                    if (pipePuzzle[i][j].getSprite() == images[k].getImage() && images[k].isLeft()) {
//                        if (images[k].isTop()) {
//                            i--;
//                            for (int x = 0; x < images.length; x++) {
//                                if (pipePuzzle[i][j].getSprite() == images[x].getImage() && images[x].isBottom())
//
//                            }
//                        }
//                        if (images[k].isBottom())
//                            break;  // might need to fix possible issue not being able to swap sprites
//                        if (images[k].isRight()) {
//                            j++;
//                            break;
//                        }
//                    }
//                }
//            } else if (i == 0 && j == 5) {
//
//            } else {
//                for (int k = 0; k < images.length; k++) {
//                    if (pipePuzzle[i][j].getSprite() == images[k].getImage() && images[k].isLeft()) {
//                        if (images[k].isTop())
//                            i--;
//                        if (images[k].isBottom())
//                            i++;
//                        if (images[k].isRight())
//                            j++;
//                    }
//                    if (pipePuzzle[i][j].getSprite() == images)
//                }
//
//            }
//
//
//        }
    }
}

