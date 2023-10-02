package byow.Core;

public class Position implements Comparable<Position> {
    public int xp;
    public int yp;

    public Position(int x, int y) {
        xp = x;
        yp = y;
    }

    @Override
    public int compareTo(Position p) {
        if (this.xp == p.xp) {
            return Integer.compare(this.yp, p.yp);
        }
        return Integer.compare(this.xp, p.xp);
    }
    public Position plus(Position p) {
        return new Position(this.xp + p.xp, this.yp + p.yp);
    }
}

