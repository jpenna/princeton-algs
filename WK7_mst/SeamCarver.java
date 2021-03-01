import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private Picture picture;
  private final EnergyCalc energyCalc;
  private double minEnergy;
  private int[][] nextPixelV;
  private double[][] energyMatrix;
  private int lowerV;
  private int w;
  private int h;

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
    if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
      return 1000;

    return energyCalc.calcEnergy(picture, x, y);
  }

  private int getIndex(int x, int y) {
    return x + y*w;
  }

  private int[] getCoords(int index) {
    return new int[]{ index % w, index / w };
  }

  private void setEnergyV(int x, int y, int nextX, int nextY) {
    double treeEnergy = energyMatrix[nextX][nextY];
    double e = energy(x, y) + treeEnergy;
    if (e < energyMatrix[x][y]) {
      energyMatrix[x][y] = e;
      nextPixelV[x][y] = getIndex(nextX, nextY);
      if (y == 0 && e < energyMatrix[lowerV][0]) {
        lowerV = x;
      }
    }
  }

  private void processNextV(int x, int y, Queue<Integer> queue) {
    int yAbove = y - 1;
    if (yAbove < 0) return;

    for (int slide = -1; slide <= 1; slide++) {
      if (x+slide < 1 || x+slide >= w-1) {
        continue;
      }
      int index = getIndex(x + slide, yAbove);
      queue.enqueue(index);
      setEnergyV(x + slide, yAbove, x, y);
    }
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    w = width();
    h = height();
    nextPixelV = new int[w][h];
    energyMatrix = new double[w][h];
    Queue<Integer> queue = new Queue<>();
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        nextPixelV[i][j] = -1;
        energyMatrix[i][j] = Double.POSITIVE_INFINITY;
      }
    }
    energyMatrix[0][h - 1] = 0; // bottom-left pixel is fake...

    for (int col = 2; col < w; col += 2) {
      int y = h-1;
      queue.enqueue(getIndex(col, y));
      setEnergyV(col, y, 0, y);
      nextPixelV[col][y] = -1;

      while(!queue.isEmpty()) {
        int index = queue.dequeue();
        int[] coords = getCoords(index);
        processNextV(coords[0], coords[1], queue);
      }
    }

    int[] seam = new int[h];
    int next = lowerV;
    for (int i = 0; i < h; i++) {
      seam[i] = next;
      next = getCoords(nextPixelV[next][i])[0];
    }

    return seam;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    return new int[]{};
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

    // int[] hSeam = sc.findHorizontalSeam();
    // StdOut.printf("Horizontal Seam (%f): ", sc.minEnergy);
    // for (int i : hSeam) {
    //   StdOut.printf("%d, ", i);
    // }
    // StdOut.println();
  }
}
