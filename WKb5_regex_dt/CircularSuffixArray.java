import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
  private static final int R = 256; // ASCII chars
  private final char[] chars;
  private final int[] sortedIndexes;

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    // Throw an IllegalArgumentException in the constructor if the argument is null.
    if (s == null)
      throw new IllegalArgumentException("String argument is null.");

    chars = s.toCharArray();

    sortedIndexes = new int[chars.length];
    for (int i = 0; i < chars.length; i++) {
      sortedIndexes[i] = i;
    }

    radixSort(chars, sortedIndexes, 0, chars.length, 0);
  }

  private int getChar(int index) {
    int i = index < chars.length ? index : (index - chars.length);
    return chars[i];
  }

  private void radixSort(char[] chars, int[] indexes, int start, int end, int offset) {
    if (end - start <= 1 || offset >= chars.length) return;

    int[] buckets = new int[R + 1];
    int[] aux = new int[end - start];

    for (int i = start; i < end; i++) {
      buckets[getChar(indexes[i] + offset) + 1]++;
    }

    for (int i = 1; i < buckets.length; i++) {
      buckets[i] += buckets[i - 1];
    }

    for (int i = start; i < end; i++) {
      aux[buckets[getChar(indexes[i] + offset)]++] = indexes[i];
    }

    int prev = 0;
    for (int i = 0; i < buckets.length - 1; i++) {
      if (buckets[i] > prev) {
        radixSort(chars, aux, prev, buckets[i], offset + 1);
      }
      prev = buckets[i];
    }

    for (int i = start; i < end; i++) {
      indexes[i] = aux[i - start];
    }
  }

  // length of s
  public int length() {
    return chars.length;
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    // Throw an IllegalArgumentException in the method index()if i is outside its prescribed range(between 0 and n−1).
    if (i >= chars.length || i < 0)
      throw new IllegalArgumentException("String argument is null.");

    return sortedIndexes[i];
  }

  // unit testing (required)
  public static void main(String[] args) {
    String str = "ABRACADABRA!";
    CircularSuffixArray circular = new CircularSuffixArray(str);
    // StdOut.println("---- " + str + " -----");
    // for (int i = 0; i < str.length(); i++) {
    //   StdOut.print(", " + circular.index(i));
    // }
    // StdOut.println();

    str = "KVXRXKIKVXRXKI";
    circular = new CircularSuffixArray(str);
    StdOut.println("---- " + str + " -----");
    StdOut.println("Length: (14): " + circular.length());
    StdOut.println("Index 10 (4): " + circular.index(10));
    // StdOut.println("Index 1 (3): " + circular.index(1));
    // StdOut.println("Index 2 (2): " + circular.index(2));
    // StdOut.println("Index 3 (0): " + circular.index(3));

    // str = "ZAB";
    // circular = new CircularSuffixArray(str);
    // StdOut.println("---- " + str + " -----");
    // StdOut.println("Length: (3): " + circular.length());
    // StdOut.println("Index 0 (1): " + circular.index(0));
    // StdOut.println("Index 1 (2): " + circular.index(1));
    // StdOut.println("Index 2 (0): " + circular.index(2));
  }
}
