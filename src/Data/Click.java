package Data;

public class Click {
    // Fields
    private int x;
    private int y;
    private int button;

    // Constants
    public static final int LEFT_BUTTON = 1;
    public static final int MIDDLE_BUTTON = 2;
    public static final int RIGHT_BUTTON = 3;
    public static final int BACK_BUTTON = 4;
    public static final int FORWARD_BUTTON = 5;


    public Click(int x, int y, int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getButton() {
        return button;
    }


    @Override
    public String toString() {
        return "Click{" +
                "x=" + x +
                ", y=" + y +
                ", button=" + button +
                '}';
    }
}
