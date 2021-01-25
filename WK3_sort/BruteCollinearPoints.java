import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
  private final LineSegment[] segments;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }

    for (Point p : points) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
    }

    LineSegment[] list = new LineSegment[points.length];
    int segmentsCount = 0;

    for (int i = 0; i < points.length; i++) {
      Point p = points[i];

      for (int j = i+1; j < points.length; j++) {
        if (p.compareTo(points[j]) == 0) {
          throw new IllegalArgumentException("Duplicated points");
        }

        for (int k = j+1; k < points.length; k++) {
          for (int m = k+1; m < points.length; m++) {
            double slope = p.slopeTo(points[j]);
            if (
              slope == p.slopeTo(points[k]) &&
              slope == p.slopeTo(points[m])
            ) {
              Point[] line = {p, points[j], points[k], points[m]};
              Arrays.sort(line);

              list[segmentsCount] = new LineSegment(line[0], line[3]);
              segmentsCount++;
            }
          }
        }
      }
    }

    segments = Arrays.copyOf(list, segmentsCount);
  }

  // the number of line segments
  public int numberOfSegments() {
    return segments.length;
  }

  // the line segments
  public LineSegment[] segments() {
    return Arrays.copyOf(this.segments, this.segments.length);
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
