import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class KdTree {

    private static final boolean IS_VERTICAL = true;

    private int size = 0;
    private Node root;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // adds the point to the set (if it is not already in the set)
    public void insert(Point2D point2D) {
        if (root == null) {
            root = new Node(point2D, IS_VERTICAL);
            root.rect = new RectHV(0, 0, 1, 1);
            size = 1;
            return;
        }
        root = put(root, point2D, 0, 0, 1, 1, root.isVertical);
    }

    // using a root, will put the point down the node
    // the Node that will be created has the isVertical condition specified in
    // the isVertical parameter, however in recursions, each successive level
    // has the opposite isVertical condition to alternate between vertical and
    // horizontal cutting
    private Node put(Node node, Point2D point2D, double x0, double y0,
                     double xf, double yf, boolean isVertical) {

        // if reached a null link, will create a new node with the point and the
        // vertical condition specified
        if (node == null) {
            Node p = new Node(point2D, isVertical);
            p.rect = new RectHV(x0, y0, xf, yf);
            size++;
            return p;
        }

        // if this value equals its node, then it should keep this node the same
        // and not change its subtrees
        if (node.p.equals(point2D)) return node;

        double compare;
        if (isVertical) {
            // if the line is vertical, compare the x-coordinates.
            compare = point2D.x() - node.p.x();

            // if compare is less than 0, the lb will be set to the put of this
            if (compare < 0) {
                node.lb = put(node.lb, point2D, x0, y0, node.p.x(),
                        yf, false);
            }

            // otherwise, it is greater than or equal to 0, and since we already
            // checked to make sure that this value isn't the same as the node's
            // value, it will not create a repeat since the previous value of
            // the same coordinates would've gone through the exact same tree.
            // also by the specifications, if compare >= 0, then it will be set
            // to go down the right tree.
            else {
                node.rt = put(node.rt, point2D, node.p.x(), y0, xf,
                        yf, false);
            }
        }
        else {
            // otherwise, the line is horizontal, so compare the y-coordinates.
            compare = point2D.y() - node.p.y();
            if (compare < 0) {
                node.lb = put(node.lb, point2D, x0, y0, xf, node.p.y(),
                        true);
            }
            else {
                node.rt = put(node.rt, point2D, x0, node.p.y(), xf, yf,
                        true);
            }
        }

        return node;

    }

    // does the set contain point point2D
    public boolean contains(Point2D point2D) {
        if (point2D == null) throw new NullPointerException();
        return root != null && getPoint(root, point2D);
    }

    private boolean getPoint(Node x, Point2D point2D) {

        // if we have reached a null-link, then the search is terminates because
        // there was no search hit following the same path
        if (x == null) return false;

        // first check if the value at x equals the point, if it is, then it is
        // contained
        if (x.p.equals(point2D)) return true;

        double compareTo;
        if (x.isVertical) {
            // if the separation line is vertical, then we need to compare the
            // x-coordinates of the two points.
            compareTo = point2D.x() - x.p.x();
        }
        else {
            // otherwise, the separation line is horizontal; thus, we must
            // compare the y-coordinates of the two points.
            compareTo = point2D.y() - x.p.y();
        }

        // if compareTo is negative, then we need to search down the left tree
        if (compareTo < 0) {
            return getPoint(x.lb, point2D);
        }
        // otherwise, we need to search down the right tree
        else {
            return getPoint(x.rt, point2D);
        }

    }

    // draw all the points to standard draw
    public void draw() {
        drawSubtrees(root);
    }

    private void drawSubtrees(Node root) {

        if (root == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        root.p.draw();

        // if the root is a vertical line, draw a vertical line
        if (root.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(root.p.x(), root.rect.ymin(), root.p.x(),
                    root.rect.ymax());
        }
        else { // otherwise, draw a horizontal line from the max and min values
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(root.rect.xmin(), root.p.y(), root.rect.xmax(),
                    root.p.y());
        }
        drawSubtrees(root.lb);
        drawSubtrees(root.rt);
    }

    // all the points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rectHV) {
        if (rectHV == null) throw new NullPointerException();
        ArrayList<Point2D> arr = new ArrayList<>();
        getPointsInRange(root, arr, rectHV);
        return arr;
    }

    private void getPointsInRange(Node root, ArrayList<Point2D> arr, RectHV rectHV) {
        if (root == null || !root.rect.intersects(rectHV)) return;
        if (rectHV.contains(root.p)) arr.add(root.p);
        getPointsInRange(root.lb, arr, rectHV);
        getPointsInRange(root.rt, arr, rectHV);
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point2D) {
        if (point2D == null) throw new NullPointerException();
        return getNearestPoint(root, point2D, root.p);
    }

    private Point2D getNearestPoint(Node root, Point2D find, Point2D closest) {

        // if it has reached a null-link, return the closest.
        if (root == null)
            return closest;

        // if the current point is closer than the closest, then we make it the
        // closest point
        Point2D ret = closest;
        if (Math.abs(root.p.distanceTo(find))
                < Math.abs(ret.distanceTo(find))) {
            ret = root.p;
        }

        // if the rectangle distance is closer than the closest point, then we
        // need to check its subtrees
        if (Math.abs(root.rect.distanceTo(find))
                < Math.abs(closest.distanceTo(find))) {

            Node close;
            Node far;

            // we need to find which side is closer to the point
            if ((root.isVertical && find.x() < root.p.x())
                    || (!root.isVertical && find.y() < root.p.y())) {
                close = root.lb;
                far = root.rt;
            } else {
                close = root.rt;
                far = root.lb;
            }

            ret = getNearestPoint(close, find, ret);
            ret = getNearestPoint(far, find, ret);

        }

        return ret;
    }


    // unit testing of methods
    public static void main(String[] args) {

    }

    private static class Node {

        private boolean isVertical;

        private Node prev;

        // the point
        private Point2D p;

        // the axis-aligned rectangle corresponding to thise node
        private RectHV rect;

        // the left/bottom subtree
        private Node lb;

        // the right/top subtree
        private Node rt;

        public Node(Point2D p, boolean isVertical) {
            this.isVertical = isVertical;
            this.p = p;
        }
    }


}