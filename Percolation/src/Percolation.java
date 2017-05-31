import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    23 April 2017
 *
 *  Description:    Modeling and simulating percolation with an N-by-N grid
 *                  using a Weighted Quick-Union Data Structure.
 *
 *  Compilation:    javac-algs4 Percolation.java
 *  Execution:      java-algs4 Percolation
 *  Dependencies:   algs4.jar
 *  Package:        default
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
public class Percolation {

    /**
     * stores length of grid
     * <p>
     * grid has n * n number of spaces
     */
    private int n;

    /**
     * stores which sites are open
     * <p>
     * array has n * n + 2 number of spaces
     *      openSite[n * n] = top virtual site
     *      openSite[n * n + 1] = bottom virtual site
     */
    private boolean[] openSite;

    /**
     * stores if index is connected to top
     */
    private boolean[] isTop;

    /**
     * stores if index is connected to bottom
     */
    private boolean[] isBottom;

    /**
     * stores which sites are connected
     */
    private WeightedQuickUnionUF wquf;

    /**
    * does the system percolate?
    */
    private boolean percolatable;

    /**
     * create n-by-n grid, with all sites blocked
     *
     * @param n of n-by-n grid with all sites blocked
     */
    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        // 1) check validity of n
        if (n < 1) {
            throw new IllegalArgumentException("n must be >= 1");
        }

        // 2) create new objects and set instance variables
        wquf = new WeightedQuickUnionUF(n * n);
        this.n = n;
        openSite = new boolean[n * n];
        isTop = new boolean[n * n];
        isBottom = new boolean[n * n];

        // 3) set every value of boolean arrays to false
        for (int i = 0; i < openSite.length; i++) {
            openSite[i] = false;
            isTop[i] = false;
            isBottom[i] = false;
        }

        // 4) set percolatable to false
        percolatable = false;
    }

    /**
     * opens site (row, col) and takes appropriate secondary actions.
     * <p>
     * Opens site (row, col), sets v
     *
     * @param row row of sute
     * @param col col of site
     */
    public void open(int row, int col) {
        // 1) check validity of site (row, col)
        checkValid(row, col);

        // 2) store 1D index of 2D point
        int oneD = xyTo1D(row, col);

        // 3) mark index oneD as open in boolean array
        openSite[oneD] = true;

//        // 4) if on top or bottom, connect to top and bottom virtual site, respectively
//        if (row == 1)
//            union(oneD, n * n);
//        if (row == n)
//            union(oneD, n * n + 1);

        // 4) connect to adjacent sites
        //      also set boolean of connectedTop and connectedBottom
        boolean connectedTop = false;
        boolean connectedBottom = false;

        // if adjacent root is connected to top or bottom, will set connectedTop and
        // connectedBottom = true, respectively

        // up/down
        if (row > 1 && isOpen(row - 1, col)) { // connect up

            if (isTop[find(row - 1, col)] || isTop[find(row, col)])
                connectedTop = true;
            if (isBottom[find(row - 1, col)] || isBottom[find(row, col)]) {
                connectedBottom = true;
            }

            union(oneD, xyTo1D(row - 1, col));
        }
        if (row < n && isOpen(row + 1, col)) { // connect down

            if (isTop[find(row + 1, col)] || isTop[find(row, col)])
                connectedTop = true;
            if (isBottom[find(row + 1, col)] || isBottom[find(row, col)]) {
                connectedBottom = true;
            }

            union(oneD, xyTo1D(row + 1, col));
        }

        // left/right
        if (col > 1 && isOpen(row, col - 1)) { // connect left

            if (isTop[find(row, col - 1)] || isTop[find(row, col)])
                connectedTop = true;
            if (isBottom[find(row, col - 1)] || isBottom[find(row, col)])
                connectedBottom = true;

            union(oneD, xyTo1D(row, col - 1));
        }
        if (col < n && isOpen(row, col + 1)) { // connect right

            if (isTop[find(row, col + 1)] || isTop[find(row, col)])
                connectedTop = true;
            if (isBottom[find(row, col + 1)] || isBottom[find(row, col)])
                connectedBottom = true;

            union(oneD, xyTo1D(row, col + 1));
        }

        // 5) if in either top or bottom row, will set isTop and isBottom = true
        if (row == 1)
            connectedTop = true;
        if (row == n)
            connectedBottom = true;

        // 6) change boolean of root if this site is connected to top or bottom
        isTop[find(row, col)] = connectedTop;
        isBottom[find(row, col)] = connectedBottom;

        // 7) if this root is connected to both top and bottom, the system percolates
        if (isBottom[find(row, col)] && isTop[find(row, col)])
            percolatable = true;

        // END! FINALLY!
    }

    /**
     * is site (row, col) open?
     *
     * @param row to check
     * @param col to check
     * @return boolean
     */
    public boolean isOpen(int row, int col) {
        // 1) Check validity of row, col
        checkValid(row, col);

        // 2) Return if (row, col) is open
        return openSite[xyTo1D(row, col)];
    }

    /**
     * is site (row, col) full?
     * <p>
     * Checks if the site (row, col) is connected to the top directly, not
     * through the virtual bottom site.
     *
     * @param row to check
     * @param col to check
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        // 1) check validity of row, col
        checkValid(row, col);

        // 2) return "is site (row, col) connected to top virtual site?"
        return isTop[find(row, col)];
    }

    /**
     * returns number of open sites
     *
     * @return int
     */
    public int numberOfOpenSites() {
        int count = 0;
        for (boolean anOpenSite : openSite) {
            if (anOpenSite) count++;
        }
        return count;
    }

    /**
     * does the system percolate?
     * <p>
     * is the top virtual site connected to the bottom virtual site?
     *
     * @return boolean
     */
    public boolean percolates() {

        // 1) return "is top virtual site connected to bottom virtual site?"
//        return wquf.connected(n * n, n * n + 1);
        return percolatable;
    }

    /**
     * returns one-dimensional array point for two-dimensional set of points
     * <p>
     * assumes formula for x and y starts at (1, 1) not (0, 0)
     * returns value between 0 (inclusive) and n * n (exclusive)
     *
     * @param x 1 <= x <= n
     * @param y 1 <= y <= n
     * @return int
     */
    private int xyTo1D(int x, int y) {
        checkValid(x, y);
        return (n * (x - 1)) + (y - 1);
    }

    /**
     * unions i & j in wquf object
     *
     * @param i index to union with j
     * @param j index to union with i
     */
    private void union(int i, int j) {
        wquf.union(i, j);
    }

    /**
     * throws IndexOutOfBoundsException if x or y are out of bounds
     * <p>
     * Two conditions must be met:
     *  1) 1 <= x <= n
     *  2) 1 <= y <= n
     * @param x to check if valid
     * @param y to check if valid
     */
    private void checkValid(int x, int y) {
        if (x < 1 || y < 1 || x > n || y > n) {
            throw new IndexOutOfBoundsException("x and y must be between"
                + " 1 and " + n + " (inclusive)");
        }
    }

    /**
     * returns int: wquf.find( site at (x, y) )
     *
     * @param x index to find site
     * @param y index to find site
     * @return int
     */
    private int find(int x, int y) {
        checkValid(x, y);
        return wquf.find(xyTo1D(x, y));
    }

    /**
     * testing client for Percolation
     *
     * @param args command-line args
     */
    public static void main(String[] args) {
    }

}
