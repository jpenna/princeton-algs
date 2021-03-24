import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;

public class BurrowsWheeler {
  private static final int R = 256;

  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform() {
    String str = BinaryStdIn.readString();
    CircularSuffixArray circular = new CircularSuffixArray(str);

    int first = 0;
    for (int i = 0; i < str.length(); i++) {
      if (circular.index(i) == 0) {
        first = i;
        break;
      }
    }

    BinaryStdOut.write(first);
    for (int i = 0; i < str.length(); i++) {
      int j = circular.index(i);
      if (j == 0) {
        BinaryStdOut.write(str.charAt(str.length() - 1));
      } else {
        BinaryStdOut.write(str.charAt(j - 1));
      }
    }

    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform,
  // reading from standard input and writing to standard output
  public static void inverseTransform() {
    int first = BinaryStdIn.readInt();
    char[] chars = BinaryStdIn.readString().toCharArray();

    int[] buckets = new int[R + 1];

    for (char c : chars) {
      buckets[c + 1]++;
    }

    for (int i = 1; i < buckets.length; i++) {
      buckets[i] += buckets[i - 1];
    }

    int[] next = new int[chars.length];
    for (int i = 0; i < chars.length; i++) {
      next[buckets[chars[i]]++] = i;
    }

    int index = first;
    for (int i = 0; i < chars.length; i++) {
      index = next[index];
      BinaryStdOut.write(chars[index]);
    }

    BinaryStdOut.close();
  }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    String op = args[0];
    if (op.equals("-")) {
      transform();
    } else if (op.equals("+")) {
      inverseTransform();
    }
  }

}
