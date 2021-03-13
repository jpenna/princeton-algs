// import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
  private final Digraph digraph;

  private void checkNull(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
  }

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    checkNull(G);
    digraph = new Digraph(G);
  }

  private int runAncestorQueue(
    Queue<Integer> currentQueue,
    HashMap<Integer, Integer> currentAncestors,
    HashMap<Integer, Integer> otherAncestors
  ) {
    if (currentQueue.isEmpty()) {
      return -1;
    }

    int cur = currentQueue.dequeue();
    int adjDist = currentAncestors.get(cur) + 1;

    // StdOut.print("\nCurrent: " + cur + ", adj:");

    int foundAncestor = -1;
    if (otherAncestors.containsKey(cur)) {
      foundAncestor = cur;
    }

    for (int adj : digraph.adj(cur)) {
      // StdOut.print(" " + adj);
      if (!currentAncestors.containsKey(adj)) {
        currentQueue.enqueue(adj);
        currentAncestors.put(adj, adjDist);
      }
      // StdOut.print(", AdjDist: " + adjDist);
    }

    return foundAncestor;
  }

  /** @return int[] {0: ancestorId, 1: length} */
  private int[] findAncestor(Integer v, Integer w) {
    checkNull(v);
    checkNull(w);
    if (v >= digraph.V() || w >= digraph.V()) {
      throw new IllegalArgumentException();
    }

    if (w.equals(v)) {
      return new int[]{v, 0};
    }

    HashMap<Integer, Integer> ancestralV = new HashMap<>();
    HashMap<Integer, Integer> ancestralW = new HashMap<>();
    Queue<Integer> queueV = new Queue<>();
    Queue<Integer> queueW = new Queue<>();

    ancestralV.put(v, 0);
    ancestralW.put(w, 0);
    queueV.enqueue(v);
    queueW.enqueue(w);

    int[] selected = {-1, -1};
    while (!queueV.isEmpty() || !queueW.isEmpty()) {
      int ancestorV = runAncestorQueue(queueV, ancestralV, ancestralW);
      int ancestorW = runAncestorQueue(queueW, ancestralW, ancestralV);

      selected = buildSet(ancestorV, ancestralV, ancestralW, selected);
      selected = buildSet(ancestorW, ancestralV, ancestralW, selected);
    }

    return selected;
  }

  private int[] buildSet(
    int ancestor,
    HashMap<Integer, Integer> ancestralOne,
    HashMap<Integer, Integer> ancestralTwo,
    int[] selected
  ) {
    if (ancestor != -1) {
      Integer anc = Integer.valueOf(ancestor);
      int distance = ancestralOne.get(anc) + ancestralTwo.get(anc);

      if (selected[0] == -1 || distance < selected[1]) {
        return new int[]{ancestor, distance};
      }
    }

    return selected;
  }

  private int[] findAncestor(Iterable<Integer> v, Iterable<Integer> w) {
    int[] solution = {-1, -1};
    double smallestLength = Double.POSITIVE_INFINITY;
    for (Integer vv : v) {
      for (Integer ww : w) {
        int[] res = findAncestor(vv, ww);
        int ancestor = res[0];
        int length = res[1];
        if (ancestor != -1 && length < smallestLength) {
          smallestLength = length;
          solution[0] = ancestor;
          solution[1] = length;
        }
      }
    }
    return solution;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    return findAncestor(v, w)[1];
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    return findAncestor(v, w)[0];
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    return findAncestor(v, w)[1];
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    return findAncestor(v, w)[0];
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }

    // In in = new In("/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/digraph6.txt");
    // Digraph G = new Digraph(in);
    // SAP sap = new SAP(G);
    // int x = 4;
    // int y = 5;
    // // ArrayList<Integer> x = new ArrayList<>();
    // // x.add(1);
    // // x.add(null);
    // // x.add(4);
    // // ArrayList<Integer> y = new ArrayList<>();
    // // y.add(3);
    // // y.add(11);
    // int length = sap.length(x, y);
    // int ancestor = sap.ancestor(x, y);
    // // int ancestor = 0;
    // StdOut.printf("\nlength = %d, ancestor = %d\n", length, ancestor);

    // String synPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/synsets.txt";
    // String hyperPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/hypernyms.txt";
    // WordNet wordNet = new WordNet(synPath, hyperPath);
    // StdOut.println("distance('jewelled_headdress', 'survey') 6: " + wordNet.distance("jewelled_headdress", "survey"));
  }
}
