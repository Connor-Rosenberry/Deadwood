public class Take {
    // VARIABLES
    private int number;
    private int x;
    private int y;
    private int w;
    private int h;

    // CONSTRUCTOR
    public Take() {
        this.number = 0;
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }

    public Take(int number, int x, int y, int w, int h) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // GETS/SETS
    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    // METHODS
}
