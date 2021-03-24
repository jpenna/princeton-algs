# COMPILE
# javac BurrowsWheeler.java CircularSuffixArray.java MoveToFront.java -cp "/usr/local/lift/lib/algs4.jar:/usr/local/lift/lib/stdlib.jar"

SAMPLE_FILE="amendments.txt"

# Original
original="$(ls -lh samples/$SAMPLE_FILE | awk '{print $5}')"
echo "Original size: $original"
echo "-----------------"

# Huffman
start_time="$(gdate -u +%s%3N)"
  java-algs4 edu.princeton.cs.algs4.Huffman - < "samples/${SAMPLE_FILE}" > compressed
end_time="$(gdate -u +%s%3N)"
elapsedHuffman="$(($end_time - $start_time))"
size="$(ls -lh compressed | awk '{print $5}')"
echo "Compressed Huffman (${elapsedHuffman}ms): $size"

start_time="$(gdate -u +%s%3N)"
  java-algs4 edu.princeton.cs.algs4.Huffman + < compressed > /dev/null
end_time="$(gdate -u +%s%3N)"
decElapsedHuffman="$(($end_time - $start_time))"
echo "Decompress Huffman (${decElapsedHuffman}ms)"
echo "-----------------"

# Preprocessed
start_time="$(gdate -u +%s%3N)"
  java -cp "/usr/local/lift/lib/algs4.jar:/usr/local/lift/lib/stdlib.jar:." BurrowsWheeler - < "samples/${SAMPLE_FILE}" |
  java -cp "/usr/local/lift/lib/algs4.jar:/usr/local/lift/lib/stdlib.jar:." MoveToFront - |
  java-algs4 edu.princeton.cs.algs4.Huffman - > compressed
end_time="$(gdate -u +%s%3N)"
elapsedProcessed="$(($end_time - $start_time))"
size="$(ls -lh compressed | awk '{print $5}')"
echo "Compressed Preprocess (${elapsedProcessed}ms): $size"

start_time="$(gdate -u +%s%3N)"
  java-algs4 edu.princeton.cs.algs4.Huffman + < compressed |
  java -cp "/usr/local/lift/lib/algs4.jar:/usr/local/lift/lib/stdlib.jar:." MoveToFront + |
  java -cp "/usr/local/lift/lib/algs4.jar:/usr/local/lift/lib/stdlib.jar:." BurrowsWheeler + > /dev/null
end_time="$(gdate -u +%s%3N)"
decElapsedProcessed="$(($end_time - $start_time))"
echo "Decompress Preprocessed (${decElapsedProcessed}ms)"
echo "-----------------"

echo "Diff compress: $((($elapsedProcessed - $elapsedHuffman)))ms"
echo "Diff decompress: $((($decElapsedProcessed - $decElapsedHuffman)))ms"


# # java-algs4 edu.princeton.cs.algs4.HexDump 16
