import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Board {
  private int nSize;
  private int[][] board;
  private int[] zeroPosition;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    nSize = tiles.length;

    board = new int[nSize][nSize];

    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        board[i][j] = tiles[i][j];
        if (tiles[i][j] == 0) {
          zeroPosition = new int[]{i, j};
        }
      }
    }
  }

  // string representation of this board
  // 1st: board size (n), then board, 0 = blank square
  public String toString() {
    String[] str = new String[nSize + 1];
    str[0] = Integer.toString(nSize);

    for (int i = 0; i < nSize; i++) {
      String[] row = new String[nSize];
      for (int j = 0; j < nSize; j++) {
        row[j] = Integer.toString(board[i][j]);
      }
      str[i+1] = String.join(" ", row);
    }

    return String.join("\n", str);
  }

  // board dimension n
  public int dimension() {
    return nSize;
  }

  // number of tiles out of place
  public int hamming() {
    int count = 0;
    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        if ((i*nSize + j + 1) != board[i][j]) {
          count += 1;
        }
      }
    }
    return count;
  }

  // sum of Manhattan distances between tiles and goal (sum of the vertical and horizontal distance)
  public int manhattan() {
    int count = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        int val = board[i][j];

        if (val != i*nSize + j + 1) {
          int targetRow;
          int targetCol;
          if (val == 0) {
            // Target last position
            targetRow = nSize - 1;
            targetCol = nSize - 1;
          } else {
            targetRow = (val - 1) / nSize;
            targetCol = (val - 1) % nSize;
          }
          count += Math.abs(i - targetRow) + Math.abs(j - targetCol);
        }
      }
    }

    return count;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hamming() == 0;
  }

  // does this board equal y?
  // Same size and positions
  public boolean equals(Object y) {
    if (y == null || y.getClass() != Board.class) {
      return false;
    }
    return toString().equals(y.toString());
  }

  // all neighboring boards
  // possible states of the board (pieces moves to 0)
  public Iterable<Board> neighbors() {
    return new BoardIterable();
  }

  private class BoardIterable implements Iterable<Board> {
    @Override
    public Iterator<Board> iterator() {
      return new BoardIterator();
    }
  }

  private class BoardIterator implements Iterator<Board> {
    private int cursor = 0;
    private Board[] neighbors = new Board[4];

    BoardIterator() {
      int count = 0;
      int zeroRow = zeroPosition[0];
      int zeroCol = zeroPosition[1];

      int[][] draftBoard = new int[nSize][nSize];
      for (int i = 0; i < nSize; i++) {
        for (int j = 0; j < nSize; j++) {
          draftBoard[i][j] = board[i][j];
        }
      }

      if (zeroRow - 1 >= 0) {
        neighbors[count++] = createNeighbor(draftBoard, zeroRow - 1, zeroCol);
      }
      if (zeroCol - 1 >= 0) {
        neighbors[count++] = createNeighbor(draftBoard, zeroRow, zeroCol - 1);
      }
      if (zeroRow + 1 < nSize) {
        neighbors[count++] = createNeighbor(draftBoard, zeroRow + 1, zeroCol);
      }
      if (zeroCol + 1 < nSize) {
        neighbors[count++] = createNeighbor(draftBoard, zeroRow, zeroCol + 1);
      }
    }

    private Board createNeighbor(int[][] draft, int targetRow, int targetCol) {
      int val = draft[targetRow][targetCol];
      draft[zeroPosition[0]][zeroPosition[1]] = val;
      draft[targetRow][targetCol] = 0;
      Board movedBoard = new Board(draft);
      draft[zeroPosition[0]][zeroPosition[1]] = 0;
      draft[targetRow][targetCol] = val;
      return movedBoard;
    }

    // Checks if the next element exists
    public boolean hasNext() {
      return cursor < nSize && neighbors[cursor] != null;
    }

    // moves the cursor/iterator to next element
    public Board next() {
      return neighbors[cursor++];
    }
}

  // // a board that is obtained by exchanging any pair of tiles
  // public Board twin() {}

  // unit testing (not graded)
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);
    Board initial2 = new Board(tiles);
    Board initial3 = new Board(new int[][]{{1,2},{3,0}});

    StdOut.println(initial);
    StdOut.println();
    StdOut.printf("Hamming: %d\n", initial.hamming());
    StdOut.printf("Manhattan: %d\n", initial.manhattan());
    StdOut.printf("no (3): %b\n", initial.equals(3));
    StdOut.printf("no (null): %b\n", initial.equals(null));
    StdOut.printf("yes: %b\n", initial.equals(initial2));
    StdOut.printf("no (diff): %b\n", initial.equals(initial3));

    for (Board neighbor : initial.neighbors()) {
      StdOut.println("Neighbor:");
      StdOut.println(neighbor);
    }

    // // solve the puzzle
    // Solver solver = new Solver(initial);

    // // print solution to standard output
    // if (!solver.isSolvable())
    //   StdOut.println("No solution possible");
    // else {
    //   StdOut.println("Minimum number of moves = " + solver.moves());
    //   for (Board board : solver.solution())
    //     StdOut.println(board);
    // }
  }

}
