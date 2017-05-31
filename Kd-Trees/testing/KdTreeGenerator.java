/******************************************************************************
 *  Compilation:  javac KdTreeGenerator.java
 *  Execution:    java KdTreeGenerator n
 *  Dependencies: 
 *
 *  Creates n random points in the unit square and print to standard output.
 *
 *  % java KdTreeGenerator 5
 *  0.195080 0.938777
 *  0.351415 0.017802
 *  0.556719 0.841373
 *  0.183384 0.636701
 *  0.649952 0.237188
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.concurrent.TimeUnit;

public class KdTreeGenerator {

    public static void main(String[] args) throws InterruptedException {
        int n = Integer.parseInt(args[0]);
        PointSET pointSET = new PointSET();
        KdTree kdTree = new KdTree();
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            pointSET.insert(new Point2D(x, y));
            kdTree.insert(new Point2D(x, y));
        }
        pointSET.draw();
        TimeUnit.SECONDS.sleep(20);
        kdTree.draw();
    }
}
