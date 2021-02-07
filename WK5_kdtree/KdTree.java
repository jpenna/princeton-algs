import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
  private PointNode root;
  private int size = 0;

  // construct an empty set of points
  public KdTree() {
    // nothing
  }

  private class PointNode {
    private final Point2D point;
    private boolean isVertical;
    private PointNode left;
    private PointNode right;
    private double distance;
    private int pos;

    public PointNode(Point2D point) {
      pos = size;
      this.point = point;
      left = null;
      right = null;
      distance = 0.0;
    }
  }

  private class InsertTuple {
    private PointNode node;
    private boolean isLeft;

    public InsertTuple(PointNode node, boolean isLeft) {
      this.node = node;
      this.isLeft = isLeft;
    }
  }

  private void drawFrom(PointNode node) {
    if (node == null) {
      return;
    }
    StdDraw.point(node.point.x(), node.point.y());
    drawFrom(node.left);
    drawFrom(node.right);
  }

  private RectHV[] getRects(PointNode node, double xMin, double yMin, double xMax, double yMax) {
    RectHV lowerRect = null;
    RectHV higherRect = null;

    if (node.isVertical) {
      double div = node.point.x();
      if (node.left != null) {
        lowerRect = new RectHV(xMin, yMin, div, yMax);
      }
      if (node.right != null) {
        higherRect = new RectHV(div, yMin, xMax, yMax);
      }
    } else {
      double div = node.point.y();
      if (node.left != null) {
        lowerRect = new RectHV(xMin, yMin, xMax, div);
      }
      if (node.right != null) {
        higherRect = new RectHV(xMin, div, xMax, yMax);
      }
    }

    return new RectHV[] { lowerRect, higherRect };
  }

  private InsertTuple findInsertPosition(Point2D p) {
    PointNode current = root;
    PointNode parentNode = root;
    int order = 0;

    while (current != null) {
      if (p.equals(current.point)) {
        return null;
      }

      Comparator<Point2D> comparator = current.isVertical ? Point2D.X_ORDER : Point2D.Y_ORDER;
      order = comparator.compare(p, current.point);
      parentNode = current;
      if (order <= 0) {
        current = current.left;
      } else {
        current = current.right;
      }
    }

    return new InsertTuple(parentNode, order <= 0);
  }

  // is the set empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // number of points in the set
  public int size() {
    return size;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    if (root == null) {
      PointNode node = new PointNode(p);
      node.isVertical = true;
      root = node;
      size += 1;
      return;
    }

    InsertTuple tuple = findInsertPosition(p);

    if (tuple == null) {
      return;
    }

    PointNode node = new PointNode(p);
    node.isVertical = !tuple.node.isVertical;
    if (tuple.isLeft) tuple.node.left = node;
    else tuple.node.right = node;
    size += 1;
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return findInsertPosition(p) == null;
  }

  // draw all points to standard draw
  public void draw() {
    drawFrom(root);
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }

    ArrayList<Point2D> list = new ArrayList<>();
    return searchInRect(list, rect, root, new RectHV(0.0, 0.0, 1.0, 1.0));
  }

  private ArrayList<Point2D> searchInRect(
    ArrayList<Point2D> list,
    RectHV rect,
    PointNode node,
    RectHV nodeRect
  ) {
    if (node == null) {
      return list;
    }

    RectHV[] rects = getRects(node, nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
    RectHV lowerRect = rects[0];
    RectHV higherRect = rects[1];

    if (rect.contains(node.point)) {
      list.add(node.point);
      searchInRect(list, rect, node.left, lowerRect);
      searchInRect(list, rect, node.right, higherRect);
    } else {
      if (lowerRect != null && rect.intersects(lowerRect)) {
        searchInRect(list, rect, node.left, lowerRect);
      }
      if (higherRect != null && rect.intersects(higherRect)) {
        searchInRect(list, rect, node.right, higherRect);
      }
    }

    return list;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    if (root == null) return null;

    root.distance = Double.POSITIVE_INFINITY;
    PointNode node = findNearest(p, root, new RectHV(0.0, 0.0, 1.0, 1.0), root);
    return node.point;
  }

  private PointNode findNearest(Point2D p, PointNode node, RectHV nodeRect, PointNode nearest) {
    // print(node, nodeRect, p, nearest);
    RectHV[] rects = getRects(node, nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());

    node.distance = node.point.distanceSquaredTo(p);
    if (node.distance < nearest.distance) {
      nearest = node;
    }

    if (node.left == null && node.right == null) {
      return nearest;
    }

    PointNode firstNode = null;
    RectHV firstRect = null;

    PointNode secondNode = null;
    RectHV secondRect = null;

    double distLow = Double.POSITIVE_INFINITY;
    double distHigh = Double.POSITIVE_INFINITY;

    if (node.left != null) {
      distLow = rects[0].distanceSquaredTo(p);
    }
    if (node.right != null) {
      distHigh = rects[1].distanceSquaredTo(p);
    }

    if (node.right == null) { // go left
      if (distLow < nearest.distance) { // only if can be smaller
        firstNode = node.left;
        firstRect = rects[0];
      }
    } else if (node.left == null) { // go right
      if (distHigh < nearest.distance) { // only if can be smaller
        firstNode = node.right;
        firstRect = rects[1];
      }
    } else { // have both
      if (distLow == 0 || distLow < distHigh) { // go left first
        firstNode = node.left;
        firstRect = rects[0];
        secondNode = node.right;
        secondRect = rects[1];
      } else { // go right first
        firstNode = node.right;
        firstRect = rects[1];
        secondNode = node.left;
        secondRect = rects[0];
      }
    }

    if (firstNode != null) {
      // StdOut.print(node.pos + " Left: ");
      nearest = findNearest(p, firstNode, firstRect, nearest);
    }

    if (secondRect != null && secondRect.distanceSquaredTo(p) < nearest.distance) {
      // StdOut.print(node.pos + " Right: ");
      nearest = findNearest(p, secondNode, secondRect, nearest);
    }

    return nearest;
  }

  private void print(PointNode node, RectHV rect, Point2D p, PointNode nearest) {
    StdOut.println(node.pos);
    StdDraw.clear();
    StdDraw.setPenRadius(0.002);
    StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
    for (double x = 0.1; x <= 1.0; x += 0.1) {
      StdDraw.line(x, 0, x, 1);
    }
    for (double y = 0.1; y <= 1.0; y += 0.1) {
      StdDraw.line(0, y, 1, y);
    }

    StdDraw.setPenRadius(0.03);
    StdDraw.setPenColor(StdDraw.MAGENTA);
    nearest.point.draw();

    StdDraw.setPenRadius(0.01);

    StdDraw.setPenColor(StdDraw.RED);
    draw();
    StdDraw.setPenColor(StdDraw.GREEN);
    node.point.draw();
    StdDraw.setPenColor(StdDraw.BLUE);
    p.draw();

    StdDraw.setPenRadius(0.006);
    StdDraw.setPenColor(StdDraw.CYAN);
    rect.draw();
  }

  public static void main(String[] args) {
    // initialize the data structures from file
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }

    StdOut.printf("Contains: %b (true)\n", kdtree.nearest(new Point2D(0.48, 0.09)));

  }

  // unit testing of the methods (optional)
  // public static void main(String[] args) {
  //   StdDraw.setPenRadius(0.002);
  //   StdDraw.setPenColor(StdDraw.GRAY);
  //   for (double x = 0.1; x <= 1.0; x += 0.1) {
  //     StdDraw.line(x, 0, x, 1);
  //   }
  //   for (double y = 0.1; y <= 1.0; y += 0.1) {
  //     StdDraw.line(0, y, 1, y);
  //   }

  //   KdTree set = new KdTree();

  //   StdOut.printf("Size: %b (true)\n", set.isEmpty());
  //   StdOut.printf("Empty: %d (0)\n", set.size());
  //   set.insert(new Point2D(0.1, 0.2));
  //   set.insert(new Point2D(0.4, 0.5));
  //   set.insert(new Point2D(0.3, 0.5));
  //   set.insert(new Point2D(0.3, 0.5));
  //   StdOut.printf("Size: %d (3)\n", set.size());
  //   StdOut.printf("Empty: %b (false)\n", set.isEmpty());
  //   set.insert(new Point2D(0.5, 0.5));
  //   set.insert(new Point2D(0.1, 0.8));
  //   set.insert(new Point2D(0.3, 0.9));
  //   set.insert(new Point2D(0.2, 0.6));
  //   StdOut.printf("Size: %d (7)\n", set.size());
  //   set.insert(new Point2D(0.45, 0.55));

  //   StdOut.printf("Contains: %b (true)\n", set.contains(new Point2D(0.2, 0.6)));
  //   StdOut.printf("Contains: %b (false)\n", set.contains(new Point2D(0.1, 0.1)));

  //   StdDraw.setPenRadius(0.01);
  //   StdDraw.setPenColor(StdDraw.RED);
  //   set.draw();

  //   StdDraw.setPenRadius(0.005);
  //   StdDraw.setPenColor(StdDraw.BLACK);

  //   RectHV rect1 = new RectHV(0.05, 0.05, 0.31, 0.91);
  //   rect1.draw();

  //   StdOut.print("Rect 1 (5): ");
  //   for (Point2D point : set.range(rect1)) {
  //     StdOut.print(point);
  //   }
  //   StdOut.println();

  //   RectHV rect2 = new RectHV(0.7, 0.1, 0.9, 0.9);
  //   rect2.draw();
  //   StdOut.print("Rect 2 (empty): ");
  //   for (Point2D point : set.range(rect2)) {
  //     StdOut.print(point);
  //   }
  //   StdOut.println();

  //   StdOut.printf("Closest (0.45, 0.55): %s\n", set.nearest(new Point2D(0.5, 0.58)));
  //   StdOut.printf("Closest (0.5, 0.5): %s\n", set.nearest(new Point2D(0.8, 0.5)));
  //   StdOut.printf("Closest (0.4, 0.5): %s\n", set.nearest(new Point2D(0.4, 0.6)));
  // }
}
