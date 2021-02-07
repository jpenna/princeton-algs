import java.util.ArrayList;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
  private final SET<Point2D> points;

  // construct an empty set of points
  public  PointSET() {
    points = new SET<Point2D>();
  }

  // is the set empty?
  public  boolean isEmpty() {
    return points.isEmpty();
  }

  // number of points in the set
  public int size() {
    return points.size();
  }

  // add the point to the set (if it is not already in the set)
  public  void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    points.add(p);
  }

  // does the set contain point p?
  public  boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return points.contains(p);
  }

  // draw all points to standard draw
  public  void draw() {
    for (Point2D point : points) {
      StdDraw.point(point.x(), point.y());
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    ArrayList<Point2D> list = new ArrayList<Point2D>();
    for (Point2D point : points) {
      if (rect.contains(point)) {
        list.add(point);
      }
    }
    return list;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public  Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    double dist = Double.POSITIVE_INFINITY;
    Point2D closestPoint = null;
    for (Point2D point : points) {
      double thisDist = point.distanceSquaredTo(p);
      if (thisDist < dist) {
        dist = thisDist;
        closestPoint = point;
      }
    }
    return closestPoint;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    StdDraw.setPenRadius(0.005);

    PointSET set = new PointSET();

    StdOut.printf("Size: %b (true)\n", set.isEmpty());
    StdOut.printf("Empty: %d (0)\n", set.size());
    set.insert(new Point2D(0.1, 0.2));
    set.insert(new Point2D(0.4, 0.5));
    set.insert(new Point2D(0.3, 0.5));
    StdOut.printf("Size: %d (3)\n", set.size());
    StdOut.printf("Empty: %b (false)\n", set.isEmpty());
    set.insert(new Point2D(0.5, 0.5));
    set.insert(new Point2D(0.1, 0.8));
    set.insert(new Point2D(0.3, 0.9));
    set.insert(new Point2D(0.2, 0.6));
    StdOut.printf("Size: %d (7)\n", set.size());

    StdOut.printf("Contains: %b (true)\n", set.contains(new Point2D(0.2, 0.6)));
    StdOut.printf("Contains: %b (false)\n", set.contains(new Point2D(0.1, 0.1)));

    set.draw();

    RectHV rect1 = new RectHV(0.05, 0.05, 0.31, 0.91);
    rect1.draw();
    StdOut.print("Rect 1 (5): ");
    for (Point2D point : set.range(rect1)) {
      StdOut.print(point);
    }
    StdOut.println();

    RectHV rect2 = new RectHV(0.7, 0.1, 0.9, 0.9);
    rect2.draw();
    StdOut.print("Rect 2 (empty): ");
    for (Point2D point : set.range(rect2)) {
      StdOut.print(point);
    }
    StdOut.println();

    StdOut.printf("Closest: %s (0.5, 0.5)\n", set.nearest(new Point2D(0.8, 0.5)));
    StdOut.printf("Closest: %s (0.4, 0.5)\n", set.nearest(new Point2D(0.4, 0.6)));
  }
}
