import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final LineSegment[] segments;
  private int segmentsCount = 0;
  // private Segment curSegment = null;
  private Point[][] lines;

  // finds all line segments containing 4 or more points
  public FastCollinearPoints(Point[] pointsArg) {
    for (Point p : pointsArg) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
    }

    for (Point p : pointsArg) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
    }

    if (pointsArg.length <= 4) {
      segments = new LineSegment[0];
      return;
    }

    Point[] points = Arrays.copyOf(pointsArg, pointsArg.length);

    lines = new Point[points.length][2];

    for (int i = 0; i < points.length; i++) {
      Point p = pointsArg[i];

      Arrays.sort(points, p.slopeOrder());

      // If duplicate point throw illegal argument exception
      if (p.slopeTo(points[1]) == Double.NEGATIVE_INFINITY) {
        throw new IllegalArgumentException("Duplicated points");
      }

      double prevSlope = 0.0;
      int count = 0;
      for (int j = 1; j < points.length; j++) {
        double slope = p.slopeTo(points[j]);

        // initialize prevSlope
        if (count == 0) {
          prevSlope = slope;
          count = 1;
        // increment count if same group
        } else if (slope == prevSlope) {
          count += 1;
          if (j == points.length - 1) {
            computeSegment(points, count, j - count + 1, j + 1);
          }
        } else {
          computeSegment(points, count, j-count, j);
          prevSlope = slope;
          count = 1;
        }
      }
    }

    segments = new LineSegment[segmentsCount];
    for (int i = 0; i < segmentsCount; i++) {
      segments[i] = new LineSegment(lines[i][0], lines[i][1]);
    }
    this.lines = null;
  }

  private void computeSegment(Point[] points, int count, int start, int end) {
    if (count < 3) {
      return;
    }

    Point first = points[0];
    Point last = points[0];

    for (int i = start; i < end; i++) {
      Point p = points[i];
      int resFirst = first.compareTo(p);
      if (resFirst > 0) {
        first = p;
      }

      int resLast = last.compareTo(p);
      if (resLast < 0) {
        last = p;
      }
    }

    for (int i = 0; i < segmentsCount; i++) {
      if (lines[i][0] == first && lines[i][1] == last) {
        return;
      }
    }
    lines[segmentsCount] = new Point[]{first, last};
    segmentsCount++;
  }

  // the number of line segments
  public int numberOfSegments() {
    return segments.length;
  }

  // the line segments
  public LineSegment[] segments() {
    return Arrays.copyOf(segments, segments.length);
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
