import edu.princeton.cs.algs4.QuickUnionUF;

public class Percolation {
  private static final byte TOP = 1; // 001
  private static final byte BOTTOM = 4; // 100
  private static final byte OPEN = 2; // 010
  private static final byte CONNECTED = 7; // 111

  private final QuickUnionUF unionFind;
  private final int n;

  private int openBlocksCount = 0;
  private boolean doPercolate = false;
  private byte[] blocksStatus;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    // IllegalArgumentException if n <= 0
    if (n <= 0) {
      throw new IllegalArgumentException("Trying to construct Percolation with <= 0 blocks");
    }

    int size = n*n;

    this.n = n;
    this.unionFind = new QuickUnionUF(size);

    this.blocksStatus = new byte[size];
  }

  private void validate(int row, int col) {
    if (row > this.n || row <= 0 || col > this.n || col <= 0) {
      throw new IllegalArgumentException(
        String.format("Trying to select a block outside range. n: %d, row: %d, col: %d.", this.n, row, col)
      );
    }
  }

  private int getBlock(int row, int col) {
    return (row - 1) * this.n + (col - 1);
  }

  private int findRoot(int block) {
    int root = this.unionFind.find(block);
    if (root == block) {
      return block;
    }
    return findRoot(root);
  }

  private void updateConnections(int adjacent, int root) {
    int adjacentSet = this.findRoot(adjacent);
    int rootSet = this.findRoot(root); // make sure

    byte update = (byte) (this.blocksStatus[rootSet] | this.blocksStatus[adjacentSet]);
    this.blocksStatus[rootSet] = update;
    this.blocksStatus[adjacentSet] = update;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    this.validate(row, col);
    // O(1) + calls to union() find() constant
    int block = this.getBlock(row, col);

    if ((this.blocksStatus[block] & OPEN) != OPEN) {
      this.openBlocksCount += 1;
    } else {
      return;
    }

    int root = this.findRoot(block);

    int up = block - this.n;
    int right = block + 1;
    int down = block + this.n;
    int left = block - 1;

    // top
    if (row - 1 > 0) {
      if (this.isOpen(up)) {
        this.updateConnections(up, root);
        this.unionFind.union(up, root);
        root = this.findRoot(root);
      }
    } else {
      this.blocksStatus[root] = (byte) (this.blocksStatus[root] | TOP);
    }
    // right
    if (col < this.n && this.isOpen(right)) {
      this.updateConnections(right, root);
      this.unionFind.union(right, root);
      root = this.findRoot(root);
    }
    // bottom
    if (row < this.n) {
      if (this.isOpen(down)) {
        this.updateConnections(down, root);
        this.unionFind.union(down, root);
        root = this.findRoot(root);
      }
    } else {
      this.blocksStatus[root] = (byte) (this.blocksStatus[root] | BOTTOM);
    }
    // left
    if (col - 1 > 0 && this.isOpen(left)) {
      this.updateConnections(left, root);
      this.unionFind.union(left, root);
      root = this.findRoot(root);
    }

    this.blocksStatus[block] = (byte) (this.blocksStatus[root] | OPEN);

    if (this.blocksStatus[root] == CONNECTED) {
      this.doPercolate = true;
    }
  }

  private boolean isOpen(int block) {
    return (this.blocksStatus[block] & OPEN) == OPEN;
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    this.validate(row, col);
    // O(1) + calls to union() find() constant
    int block = this.getBlock(row, col);
    return this.isOpen(block);
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    this.validate(row, col);
    // O(1) + calls to union() find() constant
    int block = this.getBlock(row, col);
    return (this.blocksStatus[this.findRoot(block)] & TOP) == TOP;
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    // O(1) + calls to union() find() constant
    return this.openBlocksCount;
  }

  // does the system percolate?
  public boolean percolates() {
    return this.doPercolate;
  }
}
