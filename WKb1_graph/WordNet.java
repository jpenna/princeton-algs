import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
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
  private SAP graph;
  private HashMap<Integer, Node> nodes;
  private final HashMap<String, LinkedBag<Integer>> words;

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
    nodes = new HashMap<>();
    In inSynsets = new In(synsets);
    In inHypernyms = new In(hypernyms);

    int countVertices = 0;
    while (!inSynsets.isEmpty()) {
      countVertices++;
      String line = inSynsets.readLine();
      String[] cols = line.split(",");
      int id = Integer.parseInt(cols[0]);
      nodes.put(id, new Node(id, cols[1]));
    }

    Digraph digraph = new Digraph(countVertices);

    while (!inHypernyms.isEmpty()) {
      String line = inHypernyms.readLine();
      String[] cols = line.split(",");
      Integer id = null;
      for (String s : cols) {
        int num = Integer.parseInt(s);
        // First column is id
        if (id == null) {
          id = num;
          continue;
        }
        digraph.addEdge(id, num);
      }
    }

    graph = new SAP(digraph);
  }

  private class Node {
    private final String synset;
    private final LinkedBag<Integer> hypernyms;

    public Node(int id, String synset) {
      this.hypernyms = new LinkedBag<>();
      this.synset = synset;

      for (String word : Arrays.asList(synset.split(" "))) {
        LinkedBag<Integer> list = words.get(word);
        if (list == null) {
          list = new LinkedBag<>();
        }
        list.add(id);
        words.put(word, list);
      }
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    ArrayList<String> nounsList = new ArrayList<>(words.size());
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

  private int ancestor(String nounA, String nounB, boolean shouldReturnDistance) {
    LinkedBag<Integer> v = words.get(nounA);
    LinkedBag<Integer> w = words.get(nounB);
    return shouldReturnDistance ? graph.length(v, w) : graph.ancestor(v, w);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    return ancestor(nounA, nounB, true);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    int id = ancestor(nounA, nounB, false);
    return nodes.get(id).synset;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    String synPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/synsets.txt";
    String hyperPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/hypernyms.txt";
    // String synPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/synsets100-subgraph.txt";
    // String hyperPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/hypernyms100-subgraph.txt";

    WordNet wordNet = new WordNet(synPath, hyperPath);

    // for (String noun : wordNet.nouns()) {
    //   StdOut.print(noun + " ");
    // }

    // StdOut.println("isNoun('change') TRUE: " + wordNet.isNoun("change"));
    // StdOut.println("isNoun('octopus') FALSE: " + wordNet.isNoun("octopus"));

    // StdOut.println("distance('octopus', 'chondrin') 2: " + wordNet.distance("collagen", "chondrin"));
    // StdOut.println("distance('myosin', 'fibrinogen') 6: " + wordNet.distance("myosin", "fibrinogen"));
    StdOut.println("distance('jewelled_headdress', 'survey') 6: " + wordNet.distance("jewelled_headdress", "survey"));

    // StdOut.println("sap('octopus', 'chondrin') 'scleroprotein albuminoid': " + wordNet.sap("collagen", "chondrin"));
    // StdOut.println("sap('myosin', 'fibrinogen') protein: " + wordNet.sap("myosin", "fibrinogen"));
  }
}
