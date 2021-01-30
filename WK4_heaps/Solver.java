import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
  private final Board[] solution;
  private final int solutionMoves;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<Node> minPQ = new MinPQ<>(new NodeComparator());
    Node current = new Node(initial, null, 0);
    while (!current.board.isGoal()) {
      boolean foundPrev = false;
      for (Board neighbor : current.board.neighbors()) {
        Node node = new Node(neighbor, current, current.moves + 1);
        // If returning to previous position, do not insert
        if (!foundPrev && (current.previous == null || node.board.equals(current.previous.board))) {
          foundPrev = true;
          continue;
        }
        minPQ.insert(node);
      }
      current = minPQ.delMin();
    }

    solutionMoves = current.moves;
    solution = new Board[current.moves + 1];
    int i = current.moves;
    do {
      solution[i--] = current.board;
      current = current.previous;
    } while (current != null);

    minPQ = null;
  }

  private class Node implements Comparable<Node> {
    private final Node previous;
    private final Board board;
    private final int moves;
    private final int weight;

    public Node(Board board, Node previous, int moves) {
      this.board = board;
      this.previous = previous;
      this.moves = moves;
      int distance = board.manhattan(); // REPLACE HERE: priority function
      this.weight = this.moves + distance;
    }

    @Override
    public int compareTo(Node other) {
      if (weight == other.weight) {
        return 0;
      }
      return weight <= other.weight ? -1 : 1;
    }

    @Override
    public boolean equals(Object other) {
      return super.equals(other);
    }
  }

  private class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
      return Integer.compare(o1.weight, o2.weight);
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return moves() != -1;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    // Return -1 in moves() if the board is unsolvable.
    return solutionMoves;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    // Return null in solution() if the board is unsolvable.
    if (!isSolvable()) {
      return null;
    }
    return new SolutionIterable();
  }

  private class SolutionIterable implements Iterable<Board> {
    @Override
    public Iterator<Board> iterator() {
      return new SolutionIterator();
    }
  }

  private class SolutionIterator implements Iterator<Board> {
    private int cursor = 0;

    @Override
    public boolean hasNext() {
      return cursor <= solutionMoves;
    }

    @Override
    public Board next() {
      return solution[cursor++];
    }
  }

  // test client (see below)
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) {
        StdOut.println();
        StdOut.println(board);
      }
      StdOut.println("Minimum number of moves = " + solver.moves());
    }
  }

}
