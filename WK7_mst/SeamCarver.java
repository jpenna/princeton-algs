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

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    if (hSeam != null)
      return hSeam;

    return new int[]{};
  }

  private int getNextY(int y, double[] pathWeight, int[][] paths, int smallestPath) {
    int smallest = smallestPath;

    // For each column from 1:w-1
    for (int x = 1; x < picture.width() - 1; x++) {
      // Select the next pixel with the smallest energy
      double smallestEnergy = Double.POSITIVE_INFINITY;
      int selectedX = -1;
      for (int diff = -1; diff <= 1; diff++) {
        double e = energy(x + diff, y);
        if (smallestEnergy > e) {
          smallestEnergy = e;
          selectedX = x + diff;
        }
      }
      // Increase the path
      pathWeight[x-1] += smallestEnergy;
      paths[x-1][y] = selectedX;
      // If current path is smaller the the previously selected, choose the current one
      if (pathWeight[smallest] > pathWeight[x-1]) {
        smallest = x-1;
      }
    }

    return smallest;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    if (vSeam != null)
      return vSeam;

    // Disconsider edges
    double[] pathWeight = new double[picture.width() - 2];
    int[][] paths = new int[pathWeight.length][picture.height()];

    for (int x = 0; x < pathWeight.length; x++) {
      paths[x][0] = x+1;
    }

    int smIndex = 0;
    for (int y = 1; y < picture.height() - 1; y++)
      smIndex = getNextY(y, pathWeight, paths, smIndex);

    int[] selected = paths[smIndex];
    // Last pixel is the same as pixel before last
    selected[selected.length - 1] = selected[selected.length - 2];

    return selected;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    Validation.validHorizontalSeam(picture, seam);
    // not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).
    resetCaches();
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    Validation.validVerticalSeam(picture, seam);
    // not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).
    resetCaches();
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture pic = new Picture("/Users/jpenna/Documents/princeton-algs/WK7_mst/samples/6x5.png");
    SeamCarver sc = new SeamCarver(pic);

    // /Users/jpenna/Documents/princeton-algs/WK7_mst/samples/3x4.png
    StdOut.println("Width (3): " + sc.width());
    StdOut.println("Height (4): " + sc.height());
    StdOut.println("Energy 0,0 (1000): " + sc.energy(0, 0));
    StdOut.println("Energy 1,1 (sqrt 52024 = 228.087702): " + sc.energy(1, 2));

    int[] vSeam = sc.findVerticalSeam();
    StdOut.println("Vertical Seam: ");
    for (int i : vSeam) {
      StdOut.printf("%d, ", i);
    }
  }
}
