package exercises;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

// Draft
class HuffmanCompress {
  private Node root = new Node();
  private final int A = 97;

  public HuffmanCompress() {
    char cur = 'a';
    for (byte i = 0; i < 26; i++) {
      Node x = root;
      for (byte j = 3; j >= 0; j--) {
        x = x.insert(readBit(cur - A, j));
      }
      x.val = cur;
      cur++;
    }
    // Add space
    cur = ' ';
    Node x = root;
    for (byte j = 3; j >= 0; j--) {
      x = x.insert(readBit(123 - A, j));
    }
    x.val = cur;
  }

  private class Node {
    private char val = '\0';
    private Node left = null;
    private Node right = null;

    private Node child(boolean b) {
      if (b) return this.right;
      return this.left;
    }

    private Node insert(boolean b) {
      if (b) {
        if (right == null) right = new Node();
        return right;
      }
      if (left == null) left = new Node();
      return left;
    }
  }

  private boolean readBit(int val, int pos) {
    return (val & (1 << pos)) != 0;
  }

  public String expand(byte[] compressed) {
    Node x = root;
    int pos = 0;
    int bitPos = 0;
    ArrayList<String> decompressed = new ArrayList<>();

    while (pos < compressed.length) {
      boolean bit = readBit(compressed[pos], bitPos);
      x = x.child(bit);
      bitPos++;

      if (x.val != '\0') {
        decompressed.add(String.valueOf(x.val));
        x = root;
      }
      if (bitPos == 7) {
        pos++;
        bitPos = 0;
      }
    }

    return String.join("", decompressed);
  }

  public byte[] compress(String str) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    for (int i = 0; i < str.length(); i += 2) {
      int c1 = str.charAt(i) & 0x0F;
      int c2 = 0;
      if (i+1 < str.length()) {
        c2 = str.charAt(i+1) & 0x0F;
      }
      stream.write((c1 << 4) | c2);
    }
    return stream.toByteArray();
  }


  public static void main(String[] args) {
    String str = "my name is juliano";
    HuffmanCompress huffman = new HuffmanCompress();
    byte[] compressed = huffman.compress(str);
    System.out.println("Compressd: " + compressed);
    System.out.println("Decompressed: " + huffman.expand(compressed));
  }
}
