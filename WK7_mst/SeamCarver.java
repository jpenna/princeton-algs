import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private Picture picture;
  private EnergyCalc energyCalc;
  private int[] hSeam = null;
  private int[] vSeam = null;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    Validation.notNull(picture);
    this.picture = new Picture(picture);
    energyCalc = new EnergyCalc(this.picture);
  }

  private void resetCaches() {
    energyCalc.resetCache(picture);
    hSeam = null;
    vSeam = null;
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

  private void getNext(int coord, boolean isVertical, double[] pathWeights, int[][] paths) {
    int lengthSkipLast = pathWeights.length - 1;

    // For each path, skipping edges
    for (int i = 1; i < lengthSkipLast; i++) {
      // Select the next pixel with the smallest energy
      double smallestEnergy = Double.POSITIVE_INFINITY;
      // Use the previous index of the current path to bound the next index
      int prev = paths[i][coord - 1];
      int selected = 0;
      for (int diff = -1; diff <= 1; diff++) {
        double e = isVertical ? energy(prev + diff, coord) : energy(coord, prev + diff);
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

  private int[] findSeam(boolean isVertical) {
    int dimension = isVertical ? picture.width() : picture.height();
    int pathSize = !isVertical ? picture.width() : picture.height();

    double[] pathWeights = new double[dimension];
    int[][] paths = new int[dimension][pathSize];

    // Disconsider edges
    for (int i = 1; i < dimension - 1; i++) {
      paths[i][0] = i;
      pathWeights[i] = 1000; // starting edge
    }

    for (int i = 1; i < pathSize; i++)
      getNext(i, isVertical, pathWeights, paths);

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

    // StdOut.println("Total weight: " + pathWeights[smIndex]);
    return selected;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    if (hSeam != null)
      return hSeam;

    hSeam = findSeam(false);

    return hSeam;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    if (vSeam != null)
      return vSeam;

    vSeam = findSeam(true);

    return vSeam;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    Validation.validHorizontalSeam(picture, seam);

    int w = picture.width();
    int h = picture.height();

    Picture newPicture = new Picture(w, h - 1);

    int prev = 0;
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        if (y < prev - 1 || y > prev + 1) {
          throw new IllegalArgumentException();
        }
        prev = y;
        if (seam[x] == y) continue;
        newPicture.setRGB(x, y, picture.getRGB(x, y));
      }
    }

    picture = newPicture;
    resetCaches();
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    Validation.validVerticalSeam(picture, seam);

    int w = picture.width();
    int h = picture.height();

    Picture newPicture = new Picture(w - 1, h);

    int prev = 0;
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        if (x < prev - 1 || x > prev + 1) {
          throw new IllegalArgumentException();
        }
        prev = x;
        if (seam[y] == x) continue;
        newPicture.setRGB(x, y, picture.getRGB(x, y));
      }
    }

    picture = newPicture;
    resetCaches();
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture pic = new Picture("/Users/jpenna/Documents/princeton-algs/WK7_mst/samples/12x10.png");
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
