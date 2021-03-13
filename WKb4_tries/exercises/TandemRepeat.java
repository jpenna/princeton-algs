package exercises;

class TandemRepeat {
  public static final int RADIX_LENGTH = 126; // ascii

  public static int[][] preprocessPattern(String pat) {
    int[][] dfa = new int[pat.length()][RADIX_LENGTH];

    // Initialize with next steps
    for (int i = 0; i < pat.length(); i++) {
      dfa[i][pat.charAt(i)] = i + 1;
    }

    int[] x = dfa[0];
    for (int pos = 1; pos < pat.length(); pos++) {
      for (int ch = 0; ch < RADIX_LENGTH; ch++) {
        if (dfa[pos][ch] == 0) dfa[pos][ch] = x[ch];
      }
      int xNext = x[pat.charAt(pos)];
      x = dfa[xNext];
    }

    return dfa;
  }

  public static int search(String str, int[][] dfa, int start) {
    for (int i = start, j = 0; i < str.length(); i++) {
      int c = str.charAt(i);
      j = dfa[j][c];
      if (j == dfa.length) {
        return i - j + 1;
      }
    }
    return -1;
  }

  public static int tandem(String str, String pat) {
    int[][] dfa = preprocessPattern(pat);
    int index = -pat.length();
    int count = -1;
    while (index != -1) {
      count++;
      index = index + pat.length();
      index = search(str, dfa, index);
    }
    return count;
  }

  public static void searchAndExpect(String txt, String pat) {
    int[][] dfa = preprocessPattern(pat);
    int index = search(txt, dfa, 0);
    System.out.println("Expected: " + txt.indexOf(pat) + " | Was: " + index);
  }

  public static int findAll(String txt, String pat) {
    int index = -pat.length();
    int count = -1;
    while (index != -1) {
      count++;
      index = index + pat.length();
      index = txt.indexOf(pat, index);
    }
    return count;
  }

  public static void tandemAndExpect(String txt, String pat) {
    int count = tandem(txt, pat);
    System.out.println("Found tandem: " + count + " | Expect: " + findAll(txt, pat));
  }

  public static void main(String[] args) {
    searchAndExpect("ababababbacababcbbbaaaccaabacbbbaababac", "ababac");
    searchAndExpect("i ran home yesterday to find something to do", "to ");

    tandemAndExpect("well... well... well! Look if we don't have the welcomed one", "wel");
    tandemAndExpect("My great dream granted whishes to great breads over the great reaper", "rea");
  }
}
