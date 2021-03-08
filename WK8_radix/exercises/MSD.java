package exercises;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * IT'S WRONG AND I HAVE TO CONTINUE THE COURSE...
 *  */

public class MSD {
  private static final int ALPHABET_LETTERS = 26;
  // lower case 'a' is 97
  private static final int CHARSET_OFFSET = 97;

  public static void sort(String[] list) {
    sortPosition(list, 0, list.length, 0);
  }

  private static int charAt(String str, int pos) {
    if (pos < str.length()) {
      return Character.toLowerCase(str.charAt(pos)) % CHARSET_OFFSET;
    }
    return 0;
  }

  public static void sortPosition(String[] list, int start, int end, int pos) {
    if (end <= start) return;

    int[] count = new int[ALPHABET_LETTERS + 2];
    String[] sorted = new String[end - start];

    for (int i = start; i < end; i++) {
      int index = charAt(list[i], pos) + 1;
      count[index] += 1;
    }

    // Aggregate
    for (int i = 1; i < count.length; i++) {
      count[i] += count[i - 1];
    }

    // Set name at index
    for (int i = start; i < end; i++) {
      int index = charAt(list[i], pos);
      sorted[count[index]++] = list[i];
    }

    // Update original list
    for (int i = start; i < end; i++) {
      list[i] = sorted[i - start];
    }

    // Recurse
    for (int i = 0; i < count.length - 1; i++) {
      int diff = i - 1 < 0 ? 0 : count[i-1];
      sortPosition(list, start + diff, start + count[i+1], pos + 1);
    }
  }

  public static void main(String[] args) {
    String file = "/Users/jpenna/Documents/princeton-algs/WK8_radix/exercises/names.txt";
    In in = new In(file);

    String[] list = new String[in.readInt()];
    for (int i = 0; !in.isEmpty(); i++) {
      list[i] = in.readString();
    }

    MSD.sort(list);

    for (int i = 0; i < 100; i++) {
      StdOut.print(list[i] + ", ");
    }
  }
}
