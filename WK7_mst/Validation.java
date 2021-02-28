import edu.princeton.cs.algs4.Picture;

public class Validation {
  private Validation() {}

  public static void notNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException();
    }
  }

  public static void validCoordinates(Picture picture, int x, int y) {
    if (x < 0 || y < 0 || x >= picture.width() || y >= picture.height()) {
      throw new IllegalArgumentException();
    }
  }

  public static void validHorizontalSeam(Picture picture, int[] seam) {
    notNull(seam);
    if (seam.length != picture.width() || picture.height() <= 1) {
      throw new IllegalArgumentException();
    }
  }

  public static void validVerticalSeam(Picture picture, int[] seam) {
    notNull(seam);
    if (seam.length != picture.height() || picture.width() <= 1) {
      throw new IllegalArgumentException();
    }
  }
}
