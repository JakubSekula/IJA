package sample;

public class Coordinate {
    float x;
    float y;

    public Coordinate(float x, float y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        } else {
            this.x = x;
            this.y = y;
        }
    }
    /**
     * Vrací hodnotu souřadnice x.
     *
     * @return Souřadnice x.
     */
    public float getX() {
        return x;
    }

    /**
     * Vrací hodnotu souřadnice y.
     *
     * @return Souřadnice y.
     */
    public float getY() {
        return y;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        else {
            return (((Coordinate) obj).getX() == this.getX() && ((Coordinate) obj).getY() == this.getY());
        }
    }

    public static Coordinate create​(int x, int y) {
        return new Coordinate(x, y);
    }

    public float diffX(Coordinate c) {
        return x - c.getX();
    }

    public float diffY(Coordinate c) {
        return y - c.getY();
    }
}
