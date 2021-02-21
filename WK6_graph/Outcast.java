import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private final WordNet wordnet;

  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    String outcastWord = "";
    int outcastDist = 0;

    for (String pin : nouns) {
      int maxDist = 0;
      for (String noun : nouns) {
        maxDist += wordnet.distance(pin, noun);
      }
      if (maxDist > outcastDist) {
        outcastDist = maxDist;
        outcastWord = pin;
      }
    }

    return outcastWord;
  }

  // see test client below
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);

    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }

    // String synPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/synsets.txt";
    // String hyperPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/ignore/hypernyms.txt";
    // String outcastPath = "/Users/jpenna/Documents/princeton-algs/WK6_graph/samples/outcast8.txt";
    // WordNet wordnet = new WordNet(synPath, hyperPath);
    // Outcast outcast = new Outcast(wordnet);

    // In in = new In(outcastPath);
    // String[] nouns = in.readAllStrings();
    // StdOut.println(outcast.outcast(nouns));
  }
}
