package exercises;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KeyIndexedCounting {
  private static final int ALPHABET_LETTERS = 26;
  private static final int A_ASCII_CODE = 65;

  private String[] list;
  private String[] sorted;
  private int[] count;

  public KeyIndexedCounting(String[] list) {
    this.list = list;
    sort();
  }

  private void sort() {
    count = new int[ALPHABET_LETTERS + 1];
    sorted = new String[list.length];

    for (String name : list) {
      count[name.charAt(0) % A_ASCII_CODE + 1] += 1;
    }

    for (int i = 1; i < count.length; i++) {
      count[i] += count[i-1];
    }

    for (String name : list) {
      int index = name.charAt(0) % A_ASCII_CODE;
      sorted[count[index]++] = name;
    }
  }


  public static void main(String[] args) {
    String file = "/Users/jpenna/Documents/princeton-algs/WK8_radix/exercises/names.txt";
    In in = new In(file);

    String[] list = new String[in.readInt()];
    for (int i = 0; !in.isEmpty(); i++) {
      list[i] = in.readString();
    }

    KeyIndexedCounting keyIndexed = new KeyIndexedCounting(list);

    StdOut.println("Counts: ");
    for (int i = 0; i < keyIndexed.count.length; i++) {
      StdOut.println((char)(i + A_ASCII_CODE) + ": " + (keyIndexed.count[i] - (i > 0 ? keyIndexed.count[i-1] : 0)));
    }
    StdOut.println("Sorted");
    for (String name : keyIndexed.sorted) {
      StdOut.print(name + ", ");
    }
  }
}
