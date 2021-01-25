import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private LineSegment[] segmentsList;
  private int segmentsCount = 0;

  // finds all line segments containing 4 or more points
  public FastCollinearPoints(Point[] pointsArg) {
    if (pointsArg == null) {
      throw new IllegalArgumentException();
    }

    Point[] points = new Point[pointsArg.length];
    Point[] sortedPoints = new Point[pointsArg.length];
    for (int i = 0; i < pointsArg.length; i++) {
      if (pointsArg[i] == null) {
        throw new IllegalArgumentException();
      }
      points[i] = pointsArg[i];
      sortedPoints[i] = pointsArg[i];
    }

    // If duplicate point throw illegal argument exception
    Arrays.sort(sortedPoints);
    for (int i = 1; i < sortedPoints.length; i++) {
      if (sortedPoints[i].compareTo(sortedPoints[i-1]) == 0) {
        throw new IllegalArgumentException();
      }
    }

    if (pointsArg.length < 4) {
      segmentsList = new LineSegment[0];
      return;
    }

    segmentsList = new LineSegment[5];

    for (int i = 0; i < points.length; i++) {
      Point p = sortedPoints[i];

      Point[] pointsBySlope = sortedPoints.clone();
      Arrays.sort(pointsBySlope, p.slopeOrder());

      double prevSlope = 0.0;
      int count = 0;
      for (int j = 1; j < pointsBySlope.length; j++) {
        double slope = p.slopeTo(pointsBySlope[j]);

        // initialize prevSlope
        if (count == 0) {
          prevSlope = slope;
        }

        if (slope == prevSlope) {
          count += 1;
          if (j == pointsBySlope.length - 1) {
            computeSegment(pointsBySlope, count, j - count + 1, j + 1);
          }
        } else {
          computeSegment(pointsBySlope, count, j-count, j);
          prevSlope = slope;
          count = 1;
        }
      }
    }

    segmentsList = Arrays.copyOf(segmentsList, segmentsCount);
  }

  private void computeSegment(Point[] points, int count, int start, int end) {
    if (count < 3) {
      return;
    }

    Point first = points[0];
    Point last = points[end-1];

    if (first.compareTo(points[start]) > 0) {
      return;
    }

    if (segmentsList.length == segmentsCount) {
      segmentsList = Arrays.copyOf(segmentsList, segmentsList.length * 2);
    }
    segmentsList[segmentsCount] = new LineSegment(first, last);
    segmentsCount++;
  }

  // the number of line segments
  public int numberOfSegments() {
    return segmentsList.length;
  }

  // the line segments
  public LineSegment[] segments() {
    return Arrays.copyOf(segmentsList, segmentsList.length);
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
