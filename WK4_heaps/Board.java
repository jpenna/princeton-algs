import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Board {
  private final int nSize;
  private final int[][] board;
  private final int zeroRow;
  private final int zeroCol;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    int zr = 0;
    int zc = 0;

    nSize = tiles.length;

    board = new int[nSize][nSize];

    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        board[i][j] = tiles[i][j];
        if (tiles[i][j] == 0) {
          zr = i;
          zc = j;
        }
      }
    }

    zeroRow = zr;
    zeroCol = zc;
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
        if ((i*nSize + j + 1) == board[i][j] || board[i][j] == 0) {
          continue;
        }
        count += 1;
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

        if (val != i*nSize + j + 1 && val != 0) {
          int targetRow = (val - 1) / nSize;
          int targetCol = (val - 1) % nSize;
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
    if (y == null || y.getClass() != this.getClass()) {
      return false;
    }

    Board other = (Board) y;
    if (dimension() != other.dimension()) {
      return false;
    }

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (other.board[i][j] != board[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  // all neighboring boards
  // possible states of the board (pieces moves to 0)
  public Iterable<Board> neighbors() {
    int[][] draftBoard = new int[nSize][nSize];
    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        draftBoard[i][j] = board[i][j];
      }
    }

    Stack<Board> neighborsStack = new Stack<>();
    if (zeroRow - 1 >= 0) {
      neighborsStack.push(createNeighbor(draftBoard, zeroRow - 1, zeroCol));
    }
    if (zeroCol - 1 >= 0) {
      neighborsStack.push(createNeighbor(draftBoard, zeroRow, zeroCol - 1));
    }
    if (zeroRow + 1 < nSize) {
      neighborsStack.push(createNeighbor(draftBoard, zeroRow + 1, zeroCol));
    }
    if (zeroCol + 1 < nSize) {
      neighborsStack.push(createNeighbor(draftBoard, zeroRow, zeroCol + 1));
    }

    return neighborsStack;
  }

  private Board createNeighbor(int[][] draft, int targetRow, int targetCol) {
    int val = draft[targetRow][targetCol];
    draft[zeroRow][zeroCol] = val;
    draft[targetRow][targetCol] = 0;
    Board movedBoard = new Board(draft);
    draft[zeroRow][zeroCol] = 0;
    draft[targetRow][targetCol] = val;
    return movedBoard;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int[][] copy = new int[nSize][nSize];

    int index = 0;
    int[] swaps = new int[4];

    for (int i = 0; i < nSize; i++) {
      for (int j = 0; j < nSize; j++) {
        if (index < swaps.length && board[i][j] != 0) {
          swaps[index++] = i;
          swaps[index++] = j;
        }
        copy[i][j] = board[i][j];
      }
    }

    int val = copy[swaps[0]][swaps[1]];
    copy[swaps[0]][swaps[1]] = copy[swaps[2]][swaps[3]];
    copy[swaps[2]][swaps[3]] = val;

    return new Board(copy);
  }

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
    Board initial3 = new Board(new int[][]{{1, 2}, {3, 0}});

    StdOut.println(initial);
    StdOut.println();
    StdOut.printf("Hamming: %d\n", initial.hamming());
    StdOut.printf("Manhattan: %d\n", initial.manhattan());
    StdOut.printf("yes: %b\n", initial.equals(initial2));
    StdOut.printf("no (diff): %b\n", initial.equals(initial3));

    for (Board neighbor : initial.neighbors()) {
      StdOut.println("Neighbor:");
      StdOut.println(neighbor);
    }
  }
}
