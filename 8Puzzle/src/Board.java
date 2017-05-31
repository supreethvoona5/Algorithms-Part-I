import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    14 May 2017
 *
 *  Description:    Immutable board object that holds an array of the 8
 *                  puzzle integers. Can return hamming and manhattan values
 *                  for A* searching.
 *
 *  Constraints:    int[][] blocks must be a n-by-n array with n >= 1.
 *
 *  Dependencies:   algs4.jar
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
public class Board implements Comparable<Board> {


    private final int[][] board;
    private int cachedManhattan = -1;


    /**
     * Constructs a board from a n-by-n array of blocks (where blocks[i][j] =
     * block in row i, column j)
     *
     * @param blocks array to be added to Board
     */
    Board(final int[][] blocks) {
        board = new int[blocks.length][blocks.length];
        for (int r = 0; r < blocks.length; r++) {
            System.arraycopy(blocks[r], 0, board[r], 0, blocks.length);
        }
    }

    /**
     * Unit testing
     *
     * @param args of command line
     */
    public static void main(String[] args) {

        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board b = new Board(blocks);

        System.out.println("b.hamming() = " + b.hamming());

    }

    /**
     * Returns dimension of the board, n
     *
     * @return int dimension of board
     */
    private int dimension() {
        return board.length;
    }

    /**
     * Returns the number of blocks out of place
     *
     * @return int of number of blocks out of place
     */
    private int hamming() {
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != 0 && board[row][col] != solveNumber(row, col)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the sum of Manhattan distances (sum of vertical and horizontal
     * distances that every block is away from where it should be).
     *
     * @return int of the sum of Manhattan distances
     */
    int manhattan() {

        if (cachedManhattan == -1) {
            cachedManhattan = 0;
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] != 0) {
                        int num = board[row][col];
                        int sCol = (num - 1) % (dimension());
                        int sRow = (num - 1) / (dimension());
                        cachedManhattan += Math.abs(sCol - col) + Math.abs(sRow - row);
                    }
                }
            }
        }

        return cachedManhattan;
    }

    /**
     * Calculates the number that should be in row, row, and column, col
     *
     * @param row of number
     * @param col of number
     * @return int of number that should be in (row, col)
     */
    private int solveNumber(int row, int col) {
        if (row == dimension() - 1 && col == dimension() - 1) {
            return 0; // returns 0 if it is the very last block
        }
        return (row * board[row].length) + (col + 1);
    }

    /**
     * Returns boolean of is this board the goal board? Essentially, is this
     * board solved?
     *
     * @return boolean answering "is this board the goal board?"
     */
    boolean isGoal() {
        return deepBoardEquals(this.board, goalBoard());
    }

    private boolean deepBoardEquals(int[][] check, int[][] goal) {
        for (int r = 0; r < dimension(); r++) {
            if (!Arrays.equals(check[r], goal[r])) {
                return false;
            }
        }
        return true;
    }

    private int[][] goalBoard() {
        int[][] array = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                array[i][j] = solveNumber(i, j);
            }
        }

        return array;
    }

    /**
     * returns if this board equals y
     *
     * @param y object to be compared
     * @return boolean of the board equaling y
     */
    public boolean equals(Object y) {

        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;

        Board yBoard = (Board) y;

        return Arrays.deepEquals(board, yBoard.board);

    }

    /**
     * Returns a board that is obtained by exchanging any pair of blocks
     *
     * @return Board that is a twin of current
     */
    Board twin() {

        int[][] diffBlocks = new int[dimension()][dimension()];
        for (int r = 0; r < diffBlocks.length; r++) {
            System.arraycopy(board[r], 0, diffBlocks[r], 0, diffBlocks.length);
        }

        for (int[] diffBlock : diffBlocks) {
            if (replaceTwo(diffBlock)) {
                break;
            }
        }

        return new Board(diffBlocks);

    }

    private boolean replaceTwo(int[] diffBlock) {
        for (int c = 0; c < diffBlock.length - 1; c++) {
            if (diffBlock[c] != 0 && diffBlock[c + 1] != 0) {
                int first = diffBlock[c];
                diffBlock[c] = diffBlock[c + 1];
                diffBlock[c + 1] = first;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns iterable of all neighboring boards.
     *
     * @return Iterable of all neighboring boards.
     */
    Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int row = 0;
        int col = 0;
        boolean found0 = false;
        for (int r = 0; r < dimension(); r++) {
            for (int c = 0; c < dimension(); c++) {
                if (board[r][c] == 0) {
                    row = r;
                    col = c;
                    found0 = true;
                }
                if (found0) break;
            }
            if (found0) break;
        }

        int[][] tempBoard;

        if (row > 0) {
            // swap up
            tempBoard = deepCopy(board);
            int other = tempBoard[row - 1][col];
            tempBoard[row - 1][col] = tempBoard[row][col];
            tempBoard[row][col] = other;
            neighbors.add(new Board(tempBoard));
        }
        if (row < dimension() - 1) {
            // swap down
            tempBoard = deepCopy(board);
            int other = tempBoard[row + 1][col];
            tempBoard[row + 1][col] = tempBoard[row][col];
            tempBoard[row][col] = other;
            neighbors.add(new Board(tempBoard));
        }
        if (col > 0) {
            // swap left
            tempBoard = deepCopy(board);
            int other = tempBoard[row][col - 1];
            tempBoard[row][col - 1] = tempBoard[row][col];
            tempBoard[row][col] = other;
            neighbors.add(new Board(tempBoard));

        }
        if (col < dimension() - 1) {
            // swap right
            tempBoard = deepCopy(board);
            int other = tempBoard[row][col + 1];
            tempBoard[row][col + 1] = tempBoard[row][col];
            tempBoard[row][col] = other;
            neighbors.add(new Board(tempBoard));
        }
        return neighbors;
    }


    /**
     * returns a deep copy of a 2d array
     *
     * @param blocks to be copied
     * @return a new copy of board
     */
    private int[][] deepCopy(int[][] blocks) {
        int[][] ret = new int[dimension()][dimension()];
        for (int r = 0; r < dimension(); r++) {
            System.arraycopy(blocks[r], 0, ret[r], 0, dimension());
        }
        return ret;
    }

    /**
     * Returns a string representation of this board (in the output format
     * specified below)
     *
     * @return String representation of board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension()).append("\n");
        for (int r = 0; r < dimension(); r++) {
            for (int c = 0; c < dimension(); c++) {
                s.append(String.format("%2d ", board[r][c]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public int compareTo(Board o) {
        if (this.manhattan() == o.manhattan()) {
            for (int r = 0; r < dimension(); r++) {
                for (int c = 0; c < dimension(); c++) {
                    if (this.board[r][c] != o.board[r][c]) {
                        return this.board[r][c] - o.board[r][c];
                    }
                }
            }
        }
        return this.manhattan() - o.manhattan();
    }
}
