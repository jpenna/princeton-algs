package exercises;

// This solution isn't good.
// Should use Manacher’s Algorithm, which is linear
public class Palindrome {

  public static String findPalindrome(String str) {
    char[] chars = str.toCharArray();

    int longest = 0;
    int indexLongest = 0;

    for (int i = 1, j = 0; i < str.length() - 1; i++) {
      char cur = chars[i];
      char next = chars[i+1];

      if (cur == next) {
        continue;
      }

      char prev = j - 1 < 0 ? '\0' : chars[j - 1];
      if (prev == next) {
        j--;
      } else {
        j = i + 1;
      }

      int size = i + 2 - j;
      if (size > longest) {
        longest = size;
        indexLongest = j;
      }
    }

    return str.substring(indexLongest, indexLongest + longest);
  }

  public static void main(String[] args) {
    System.out.println(findPalindrome("peixe salolas agora"));
    System.out.println(findPalindrome("peixe salolas exiep"));

    // expect
    System.out.println("ababababa");
    System.out.println(findPalindrome("peixe ababababac uexiep"));
  }
}
