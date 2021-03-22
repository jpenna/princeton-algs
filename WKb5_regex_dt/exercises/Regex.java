package exercises;

import java.util.ArrayList;
import java.util.Stack;

// This is just a draft (bad one)
class Regex {
  public static ArrayList<Integer>[] buildNfs(String re) {
    Stack<Integer> stack = new Stack<>();
    char[] chars = re.toCharArray();
    ArrayList<Integer>[] nfs = new ArrayList[chars.length];

    for (int i = 0; i < nfs.length; i++) {
      nfs[i] = new ArrayList<>();
    }

    for (int i = 0; i < chars.length - 1; i++) {
      if (chars[i] == ')') {
        int fp = stack.pop();
      }
      if (chars[i] == '(') {
        stack.push(i);
      }
      if (chars[i+1] == '*') {
        nfs[i+1].add(i);
      }
      nfs[i].add(i + 1);
    }

    return nfs;
  }

  public static boolean match(String re, String text) {
    String nfsText = "(" + re + ")";
    ArrayList<Integer>[] nfs = buildNfs(nfsText);
    Stack<Integer[]> stack = new Stack<>();

    char[] textChars = text.toCharArray();
    char[] nfsChars = nfsText.toCharArray();

    stack.push(new Integer[]{0, 0});
    while (!stack.isEmpty()) {
      Integer[] vals = stack.pop();
      int state = vals[0];
      int pos = vals[1];

      if (nfs[state].isEmpty()) {
        return true;
      }

      if (pos >= textChars.length) {
        return false;
      }

      char c = textChars[pos];

      boolean match = false;
      int nextPos = pos;
      if (c == nfsChars[state] || nfsChars[state] == '.' || nfsChars[state] == '*') {
        match = true;
        nextPos = pos + 1;
      } else if (nfsChars[state] == '(') {
        match = true;
      }

      if (match) {
        for (Integer j : nfs[state]) {
          stack.push(new Integer[]{j, nextPos});
        }
      } else {
        if (state <= 1) {
          stack.push(new Integer[]{0, pos + 1});
        } else {
          stack.push(new Integer[]{0, pos});
        }
      }
    }

    return false;
  }

  public static void main(String[] args) {
    String regex = "a.x*..b";
    String[] texts = new String[]{
      // "acb",
      // "acb",
      // "acxb",
      // "acbxb",
      "axxaab",
    };

    for (String txt : texts) {
      System.out.println(Regex.match(regex, txt));
    }
  }
}
