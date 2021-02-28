import edu.princeton.cs.algs4.Picture;

public class EnergyCalc {
  private EnergyCalc() {}

  private static int[] getRGB(Picture picture, int x, int y) {
    int rgb = picture.getRGB(x, y);
    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = (rgb >> 0) & 0xFF;

    return new int[] { r, g, b };
  }

  private static double calcCoordinateEnergy(int[] prev, int[] next) {
    double sum = 0.0;
    for (int i = 0; i < 3; i++) {
      int diff = next[i] - prev[i];
      sum += Math.pow(diff, 2);
    }
    return sum;
  }

  private static double getXEnergy(Picture picture, int x, int y) {
    int[] prev = getRGB(picture, x - 1, y);
    int[] next = getRGB(picture, x + 1, y);
    return calcCoordinateEnergy(prev, next);
  }

  private static double getYEnergy(Picture picture, int x, int y) {
    int[] prev = getRGB(picture, x, y - 1);
    int[] next = getRGB(picture, x, y + 1);
    return calcCoordinateEnergy(prev, next);
  }

  public static double calcEnergy(Picture picture, int x, int y) {
    // StdOut.println("x: " + getXEnergy(picture, x, y));
    // StdOut.println("y: " + getYEnergy(picture, x, y));
    // StdOut.println("sum: " + (getXEnergy(picture, x, y) + getYEnergy(picture, x, y)));
    return Math.sqrt(getXEnergy(picture, x, y) + getYEnergy(picture, x, y));
  }
}
