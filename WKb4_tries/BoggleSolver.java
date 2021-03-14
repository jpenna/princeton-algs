import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
  private static final int A_ENCODE = 65;
  private static final int U_ENCODE = 85;
  private static final int Q_LETTER = 16;
  private final Node root;
  private int boardRows;
  private int boardCols;

  // Initializes the data structure using the given array of strings as the
  // dictionary.
  // (You can assume each word in the dictionary contains only the uppercase
  // letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    root = new Node();

    for (String word : dictionary) {
      if (word.length() < 3) {
        continue;
      }
      Node node = processWord(word);
      if (node != null) node.setWord(word);
    }
  }

  private class Node {
    private Node[] edges = new Node[26];
    private String word;

    private void setWord(String word) {
      this.word = word;
    }

    private Node addNode(int letter) {
      edges[letter] = new Node();
      return edges[letter];
    }
  }

  private Node processWord(String word) {
    Node current = root;
    for (int i = 0; i < word.length(); i++) {
      int c = word.charAt(i) - A_ENCODE;

      if (c == Q_LETTER) {
        // Skip 'U'
        if (i + 1 < word.length() && word.charAt(i + 1) == U_ENCODE) {
          i++;
          // Do not consider words that have 'Q' that isn't 'QU'
        } else {
          return null;
        }
      }

      if (current.edges[c] != null) {
        current = current.edges[c];
      } else {
        current = current.addNode(c);
      }
    }
    return current;
  }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    boardRows = board.rows();
    boardCols = board.cols();
    boolean[][] visited = new boolean[boardRows][boardCols];
    HashSet<String> foundWords = new HashSet<>();
    for (int row = 0; row < boardRows; row++) {
      for (int col = 0; col < boardCols; col++) {
        doSearch(row, col, root, board, visited, foundWords);
      }
    }
    return foundWords;
  }

  private void doSearch(
    int row,
    int col,
    Node cur,
    BoggleBoard board,
    boolean[][] visited,
    HashSet<String> foundWords
  ) {
    // Out of bounds
    if (row < 0 || col < 0 || row >= boardRows || col >= boardCols) {
      return;
    }

    // Visited already
    if (visited[row][col]) {
      return;
    }


    char letter = board.getLetter(row, col);
    Node node = cur.edges[letter - A_ENCODE];

    if (node == null) {
      return;
    }

    if (node.word != null) {
      foundWords.add(node.word);
    }

    visited[row][col] = true;

    // Vertical
    doSearch(row - 1, col, node, board, visited, foundWords);
    doSearch(row + 1, col, node, board, visited, foundWords);
    // Horizontal
    doSearch(row, col - 1, node, board, visited, foundWords);
    doSearch(row, col + 1, node, board, visited, foundWords);
    // Diagonals
    doSearch(row - 1, col - 1, node, board, visited, foundWords);
    doSearch(row + 1, col + 1, node, board, visited, foundWords);
    doSearch(row - 1, col + 1, node, board, visited, foundWords);
    doSearch(row + 1, col - 1, node, board, visited, foundWords);

    visited[row][col] = false;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    int length = word.length();

    // Validate word
    Node node = root;
    for (int i = 0; i < length; i++) {
      int c = word.charAt(i) - A_ENCODE;

      // Handle Q
      if (c == Q_LETTER) {
        // Skip 'U'
        if (i + 1 < length && word.charAt(i + 1) == U_ENCODE) {
          i++;
        // Dictionary do not consider words that have 'Q' that isn't 'QU'
        } else {
          return 0;
        }
      }

      node = node.edges[c];
      if (node == null) {
        return 0;
      }
    }

    if (node.word == null) {
      return 0;
    }

    // Get points
    if (length < 5) {
      return 1;
    }
    switch (length) {
      case 5: return 2;
      case 6: return 3;
      case 7: return 5;
      default: return 11;
    }
  }

  public static void main(String[] args) {
    // String dict = "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/dictionary-algs4.txt";
    // In in = new In(dict);
    // String[] dictionary = in.readAllStrings();
    // BoggleSolver solver = new BoggleSolver(dictionary);
    // StdOut.println(solver.scoreOf("TRICE"));

    String dict = "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/dictionary-yawl.txt";
    String[] boardPaths = new String[]{
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points0.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points1.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points100.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points1000.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points1111.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points1250.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points13464.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points1500.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points2.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points200.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points2000.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points26539.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points3.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points300.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points4.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points400.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points4410.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points4527.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points4540.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points5.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points500.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points750.txt",
      "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-points777.txt",
    };
    // String dict = "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/dictionary-common.txt";
    // String[] boardPaths = new String[]{
    //   "/Users/jpenna/Documents/princeton-algs/WKb4_tries/samples/board-random1.txt",
    // };
    In in = new In(dict);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);

    int[] scores = new int[boardPaths.length];
    int[] counts = new int[boardPaths.length];
    for (int i = 0; i < boardPaths.length; i++) {
      BoggleBoard board = new BoggleBoard(boardPaths[i]);
      for (String word : solver.getAllValidWords(board)) {
        counts[i]++;
        StdOut.print(word + ", ");
        scores[i] += solver.scoreOf(word);
      }
      StdOut.println("\n************************\n");
    }

    for (int i = 0; i < boardPaths.length; i++) {
      StdOut.println(boardPaths[i]);
      StdOut.println("Count: " + counts[i]);
      StdOut.println("Score = " + scores[i]);
    }
  }
}
