import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private static final double EDGE_ENERGY = 1000d;

  private Picture picture;
  private int[][] pathTo;
  private double[][] energyMatrix;
  private int width = 0;
  private int height = 0;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    notNull(picture);
    this.picture = new Picture(picture);
  }

  // current picture
  public Picture picture() {
    return new Picture(picture);
  }

  // width of current picture
  public int width() {
    return picture.width();
  }

  // height of current picture
  public int height() {
    return picture.height();
  }

  private void notNull(Object obj) {
    if (obj == null) {
      throw new IllegalArgumentException();
    }
  }

  private void validCoordinates(int x, int y) {
    if (x < 0 || x >= width() || y < 0 || y >= height()) {
      throw new IllegalArgumentException(String.format("Args: %d, %d", x, y));
    }
  }

  private void validHorizontalSeam(int[] seam) {
    notNull(seam);
    if (seam.length != width() || height() <= 1) {
      throw new IllegalArgumentException();
    }
  }

  private void validVerticalSeam(int[] seam) {
    notNull(seam);
    if (seam.length != height() || width() <= 1) {
      throw new IllegalArgumentException();
    }
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    validCoordinates(x, y);

    // Edge pixels
    if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
      return EDGE_ENERGY;

    int xp = picture.getRGB(x - 1, y);
    int xf = picture.getRGB(x + 1, y);
    int yp = picture.getRGB(x, y - 1);
    int yf = picture.getRGB(x, y + 1);

    double sumX = 0;
    double sumY = 0;
    for (int shift = 0; shift <= 16; shift += 8) {
      sumX += Math.pow((double) ((xf >> shift) & 0xFF) - ((xp >> shift) & 0xFF), 2);
      sumY += Math.pow((double) ((yf >> shift) & 0xFF) - ((yp >> shift) & 0xFF), 2);
    }

    return Math.sqrt(sumX + sumY);
  }

  private void initializeData() {
    width = width();
    height = height();
    pathTo = new int[height][width];
    energyMatrix = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        energyMatrix[i][j] = Double.POSITIVE_INFINITY;
      }
    }
  }

  private void relaxVertical(int row, int col) {
    double e = energy(col, row);
    int[] cols = {col - 1, col, col + 1};
    double[] candidates = {
      cols[0] > 0 ? energyMatrix[row - 1][cols[0]] : Double.POSITIVE_INFINITY,
      energyMatrix[row - 1][cols[1]],
      cols[2] < width ? energyMatrix[row - 1][cols[2]] : Double.POSITIVE_INFINITY,
    };

    int small = 0;
    if (candidates[1] < candidates[small]) small = 1;
    if (candidates[2] < candidates[small]) small = 2;

    if (e + candidates[small] < energyMatrix[row][col]) {
      energyMatrix[row][col] = e + candidates[small];
      pathTo[row][col] = cols[small];
    }
  }

  private void relaxHorizontal(int row, int col) {
    double e = energy(col, row);
    int[] rows = {row - 1, row, row + 1};
    double[] candidates = {
      rows[0] > 0 ? energyMatrix[rows[0]][col - 1] : Double.POSITIVE_INFINITY,
      energyMatrix[rows[1]][col - 1],
      rows[2] < height ? energyMatrix[rows[2]][col - 1] : Double.POSITIVE_INFINITY,
    };

    int small = 0;
    if (candidates[1] < candidates[small]) small = 1;
    if (candidates[2] < candidates[small]) small = 2;

    if (e + candidates[small] < energyMatrix[row][col]) {
      energyMatrix[row][col] = e + candidates[small];
      pathTo[row][col] = rows[small];
    }
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    initializeData();

    for (int col = 0; col < width; col++) {
      energyMatrix[0][col] = EDGE_ENERGY; // Initialize first row
    }

    for (int row = 1; row < height; row += 1) {
      energyMatrix[row][0] = Double.POSITIVE_INFINITY;
      energyMatrix[row][width - 1] = Double.POSITIVE_INFINITY;
      for (int col = 1; col < width - 1; col += 1) {
        relaxVertical(row, col);
      }
    }

    double leastEnergy = Double.POSITIVE_INFINITY;
    int index = 0;
    for (int col = 0; col < width; col++) {
      if (energyMatrix[height - 1][col] < leastEnergy) {
        leastEnergy = energyMatrix[height - 1][col];
        index = col;
      }
    }

    int[] seam = new int[height];
    for (int row = height - 1; row >= 0; row--) {
      seam[row] = index;
      index = pathTo[row][index];
    }

    energyMatrix = null;
    pathTo = null;
    return seam;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    initializeData();

    for (int row = 0; row < height; row++) {
      energyMatrix[row][0] = EDGE_ENERGY; // Initialize first column
    }

    for (int col = 1; col < width; col += 1) {
      energyMatrix[0][col] = Double.POSITIVE_INFINITY;
      energyMatrix[height - 1][col] = Double.POSITIVE_INFINITY;
      for (int row = 1; row < height - 1; row += 1) {
        relaxHorizontal(row, col);
      }
    }

    double leastEnergy = Double.POSITIVE_INFINITY;
    int index = 0;
    for (int row = 0; row < height; row++) {
      if (energyMatrix[row][width - 1] < leastEnergy) {
        leastEnergy = energyMatrix[row][width - 1];
        index = row;
      }
    }

    int[] seam = new int[width];
    for (int col = width - 1; col >= 0; col--) {
      seam[col] = index;
      index = pathTo[index][col];
    }

    energyMatrix = null;
    pathTo = null;
    return seam;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    width = width();
    height = height();
    validHorizontalSeam(seam);

    Picture newPicture = new Picture(width, height - 1);

    int prev = seam[0];
    for (int x = 0; x < width; x++) {
      int diff = 0;
      for (int y = 0; y < height; y++) {
        if (seam[x] == y) {
          // Validate seam
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
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    width = width();
    height = height();
    validVerticalSeam(seam);

    Picture newPicture = new Picture(width - 1, height);

    int prev = seam[0];
    for (int y = 0; y < height; y++) {
      int diff = 0;
      for (int x = 0; x < width; x++) {
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
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture pic = new Picture("/Users/jpenna/Documents/princeton-algs/WK7_mst/samples/10x12.png");
    SeamCarver sc = new SeamCarver(pic);

    // /Users/jpenna/Documents/princeton-algs/WK7_mst/samples/3x4.png
    StdOut.println("Width (3): " + sc.width());
    StdOut.println("Height (4): " + sc.height());
    StdOut.println("Energy 0,0 (1000): " + sc.energy(0, 0));
    // StdOut.println("Energy 1,1 (sqrt 52024 = 228.087702): " + sc.energy(1, 2));
    // StdOut.println("Energy 0,3: " + sc.energy(0, 3));

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
