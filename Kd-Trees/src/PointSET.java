import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by aditp on 5/24/2017.
 * PointSET object represents a set of points in the unit square.
 * Uses a Tree-Set Red-Black BST.
 */
public class PointSET {


    private TreeSet<Point2D> points;

    // constructs an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert (Point2D point2D) {
        if (point2D == null) throw new NullPointerException();
//        if (root == null) {
//            root = point2D;
//            minX = point2D.x();
//            maxX = point2D.x();
//            minY = point2D.y();
//            maxY = point2D.y();
//        }
        if (!points.contains(point2D)) {
//            updateMinMax(point2D);
            points.add(point2D);
        }
    }

//    private void updateMinMax(Point2D point2D) {
//        double y = point2D.y();
//        if (y < minY) {
//            minY = y;
//        }
//        if (y > maxY) {
//            maxY = y;
//        }

//        double x = point2D.x();
//        if (x < minX) {
//            minX = x;
//        }
//        if (x > maxX) {
//            maxX = x;
//        }
//    }

    // does the set contain point p
    public boolean contains(Point2D point2D) {
        if (point2D == null) throw new NullPointerException();
        return points.contains(point2D);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rectHV) {
        LinkedList<Point2D> ret = new LinkedList<>();
        if (rectHV == null) throw new NullPointerException();
        for (Point2D p : points) {
            if (rectHV.contains(p)) {
                ret.add(p);
            }
        }
        return ret;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point2D) {
        if (point2D == null) throw new NullPointerException();
        double minDistance = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D p : points) {
            if (Math.abs(p.distanceTo(point2D)) < minDistance) {
                minDistance = Math.abs(p.distanceTo(point2D));
                nearest = p;
            }
        }
        return nearest;
    }

    // unit testing
    public static void main(String[] args) {

    }

}
