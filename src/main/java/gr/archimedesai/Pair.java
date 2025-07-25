package gr.archimedesai;

public class Pair {

    private final double x;
    private final double y;

    private Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public static Pair newPair(double x, double y) {
        return new Pair(x, y);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
