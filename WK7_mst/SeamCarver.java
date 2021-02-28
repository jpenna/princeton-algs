import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private static final boolean VERTICAL = true;
  private static final boolean HORIZONTAL = false;

  private Picture picture;
  private final EnergyCalc energyCalc;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    Validation.notNull(picture);
    this.picture = new Picture(picture);
    energyCalc = new EnergyCalc(this.picture);
  }

  // current picture
  public Picture picture() {
    return picture;
  }

  // width of current picture
  public int width() {
    return picture.width();
  }

  // height of current picture
  public int height() {
    return picture.height();
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    Validation.validCoordinates(picture, x, y);

    // Edge pixels
    if (x == 0 || y == 0 || x == picture.width() - 1 || y == picture.height() - 1)
      return 1000;

    return energyCalc.calcEnergy(picture, x, y);
  }

  private void calcNext(int coord, boolean direction, double[] pathWeights, int[][] paths) {
    int lengthSkipLast = pathWeights.length - 1;

    // For each path, skipping edges
    for (int i = 1; i < lengthSkipLast; i++) {
      // Select the next pixel with the smallest energy
      double smallestEnergy = Double.POSITIVE_INFINITY;
      // Use the previous index of the current path to bound the next index
      int prev = paths[i][coord - 1];
      int selected = 0;
      for (int diff = -1; diff <= 1; diff++) {
        double e = direction == VERTICAL ? energy(prev + diff, coord) : energy(coord, prev + diff);
        if (smallestEnergy > e) {
          smallestEnergy = e;
          selected = prev + diff;
        }
      }
      // Increase the path weight and pixels
      pathWeights[i] += smallestEnergy;
      paths[i][coord] = selected;
    }
  }

  private int[] findSeam(boolean direction) {
    int w = picture.width();
    int h = picture.height();
    int dimension = direction == VERTICAL ? w : h;
    int pathSize = direction != VERTICAL ? w : h;

    double[] pathWeights = new double[dimension];
    int[][] paths = new int[dimension][pathSize];

    // Disconsider edges
    for (int i = 1; i < dimension - 1; i++) {
      paths[i][0] = i;
      pathWeights[i] = 1000; // starting edge
    }

    for (int i = 1; i < pathSize; i++)
      calcNext(i, direction, pathWeights, paths);

    int smIndex = 1;
    for (int i = 1; i < pathWeights.length - 1; i++) { // Skip edges
      if (pathWeights[smIndex] > pathWeights[i]) {
        smIndex = i;
      }
    }

    int[] selected = paths[smIndex];
    // Last pixel is the same as pixel before last
    if (selected.length > 1) {
      selected[selected.length - 1] = selected[selected.length - 2];
    }

    StdOut.println("Total weight: " + pathWeights[smIndex]);
    return selected;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    return findSeam(HORIZONTAL);
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    return findSeam(VERTICAL);
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    Validation.validHorizontalSeam(picture, seam);

    int w = picture.width();
    int h = picture.height();

    Picture newPicture = new Picture(w, h - 1);

    int prev = seam[0];
    for (int x = 0; x < w; x++) {
      int diff = 0;
      for (int y = 0; y < h; y++) {
        if (seam[x] == y) {
          if (y < prev - 1 || y > prev + 1) {
            throw new IllegalArgumentException(String.format("Seam coordinate not valid (Prev: %d, Curr: %d, Loop: %d, %d)", prev, y, x, y));
          }
          prev = y;
          diff = 1;
          continue;
        }
        newPicture.setRGB(x, y - diff, picture.getRGB(x, y));
      }
      prev = seam[x];
    }

    picture = newPicture;
    energyCalc.resetCache(picture);
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    Validation.validVerticalSeam(picture, seam);

    int w = picture.width();
    int h = picture.height();

    Picture newPicture = new Picture(w - 1, h);

    int prev = seam[0];
    for (int y = 0; y < h; y++) {
      int diff = 0;
      for (int x = 0; x < w; x++) {
        if (seam[y] == x) {
          if (x < prev - 1 || x > prev + 1) {
            throw new IllegalArgumentException(String.format("Seam coordinate not valid (Prev: %d, Curr: %d, Loop: %d, %d)", prev, x, x, y));
          }
          prev = x;
          diff = 1;
          continue;
        }
        newPicture.setRGB(x - diff, y, picture.getRGB(x, y));
      }
      prev = seam[y];
    }

    picture = newPicture;
    energyCalc.resetCache(picture);
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture pic = new Picture("/Users/jpenna/Documents/princeton-algs/WK7_mst/samples/7x10.png");
    SeamCarver sc = new SeamCarver(pic);

    // /Users/jpenna/Documents/princeton-algs/WK7_mst/samples/3x4.png
    StdOut.println("Width (3): " + sc.width());
    StdOut.println("Height (4): " + sc.height());
    StdOut.println("Energy 0,0 (1000): " + sc.energy(0, 0));
    // StdOut.println("Energy 1,1 (sqrt 52024 = 228.087702): " + sc.energy(1, 2));
    StdOut.println("Energy 0,3: " + sc.energy(0, 3));

    int[] vSeam = sc.findVerticalSeam();
    StdOut.print("Vertical Seam: ");
    for (int i : vSeam) {
      StdOut.printf("%d, ", i);
    }
    StdOut.println();

    int[] hSeam = sc.findHorizontalSeam();
    StdOut.print("Horizontal Seam: ");
    for (int i : hSeam) {
      StdOut.printf("%d, ", i);
    }
    StdOut.println();
  }
}
