import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    14 May 2017
 *
 *  Description:    Finds the fastest solution to a puzzle board of n-by-n
 *                  size using A* pathfinding algorithm.
 *
 *  Compilation:    javac Solver.java
 *  Execution:      java Solver inputPuzzle.txt
 *
 *  Input:          Utilizes command line argument of puzzle file with the
 *                  first line having integer n, which is the length of the
 *                  puzzle board, then followed by a n-by-n array of integers
 *                  separated by spaces. For example,
 *
 *                  3
 *                  1 2 3
 *                  4 5 6
 *                  7 8 0
 *
 *  Constraints:    The array following n must be a n-by-n array with n >= 1.
 *  Output:         If there is no solution, outputs "No solution possible,"
 *                  otherwise, outputs "Minimum number of moves = (min number
 *                  of moves)" followed by a string representation of every
 *                  board to get to the solution.
 *
 *  Dependencies:   Board.java
 *                  algs4.jar
 *  Package:        None
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
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
public class Solver {

    private final ArrayList<Board> solution = new ArrayList<>();
    private boolean isSolvable = false;

    /*
     * find a solution to the initial board (using the A* algorithm)
     */
    Solver(Board initial) {


        SearchNode sn = new SearchNode(initial, null);
        SearchNode snTw = new SearchNode(initial.twin(), null);

        if (sn.board.isGoal()) {
            isSolvable = true;
            solution.add(sn.board);
            return;
        }
        if (snTw.board.isGoal()) {
            isSolvable = false;
            return;
        }

        MinPQ<SearchNode> minPQ = new MinPQ<>();
        MinPQ<SearchNode> minPQTw = new MinPQ<>();

        minPQ.insert(sn);
        minPQTw.insert(snTw);

        TreeSet<Board> added = new TreeSet<>();
        TreeSet<Board> addedTw = new TreeSet<>();

        added.add(sn.board);
        addedTw.add(snTw.board);

        while (!(sn.board.isGoal() || snTw.board.isGoal())) {

            sn = minPQ.delMin();
            snTw = minPQTw.delMin();

            for (Board b : sn.board.neighbors()) {
                SearchNode newSN = new SearchNode(b, sn);
                if (!added.contains(newSN.board)) {
                    minPQ.insert(newSN);
                    added.add(newSN.board);
                }
            }

            for (Board bTw : snTw.board.neighbors()) {
                SearchNode newSNTw = new SearchNode(bTw, snTw);
                if (!addedTw.contains(newSNTw.board)) {
                    minPQTw.insert(newSNTw);
                    addedTw.add(newSNTw.board);
                }
            }

        }

        if (snTw.board.isGoal()) {
            isSolvable = false;
            return;
        }

        isSolvable = true;
        while (sn != null) {
            solution.add(0, sn.board);
            sn = sn.previous;
        }

    }

    /*
     * solve a slider puzzle (given below)
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            Iterable<Board> solutions = solver.solution();
            assert solutions != null;
            for (Board board : solutions) {
                StdOut.println(board);
            }
        }
    }

    /*
     * is the initial board solvable?
     */
    private boolean isSolvable() {
        return isSolvable;
    }

    /*
     * min number of moves to solve initial board; -1 if no solution
     */
    int moves() {
        if (isSolvable) {
            return solution.size() - 1;
        }
        else {
            return -1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if no solution
     */
    private Iterable<Board> solution() {
        if (isSolvable) {
            return solution;
        }
        else {
            return null;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private int moves;
        private SearchNode previous;
        private int cachedPriority = -1;

        SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            if (previous == null)
                moves = 0;
            else
                moves = 1 + previous.moves;
        }

        private int priority() {
            if (cachedPriority == -1) {
                cachedPriority = moves + board.manhattan();
            }
            return cachedPriority;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority() < that.priority()) {
                return -1;
            }
            if (this.priority() > that.priority()) {
                return +1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SearchNode that = (SearchNode) o;

            return board.equals(that.board);
        }

    }
}
