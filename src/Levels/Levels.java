package Levels;

import Data.Animation;
import Data.RECT;
import Data.Sprite;

// TODO: 4/23/2023 delete this class? unsure of how to generalize levels because of different puzzles

public class Levels {
    // Fields
    private  RECT nextLevelDoor, prevLevelDoor;
    private  RECT itemRect;
    private  Sprite itemSprite, backgroundSprite;
    private  Animation doorOpen;
//    private static Puzzle puzzle;

    // Constructor
    public Levels(RECT nextLevelDoor, RECT prevLevelDoor, RECT itemRect, Sprite itemSprite, Sprite backgroundSprite, Animation doorOpen) {
        this.nextLevelDoor = nextLevelDoor;
        this.prevLevelDoor = prevLevelDoor;
        this.itemRect = itemRect;
        this.itemSprite = itemSprite;
        this.backgroundSprite = backgroundSprite;
        this.doorOpen = doorOpen;
    }

}
