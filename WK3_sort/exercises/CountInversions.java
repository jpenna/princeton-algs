package exercises;

import edu.princeton.cs.algs4.StdOut;

/**
 * CountInversions
 */
public class CountInversions {
  public static int count = 0;

  public static boolean less(Integer a, Integer b) {
    return a.compareTo(b) < 0;
  }

  public static void merge(
    Integer[] array,
    Integer[] aux,
    int a,
    int mid,
    int b
  ) {
    int i = a;
    int j = mid;
    for (int k = a; k < b; k++) {
      if (j >= b)
        aux[k] = array[i++];
      else if (i >= b || less(array[j], array[i])) {
        count += mid - i;
        aux[k] = array[j++];
      } else
        aux[k] = array[i++];
    }
    for (int n = a; n < b; n++) {
      array[n] = aux[n];
    }
  }

  public static void sort(
    Integer[] array,
    Integer[] aux,
    int a,
    int b
  ) {
    if (b - a <= 1) return;
    int mid = a + (b - a) / 2;
    sort(array, aux, a, mid);
    sort(array, aux, mid, b);
    merge(array, aux, a, mid, b);
  }

  public static void main(String[] args) {
    Integer[] array = {9,8,1,6,2,3,1}; // 4 + 2 = 6
    Integer[] aux = new Integer[array.length]; // 4 + 2 = 6
    sort(array, aux, 0, array.length);

    for (int val : array) {
      StdOut.printf("%d ", val);
    }

    StdOut.printf("\nInversions: %d\n", count);
  }

}
