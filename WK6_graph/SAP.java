// import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
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
    digraph = G;
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
    int distToCur = currentAncestors.get(cur) + 1;

    for (int adj : digraph.adj(cur)) {
      if (!currentAncestors.containsKey(adj)) {
        currentQueue.enqueue(adj);
        currentAncestors.put(adj, distToCur);
      }

      if (otherAncestors.containsKey(adj)) {
        return adj;
      }
    }

    return -1;
  }

  /** @return int[] {0: length, 1: ancestorId} */
  private int[] findAncestor(int v, int w) {
    checkNull(v);
    checkNull(w);
    if (v >= digraph.V() || w >= digraph.V()) {
      throw new IllegalArgumentException();
    }

    if (w == v) {
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

    int[] selected = null;
    int level = 0;
    while (!queueV.isEmpty() || !queueW.isEmpty()) {
      level++;
      int ancestorV = runAncestorQueue(queueV, ancestralV, ancestralW);
      int ancestorW = runAncestorQueue(queueW, ancestralW, ancestralV);

      selected = buildSet(ancestorV, ancestralV, ancestralW, selected);
      selected = buildSet(ancestorW, ancestralV, ancestralW, selected);

      if (selected != null && selected[1] < level) {
        return selected;
      }
    }

    return selected != null ? selected : new int[]{-1, -1};
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

      if (selected == null || distance < selected[1]) {
        return new int[]{ancestor, distance};
      }
    }

    return selected;
  }

  private int[] findAncestor(Iterable<Integer> v, Iterable<Integer> w) {
    int[] solution = {-1, -1};
    double smallestLength = Double.POSITIVE_INFINITY;
    for (int vv : v) {
      for (int ww : w) {
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

    // In in = new In("/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/digraph4.txt");
    // Digraph G = new Digraph(in);
    // SAP sap = new SAP(G);
    // while (!StdIn.isEmpty()) {
    //   int v = 1;
    //   int w = 4;
    //   ArrayList<Integer> x = new ArrayList<>();
    //   x.add(3);
    //   x.add(9);
    //   x.add(7);
    //   x.add(2);
    //   ArrayList<Integer> y = new ArrayList<>();
    //   y.add(12);
    //   y.add(11);
    //   y.add(2);
    //   int length = sap.length(v, w);
    //   int ancestor = sap.ancestor(v, w);
    //   StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    // }
  }
}
