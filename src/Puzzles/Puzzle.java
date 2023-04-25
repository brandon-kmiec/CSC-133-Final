package Puzzles;

// TODO: 4/23/2023 delete this class? unsure of how to generalize puzzles for levels because of different puzzles

public class Puzzle {
    // Fields
    private static boolean solved;
    private static PipePuzzle pipePuzzle;

    // Constructor
    public Puzzle(){
        solved = false;

    }

    // Methods
    public static void pipePuzzleSolved(){
//        if (pipePuzzle.solved)
            solved = true;
    }
}
