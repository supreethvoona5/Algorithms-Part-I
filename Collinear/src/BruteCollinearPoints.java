import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    07 May 2017
 *
 *  Description:    [description of program]
 *
 *  Compilation:    [compilation lines]
 *  Execution:      [execute lines]
 *  Dependencies:   [dependencies]
 *  Package:        PACKAGE_NAME
 *
 *  Copyright 2017 Aditya Pillai
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a 
 *  copy of this software and associated documentation files (the "Software"), 
 *  to deal in the Software without restriction, including without limitation 
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 *  and/or sell copies of the Software, and to permit persons to whom the 
 *  Software is furnished to do so, subject to the following conditions:
 *  The above copyright notice and this permission notice shall be included in 
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 *  IN THE SOFTWARE.
 ******************************************************************************/
@SuppressWarnings({"unused", "WeakerAccess"})
public class BruteCollinearPoints {

    private LineSegment[] segments;

    /**
     * Initializes a BruteCollinearPoints object to find all line segments
     * containing 4 points.
     *
     * @param argPoints array of points to be added to object.
     */
    public BruteCollinearPoints(Point[] argPoints) {
        if (argPoints == null) throw new NullPointerException("Empty argument");
        Point[] points = Arrays.copyOf(argPoints, argPoints.length);
        checkDuplicatedItems(points);

        ArrayList<LineSegment> arr = new ArrayList<>();
        Arrays.sort(points);


        // now for the mother of all iteration
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slopeij = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    double slopeik = points[i].slopeTo(points[k]);
                    if (slopeij == slopeik) {
                        for (int m = k + 1; m < points.length; m++) {
                            double slopeim = points[i].slopeTo(points[m]);
                            if (slopeij == slopeim) {
                                arr.add(new
                                    LineSegment(points[i], points[m]));
                            }
                        }
                    }
                }
            }
        }

        segments = arr.toArray(new LineSegment[arr.size()]);
    }

    private void checkDuplicatedItems(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null) {
                    throw new NullPointerException();
                }
                if (points[i].equals(points[j]))
                    throw new IllegalArgumentException("two equal points.");

            }
        }
    }

    /**
     * returns number of line segments in set of points
     * @return int of number of LineSegment objects
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * Returns array of all line segments in object by brute-force.
     *
     * @return LineSegment[] array of all LineSegment objects.
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
    /**
     * Visualizing and unit-testing
     * @param args from command line.
     *             int n, followed by n pairs of ints (x, y), each between
     *             0 and 32,767
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        long first = System.currentTimeMillis();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        System.out.println(System.currentTimeMillis() - first);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

