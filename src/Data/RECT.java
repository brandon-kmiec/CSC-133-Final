package Data;

public class RECT {
    // Fields
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private String tag;


    //Constructor
    public RECT(int x1, int y1, int x2, int y2, String tag) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.tag = tag;
    }


    public String getTag() {
        return tag;
    }


    public boolean isCollision(int x, int y) {
        if (x >= x1 && x <= x2)
            if (y >= y1 && y <= y2)
                return true;
        return false;
    }


    public boolean isClicked(Click c, int buttonComparator) {
        if (c.getButton() != buttonComparator)
            return false;   // Not our button
        return isCollision(c.getX(), c.getY());
    }
}
