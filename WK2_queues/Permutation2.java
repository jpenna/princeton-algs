import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation2 {
  private static HashMap<String, Integer> run(int k, ArrayList<String> input) {

    RandomizedQueue<String> queue = new RandomizedQueue<>();
    // int count = 0;
    // for (String s : input) {
    //   count++;
    //   if (queue.size() >= k) {
    //     int lucky = count - StdRandom.uniform(count);
    //     if (lucky > 1) {
    //       continue;
    //     }
    //     queue.dequeue();
    //   }
    //   queue.enqueue(s);
    // }
    for (String s : input) {
      queue.enqueue(s);
      if (queue.size() > k) {
        queue.dequeue();
      }
    }

    Iterator<String> iterator = queue.iterator();
    HashMap<String, Integer> dict = new HashMap<>();

    for (int i = 0; i < k; i++) {
      String key = iterator.next();
      Integer val = dict.get(key);
      int c = val == null ? 0 : val;
      dict.put(key, ++c);
    }

    return dict;
  }

  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);

    if (k == 0) {
      return;
    }

    ArrayList<String> list = new ArrayList<>();

    while (!StdIn.isEmpty()) {
      list.add(StdIn.readString());
    }
    HashMap<String, Integer> dict = new HashMap<>();

    for (int i = 0; i < 1000; i++) {
      HashMap<String, Integer> result = run(k, list);

      for (Map.Entry<String, Integer> set : result.entrySet()) {
        String key = set.getKey();
        Integer c = dict.get(key) == null ? 0 : dict.get(key);
        dict.put(key, c + set.getValue());
      }
    }

    for (Map.Entry<String, Integer> set : dict.entrySet()) {
      StdOut.printf("%s: %d\n", set.getKey(), set.getValue());
    }
  }
}
