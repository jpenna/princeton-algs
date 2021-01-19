import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);

    if (k == 0) {
      return;
    }

    RandomizedQueue<String> queue = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      queue.enqueue(StdIn.readString());
    }

    Iterator<String> iterator = queue.iterator();

    for (int i = 0; i < k; i++) {
      StdOut.println(iterator.next());
    }

    // StdOut.printf("Size: %d\n", queue.size());
  }
}
