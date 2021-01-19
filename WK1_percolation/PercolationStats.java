// Use StdRandom to generate random numbers; use StdStats to compute the sample
// mean and sample standard deviation.

// https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdRandom.html
// https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/StdStats.html
// https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/WeightedQuickUnionUF.html

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
  private final double[] counts;
  private final double trialsSqrRoot;
  private static final double CONFIDENCE_95 = 1.96;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    // IllegalArgumentException n <=0, trials <= 0
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("Trying to select a block outside range");
    }

    counts = new double[trials];
    trialsSqrRoot = Math.sqrt(trials);

    for (int i = 0; i < trials; i++) {
      Percolation perc = new Percolation(n);
      while (!perc.percolates()) {
        int row = StdRandom.uniform(n) + 1;
        int col = StdRandom.uniform(n) + 1;

        perc.open(row, col);
      }
      counts[i] = (double) perc.numberOfOpenSites() / (n * n);
    }
  }

    // sample mean of percolation threshold
  public double mean() {
    // Use StdRandom to generate random numbers; use StdStats to compute the sample
    // mean and sample standard deviation.
    return StdStats.mean(this.counts);
  }

    // sample standard deviation of percolation threshold
  public double stddev() {
    // Use StdRandom to generate random numbers; use StdStats to compute the sample
    // mean and sample standard deviation.
    return StdStats.stddev(this.counts);
  }

    // low endpoint of 95% confidence interval
  public double confidenceLo() {
    double meanCalc = this.mean();
    double std = this.stddev();
    return meanCalc - (CONFIDENCE_95 * std) / this.trialsSqrRoot;
  }

    // high endpoint of 95% confidence interval
  public double confidenceHi() {
    double meanCalc = this.mean();
    double std = this.stddev();
    return meanCalc + (CONFIDENCE_95 * std) / this.trialsSqrRoot;
  }

   // test client (see below)
  public static void main(String[] args) {
    // takes n: nxn grid, T: t experiments
    // prints the sample mean, sample standard deviation, and the 95%
    // confidence interval for the percolation threshold
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    Stopwatch watch = new Stopwatch();

    PercolationStats percStats = new PercolationStats(n, trials);

    double time = watch.elapsedTime() / trials;

    StdOut.printf("mean = %f%n", percStats.mean());
    StdOut.printf("stddev = %f%n", percStats.stddev());
    StdOut.printf("95%% confidence interval = [%f, %f]%n", percStats.confidenceLo(), percStats.confidenceHi());
    StdOut.printf("time = %f%n", time);
  }

}
