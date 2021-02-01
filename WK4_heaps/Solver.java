import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
  private final Board[] solution;
  private final int solutionMoves;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    if (initial.isGoal()) {
      solutionMoves = 0;
      solution = new Board[]{initial};
      return;
    }

    Board twin = initial.twin();

    MinPQ<Node> minPQOriginal = new MinPQ<>(new NodeComparator());
    MinPQ<Node> minPQTwin = new MinPQ<>(new NodeComparator());

    Node solutionNode = null;
    boolean unsolvable = false;

    Node current = new Node(initial, null, 0);
    Node currentTwin = new Node(twin, null, 0);

    while (!unsolvable) {
      current = solve(current, minPQOriginal);
      if (current.board.isGoal()) {
        solutionNode = current;
        break;
      }
      currentTwin = solve(currentTwin, minPQTwin);
      if (currentTwin.board.isGoal()) {
        unsolvable = true;
      }
    }

    if (unsolvable) {
      solutionMoves = -1;
      solution = new Board[]{};
    } else {
      solutionMoves = solutionNode.moves;
      solution = new Board[solutionNode.moves + 1];
      int i = solutionNode.moves;
      do {
        solution[i--] = solutionNode.board;
        solutionNode = solutionNode.previous;
      } while (solutionNode != null);
    }
  }

  private Node solve(Node current, MinPQ<Node> minPQ) {
    boolean foundPrev = false;
    for (Board neighbor : current.board.neighbors()) {
      Node node = new Node(neighbor, current, current.moves + 1);
      // If returning to previous position, do not insert
      if (!foundPrev && (current.previous != null && node.board.equals(current.previous.board))) {
        foundPrev = true;
        continue;
      }
      minPQ.insert(node);
    }
    return minPQ.delMin();
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
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
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
        StdOut.println(board);
      }
    }
  }

}
