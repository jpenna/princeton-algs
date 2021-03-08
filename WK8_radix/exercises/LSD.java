package exercises;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class LSD {
  private static final int ALPHABET_LETTERS = 26;
  // lower case A is 97
  private static final int CHARSET_OFFSET = 97;

  public static void sort(String[] list) {
    int last = getLastPosition(list);
    while (last >= 0) {
      sortPosition(list, last--);
    }
  }

  public static int getLastPosition(String[] list) {
    int last = 0;
    for (String name : list) {
      if (name.length() > last) {
        last = name.length();
      }
    }
    return last - 1;
  }

  public static void sortPosition(String[] list, int position) {
    int[] count = new int[ALPHABET_LETTERS + 2];
    String[] sorted = new String[list.length];

    // Count how many of each letter
    for (String name : list) {
      // + 1 space for empty char (smaller names)
      int index = 1;
      if (name.length() > position) {
        index = Character.toLowerCase(name.charAt(position)) % CHARSET_OFFSET + 2;
      }
      count[index] += 1;
    }

    // Aggregate
    for (int i = 1; i < count.length; i++) {
      count[i] += count[i - 1];
    }

    // Set name at index
    for (String name : list) {
      int index = 0;
      if (name.length() > position) {
        // +1 because 0 is for empty chars
        index = Character.toLowerCase(name.charAt(position)) % CHARSET_OFFSET + 1;
      }
      sorted[count[index]++] = name;
    }

    // Update original list
    for (int i = 0; i < list.length; i++) {
      list[i] = sorted[i];
    }
  }

  public static void main(String[] args) {
    String file = "/Users/jpenna/Documents/princeton-algs/WK8_radix/exercises/names.txt";
    In in = new In(file);

    String[] list = new String[in.readInt()];
    for (int i = 0; !in.isEmpty(); i++) {
      list[i] = in.readString();
    }

    LSD.sort(list);

    for (int i = 0; i < 100; i++) {
      StdOut.print(list[i] + ", ");
    }
  }
}
