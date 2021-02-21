import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.Queue;

/* Corner cases.  Throw an IllegalArgumentException in the following situations:
Any argument to the constructor or an instance method is null
The input to the constructor does not correspond to a rooted DAG.
Any of the noun arguments in distance() or sap() is not a WordNet noun.

Performance requirements.  Your data type should use space linear in the input size (size of synsets and hypernyms files). The constructor should take time linearithmic (or better) in the input size. The method isNoun() should run in time logarithmic (or better) in the number of nouns. The methods distance() and sap() should run in time linear in the size of the WordNet digraph. For the analysis, assume that the number of nouns per synset is bounded by a constant.

*/

public class WordNet {
  private ArrayList<Node> graph;
  private HashMap<String, Integer> words;

  private void checkNull(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException();
    }
  }

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    checkNull(synsets);
    checkNull(hypernyms);

    words = new HashMap<>();
    graph = new ArrayList<>();
    In inSynsets = new In(synsets);
    In inHypernyms = new In(hypernyms);

    while (!inSynsets.isEmpty()) {
      String line = inSynsets.readLine();
      String[] cols = line.split(",");
      int id = Integer.parseInt(cols[0]);
      graph.add(new Node(id, cols[1]));
    }


    Integer rootId = null;
    while (!inHypernyms.isEmpty()) {
      String line = inHypernyms.readLine();
      String[] cols = line.split(",");
      Integer id = null;
      for (String s : cols) {
        int num = Integer.parseInt(s);
        if (id == null) {
          id = num;
          continue;
        }
        graph.get(id).addEdge(num);
      }
      // Find root (what if it has 0 hypernyms and 0 connected nodes?)
      if (graph.get(id).hypernyms.size() == 0) {
        rootId = id;
      }
    }

    if (rootId == null) {
      throw new IllegalArgumentException();
    }
  }

  private class Node {
    private final int id;
    private final String synset;
    private LinkedBag<Integer> hypernyms;

    public Node(int id, String synset) {
      this.id = id;
      this.hypernyms = new LinkedBag<>();
      this.synset = synset;

      for (String word : Arrays.asList(synset.split(" "))) {
        words.put(word, id);
      }
    }

    private void addEdge(int hypernymId) {
      hypernyms.add(hypernymId);
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    ArrayList<String> nounsList = new ArrayList<>(graph.size());
    for (String str : words.keySet()) {
      nounsList.add(str);
    }
    return nounsList;
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    checkNull(word);
    return words.containsKey(word);
  }

  private Integer findAncestorId(Queue<Integer> queue, Set<Integer> parents, Set<Integer> otherParents) {
    ArrayList<Integer> list = new ArrayList<>(queue.size());
    while (!queue.isEmpty()) {
      list.add(queue.dequeue());
    }

    for (int id : list) {
      for (int hyperId : graph.get(id).hypernyms) {
        if (otherParents.contains(hyperId)) {
          return hyperId;
        }
        parents.add(hyperId);
        queue.enqueue(hyperId);
      }
    }

    return null;
  }

  private int ancestor(String nounA, String nounB, boolean shouldReturnDistance) {
    checkNull(nounA);
    checkNull(nounB);

    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    int idA = words.get(nounA);
    int idB = words.get(nounB);

    if (idA == idB) {
      return 0;
    }

    Set<Integer> parentsA = new HashSet<>();
    Set<Integer> parentsB = new HashSet<>();
    Queue<Integer> queueA = new Queue<>();
    Queue<Integer> queueB = new Queue<>();

    queueA.enqueue(idA);
    queueB.enqueue(idB);

    int pathCount = 0;
    Integer ancestor;
    while (true) {
      ancestor = findAncestorId(queueA, parentsA, parentsB);
      if (ancestor != null) {
        return shouldReturnDistance ? pathCount + 1 : ancestor;
      }
      pathCount++;

      ancestor = findAncestorId(queueB, parentsB, parentsA);
      if (ancestor != null) {
        return shouldReturnDistance ? pathCount + 1 : ancestor;
      }
      pathCount++;
    }
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    return ancestor(nounA, nounB, true);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    int id = ancestor(nounA, nounB, false);
    return graph.get(id).synset;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    String synPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/synsets100-subgraph.txt";
    String hyperPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/hypernyms100-subgraph.txt";

    WordNet wordNet = new WordNet(synPath, hyperPath);

    for (String noun : wordNet.nouns()) {
      StdOut.print(noun + " ");
    }

    StdOut.println("isNoun('change') TRUE: " + wordNet.isNoun("change"));
    StdOut.println("isNoun('octopus') FALSE: " + wordNet.isNoun("octopus"));

    StdOut.println("distance('octopus', 'chondrin') 2: " + wordNet.distance("collagen", "chondrin"));
    StdOut.println("distance('myosin', 'fibrinogen') 6: " + wordNet.distance("myosin", "fibrinogen"));

    StdOut.println("sap('octopus', 'chondrin') 'scleroprotein albuminoid': " + wordNet.sap("collagen", "chondrin"));
    StdOut.println("sap('myosin', 'fibrinogen') protein: " + wordNet.sap("myosin", "fibrinogen"));
  }
}
