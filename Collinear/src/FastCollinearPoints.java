import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

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
@SuppressWarnings({"WeakerAccess", "unused"})
public class FastCollinearPoints {

    private ArrayList<LineSegment> segments = new ArrayList<>();
    private HashMap<Double, List<Point>> foundSegments = new HashMap<>();

    /**
     * creates Object that has segments of collinear points
     * constructor finds collinear points and segments accessed with segments()
     *
     * @param points array of points to be added
     */
    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        // iterate through each point
        for (Point point : points) {
            Arrays.sort(pointsCopy, point.slopeOrder());
            ArrayList<Point> sameSlopePoints = new ArrayList<>();

            for (int i = 1; i < pointsCopy.length; i++) {
                sameSlopePoints.add(point);
                sameSlopePoints.add(pointsCopy[i]);
                double slope = point.slopeTo(pointsCopy[i]);
                int next = i + 1;
                while (next < pointsCopy.length
                    && slope == point.slopeTo(pointsCopy[next])) {
                    sameSlopePoints.add(pointsCopy[next]);
                    next++;
                }
                if (sameSlopePoints.size() >= 4) {
                    Collections.sort(sameSlopePoints);
                    addIfNew(sameSlopePoints, slope);
                }
                sameSlopePoints.clear();
            }
        }
    }

    private void addIfNew(ArrayList<Point> slopePoints, double slope) {
        List<Point> endPoints = foundSegments.get(slope);
        Collections.sort(slopePoints);

        Point startPoint = slopePoints.get(0);
        Point endPoint = slopePoints.get(slopePoints.size() - 1);

        if (endPoints == null) {
            endPoints = new ArrayList<>();
            endPoints.add(endPoint);
            foundSegments.put(slope, endPoints);
            segments.add(new LineSegment(startPoint, endPoint));
        }
        else {
            for (Point currentEndPoint : endPoints) {
                if (currentEndPoint.compareTo(endPoint) == 0) {
                    return;
                }
            }
            endPoints.add(endPoint);
            segments.add(new LineSegment(startPoint, endPoint));
        }
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new NullPointerException();
            }
        }
    }

    /**
     * returns number of line segments that are collinear
     *
     * @return int of number of collinear line segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * returns array of line segments that are collinear
     *
     * @return LineSegment[] of linesegments
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
