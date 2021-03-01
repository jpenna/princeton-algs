import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private Picture picture;
  private final EnergyCalc energyCalc;
  private double minEnergy;
  private int[] minPath;
  private int[] path;
  private int endScan;
  private int endPath;
  private boolean isVertical;
  private int[] sides = new int[]{-1, 1, 0};

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

  private void findNext(int scanVal, int pathCursor, double pathEnergy) {
    // Do not consider paths going through edges
    if (pathEnergy >= minEnergy) {
      return;
    }
    if (pathCursor == endPath) {
      minEnergy = pathEnergy; // minEnergy will be smaller
      minPath = path.clone();
      return;
    }
    if (pathCursor > endPath) {
      throw new IllegalArgumentException("pathCursor should never be bigger than height/width, because it should stop earlier.");
    }
    for (int side : sides) {
      int scanIndex = scanVal+side;

      if (scanIndex >= endScan || scanIndex < 1) continue;

      int x = isVertical ? scanIndex : pathCursor;
      int y = isVertical ? pathCursor : scanIndex;
      path[pathCursor] = scanIndex;

      findNext(scanIndex, pathCursor+1, pathEnergy + energy(x, y));
    }
  }

  private int[] findSeam() {
    int w = picture.width();
    int h = picture.height();

    endScan = isVertical ? w : h;
    endPath = isVertical ? h : w;

    path = new int[endPath];
    minPath = new int[endPath];
    minEnergy = Double.POSITIVE_INFINITY;

    for (int i = 2; i < endScan - 1; i += 3) {
      findNext(i, 0, 0);
    }

    return minPath;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    isVertical = false;
    return findSeam();
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    isVertical = true;
    return findSeam();
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
    StdOut.printf("Vertical Seam (%f): ", sc.minEnergy);
    for (int i : vSeam) {
      StdOut.printf("%d, ", i);
    }
    StdOut.println();

    int[] hSeam = sc.findHorizontalSeam();
    StdOut.printf("Horizontal Seam (%f): ", sc.minEnergy);
    for (int i : hSeam) {
      StdOut.printf("%d, ", i);
    }
    StdOut.println();
  }
}
