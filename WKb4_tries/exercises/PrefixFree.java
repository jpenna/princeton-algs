package exercises;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class PrefixFree {
  private Node root;
  private boolean isPrefixFree;

  public PrefixFree() {
    root = new Node();
    isPrefixFree = true;
  }

  private class Node {
    private String value;
    private Node[] next = null;

    public Node() {
      next = new Node[2];
    }
  }

  public void put(String str) {
    put(root, str, 0);
  }

  private Node put(Node node, String str, int pos) {
    if (node == null) {
      node = new Node();
    }
    if (node.value != null) {
      isPrefixFree = false;
    }
    if (str.length() == pos) {
      node.value = str;
      return node;
    }

    int branch = (int) str.charAt(pos) - 48;
    node.next[branch] = put(node.next[branch], str, pos + 1);

    return node;
  }

  private void print() {
    Queue<Node> queue = new Queue<>();

    int count = 1;
    int power = 0;
    queue.enqueue(root);

    while (!queue.isEmpty()) {
      Node node = queue.dequeue();
      if (node == null) {
        StdOut.print("  empty  ");
      } else {
        StdOut.print("  " + node.value + (node.value != null ? "* " : " "));
        for (Node n : node.next) {
          queue.enqueue(n);
        }
      }

      if (count == Math.pow(2, power)) {
        StdOut.println();
        power += 1;
      }
      count++;
    }
  }

  public static void main(String[] args) {
    PrefixFree pf = new PrefixFree();
    // String[] str = new String[]{"01", "10" , "0010", "1111"};
    String[] str = new String[]{"01", "10", "0010", "10100"};

    for (String s : str) {
      pf.put(s);
    }

    StdOut.println("Is prefix free: " + pf.isPrefixFree);

    pf.print();
  }
}
