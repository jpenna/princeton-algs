import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
  private Picture picture;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    Validation.notNull(picture);
    this.picture = new Picture(picture);
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

    return EnergyCalc.calcEnergy(picture, x, y);
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    return new int[]{};
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    return new int[] {};
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    Validation.validHorizontalSeam(picture, seam);
    // not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    Validation.validVerticalSeam(picture, seam);
    // not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).

  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture pic = new Picture("/Users/jpenna/Documents/princeton-algs/WK7_mst/samples/3x4.png");
    SeamCarver sc = new SeamCarver(pic);

    // /Users/jpenna/Documents/princeton-algs/WK7_mst/samples/3x4.png
    StdOut.println("Width (3): " + sc.width());
    StdOut.println("Height (4): " + sc.height());
    StdOut.println("Energy 0,0 (1000): " + sc.energy(0, 0));
    StdOut.println("Energy 1,1 (sqrt 52024 = 228.087702): " + sc.energy(1, 2));
  }
}
