import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (that == null) {
            throw new NullPointerException();
        }
        if (this.doesEqual(that)) {
            // if the two points are equal, degenerate points
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            // if the x-coordinate of two points are equal, they
            // have a vertical line
            return Double.POSITIVE_INFINITY;
        }
        // otherwise, use slope formula
        double ret = ((double) (that.y - this.y)) / ((double) (that.x - this.x));
        if (ret == -0.0) {
            ret = 0.0;
        }
        return ret;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) // if y = y, show diff of x
            return this.x - that.x;
        return this.y - that.y; // if y coordinates aren't equal, diff of y
    }



    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return (o1, o2) -> {
            double slopeDiff = Point.this.slopeTo(o1) - Point.this.slopeTo(o2);
            if (slopeDiff < 0)
                return -1;
            if (slopeDiff > 0)
                return 1;
            return 0;
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p = new Point(47, 47);
        Point q = new Point(198, 47);
        System.out.println("p.slopeTo(q) = " + p.slopeTo(q));
    }

    /**
     * Boolean statement if this point doesEqual argument point in both x and y
     *
     * @param that Point to be compared to
     * @return boolean true if the two points are equal, false if they are
     * not equal.
     */
    private boolean doesEqual(Point that) {
        return (this.x == that.x && this.y == that.y);
    }

}