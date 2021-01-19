import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
  private Node first;
  private Node last;
  private int dequeSize = 0;

  private class Node {
    Item item;
    Node next;
    Node prev;

    public Node(Item item) {
      this.item = item;
    }
  }

  // is the deque empty?
  public boolean isEmpty() {
    return dequeSize == 0;
  }

  // return the number of items on the deque
  public int size() {
    return dequeSize;
  }

  // add the item to the front
  public void addFirst(Item item) {
    // Throw an IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
    if (item == null) {
      throw new IllegalArgumentException("Cannot call addFirst with null argument.");
    }

    Node node = new Node(item);

    if (this.first == null) {
      // If first == null, then last == null
      this.last = node;
    } else {
      node.next = this.first;
      this.first.prev = node;
    }

    dequeSize += 1;
    this.first = node;
  }

  // add the item to the back
  public void addLast(Item item) {
    // Throw an IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
    if (item == null) {
      throw new IllegalArgumentException("Cannot call addLast with null argument.");
    }

    Node node = new Node(item);

    if (this.first == null) {
      // If first == null, then last == null
      this.first = node;
    } else {
      node.prev = this.last;
      this.last.next = node;
    }

    dequeSize += 1;
    this.last = node;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    // Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
    if (this.isEmpty()) {
      throw new NoSuchElementException("Deque is empty.");
    }

    Node removed = this.first;

    this.first = removed.next;
    if (this.first == null) {
      this.last = null;
    } else {
      this.first.prev = null;
    }

    dequeSize -= 1;
    return removed.item;
  }

  // remove and return the item from the back
  public Item removeLast() {
    // Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
    if (this.isEmpty()) {
      throw new NoSuchElementException("Deque is empty.");
    }

    Node removed = this.last;

    this.last = removed.prev;
    if (this.last == null) {
      this.first = null;
    } else {
      this.last.next = null;
    }

    dequeSize -= 1;
    return removed.item;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {
    private Node current = first;

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      // Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      Item item = current.item;
      current = current.next;
      return item;
    }

    @Override
    public void remove() {
      // Throw an UnsupportedOperationException if the client calls the remove() method in the iterator.
      throw new UnsupportedOperationException();
    }
  }


  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<>();

    StdOut.println(deque.isEmpty());
    StdOut.println(deque.size());

    deque.addFirst(10);
    deque.addLast(11);
    deque.addFirst(9);
    deque.addLast(12);

    StdOut.println(deque.removeFirst());
    StdOut.println(deque.removeLast());

    deque.addLast(12);
    deque.addLast(13);
    deque.addLast(14);
    deque.addFirst(9);
    deque.addFirst(8);
    deque.addFirst(7);

    StdOut.print("For each... ");
    for (Integer i : deque) {
      StdOut.printf("%d, ", i);
    }
    StdOut.println();

    StdOut.println(deque.size());

    Iterator<Integer> dequeIter = deque.iterator();
    StdOut.println(dequeIter.hasNext());
    for (int i = 0; i < deque.size(); i++) {
      dequeIter.next();
    }
    StdOut.println(dequeIter.hasNext());

    // deque.addFirst(10);
    // StdOut.println(deque.removeFirst());

    // deque.addLast(11);
    // StdOut.println(deque.removeLast());

    // deque.addFirst(9);
    // deque.addLast(12);

  }
}
