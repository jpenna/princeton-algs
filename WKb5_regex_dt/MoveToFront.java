import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static char[] buildAlphabet() {
        char[] alphabet = new char[256];
        for (char i = 33; i < alphabet.length; i++) {
            alphabet[i] = i;
        }
        return alphabet;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = buildAlphabet();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char i = 0;
            while (c != alphabet[0]) {
                i++;
                char a = alphabet[0];
                alphabet[0] = alphabet[i];
                alphabet[i] = a;
            }
            BinaryStdOut.write(i);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = buildAlphabet();

        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            char c = alphabet[i];

            for (char j = i; j > 0; j--) {
                alphabet[j] = alphabet[j - 1];
            }
            alphabet[0] = c;

            BinaryStdOut.write(c);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String op = args[0];
        if (op.equals("-")) {
            encode();
        } else if (op.equals("+")) {
            decode();
        }
    }

    // - < /Users/jpenna/Documents/princeton-algs/WKb5_regex_dt/samples/abra.txt > compressed
    // + < /Users/jpenna/Documents/princeton-algs/WKb5_regex_dt/compressed

}
