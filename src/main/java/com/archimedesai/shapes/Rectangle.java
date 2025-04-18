package com.archimedesai.shapes;

public class Rectangle {

    private final Point lowerBound;
    private final Point upperBound;

    private Rectangle(Point lowerBound, Point upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static Rectangle newRectangle(Point lowerBound, Point upperBound){
        return new Rectangle(lowerBound, upperBound);
    }

    public Point getLowerBound() {
        return lowerBound;
    }

    public Point getUpperBound() {
        return upperBound;
    }

    public Point getLowerRightBound(){
        return Point.newPoint(upperBound.getX(), lowerBound.getY());
    }

    public Point getUpperLeftBound(){
        return Point.newPoint(lowerBound.getX(), upperBound.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (!lowerBound.equals(rectangle.lowerBound)) return false;
        return upperBound.equals(rectangle.upperBound);
    }

    @Override
    public int hashCode() {
        int result = lowerBound.hashCode();
        result = 31 * result + upperBound.hashCode();
        return result;
    }

    public String toString(){
        return "Rectangle [("+lowerBound.getX()+", "+lowerBound.getY()+"), ("+upperBound.getX()+", "+upperBound.getY()+")]";
    }

    public boolean contains(Point point){
        if(point.getX() >= lowerBound.getX() && point.getY() >= lowerBound.getY() &&
                point.getX() < upperBound.getX() && point.getY() < upperBound.getY()){
            return true;
        }
        return false;
    }

    public boolean intersectsWith(Rectangle rectangle){
        return (Double.compare(this.getLowerBound().getX(), rectangle.getUpperBound().getX()) == -1) &&
                (Double.compare(this.getUpperBound().getX(), rectangle.getLowerBound().getX()) != -1) &&
                (Double.compare(this.getLowerBound().getY(), rectangle.getUpperBound().getY()) == -1) &&
                (Double.compare(this.getUpperBound().getY(), rectangle.getLowerBound().getY()) != -1);
    }


}
