import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    20 April 2017
 *
 *  Description:    Performing statistical analysis on percolation problem
 *                  with a [n]-by-[n] grid and [trials] number of trials to
 *                  find mean/average probability, standard deviation, and a
 *                  95% confidence interval.
 *
 *  Compilation:    javac-algs4 PercolationStats.java
 *  Execution:      java-algs4 PercolationStats [n] [trials]
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
@SuppressWarnings("WeakerAccess")
public class PercolationStats {

    private final int n;
    private double[] trialFractions;

    public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
        // check validity of trials
        if (trials < 1) {
            throw new IllegalArgumentException("trials must be > 0, was " + trials);
        }
        this.n = n;

        trialFractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            conductExperiment(i);
        }
    }

    public static void main(String[] args) { // test client (described below)

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats p = new PercolationStats(n, trials);
        StdOut.println("mean\t\t\t\t\t= " + p.mean());
        StdOut.println("stdde:\t\t\t\t\t= " + p.stddev());
        StdOut.println("95% confidence interval\t= [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");
    }

    private void conductExperiment(int i) {
        Percolation p = new Percolation(n);
        int count = 0;
        while (!p.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!p.isOpen(row, col)) {
                p.open(row, col);
                count++;
            }
        }
        trialFractions[i] = ((double) count) / ((double) (n * n));
    }

    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(trialFractions);
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(trialFractions);
    }

    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return mean() - (1.96 * stddev()) / Math.sqrt(trialFractions.length);
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        return mean() + ((1.96 * stddev())) / Math.sqrt(trialFractions.length);
    }
}