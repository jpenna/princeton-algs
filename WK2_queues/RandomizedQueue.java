import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

// @SuppressWarnings("unchecked")
public class RandomizedQueue<Item> implements Iterable<Item> {
  private Object[] items = new Object[1];
  private int size = 0;

  private void resizeItems() {
    Object[] newItems = null;

    if (size == items.length) {
      newItems = new Object[items.length * 2];
    } else if (size <= items.length / 4) {
      newItems = new Object[items.length / 2];
    }

    if (newItems == null) {
      return;
    }

    for (int i = 0; i < size; i++) {
      newItems[i] = items[i];
    }

    items = newItems;
  }

  private int getRandomIndex() {
    return StdRandom.uniform(size);
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item item) {
    // Throw an IllegalArgumentException if the client calls enqueue() with a null argument.
    if (item == null) {
      throw new IllegalArgumentException();
    }
    items[size] = item;
    size += 1;
    resizeItems();
  }

  // remove and return a random item
  public Item dequeue() {
    // Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
    if (size == 0) {
      throw new NoSuchElementException();
    }
    int index = getRandomIndex();
    Item selected = (Item) items[index];
    if (index == size - 1) { // Is last
      items[index] = null;
    } else {
      items[index] = items[size - 1]; // Replace for last
    }
    size -= 1;
    resizeItems();
    return selected;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    // Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
    if (size == 0) {
      throw new NoSuchElementException();
    }
    int index = getRandomIndex();
    return (Item) items[index];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() { return new DequeIterator(); }

  private class DequeIterator implements Iterator<Item> {
    Object[] shuffledItems;
    int current = 0;

    DequeIterator() {
      shuffledItems = new Object[size];
      for (int i = 0; i < size; i++) {
        shuffledItems[i] = items[i];
      }
      StdRandom.shuffle(shuffledItems);
    }

    @Override
    public boolean hasNext() {
      return current < shuffledItems.length;
    }

    @Override
    public Item next() {
      // Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Item item = (Item) shuffledItems[current];
      current += 1;
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
    RandomizedQueue<Integer> rand = new RandomizedQueue<>();

    StdOut.println(rand.isEmpty());
    StdOut.println(rand.size());

    for (int i = 0; i < 50; i++) {
      rand.enqueue(i);
    }

    StdOut.println(rand.sample());
    StdOut.println(rand.sample());
    StdOut.println(rand.sample());
    StdOut.printf("Size: %d | Length: %d\n", rand.size(), rand.items.length);

    for (int i = 0; i < 25; i++) {
      rand.dequeue();
    }
    StdOut.printf("Size dequeue: %d\n", rand.items.length);
    StdOut.printf("Size dequeue: %d | Length: %d\n", rand.size(), rand.items.length);

    for (int i = 0; i < 15; i++) {
      rand.dequeue();
    }
    StdOut.printf("Size resized: %d | Length: %d\n", rand.size(), rand.items.length);

    Iterator<Integer> iteratorFirst = rand.iterator();
    Iterator<Integer> iteratorSecond = rand.iterator();

    String iterPrint = "Iterator %d: %d\n";
    StdOut.printf(iterPrint, 1, iteratorFirst.next());
    StdOut.printf(iterPrint, 1, iteratorFirst.next());
    StdOut.printf(iterPrint, 1, iteratorFirst.next());
    StdOut.printf(iterPrint, 2, iteratorSecond.next());
    StdOut.printf(iterPrint, 2, iteratorSecond.next());
    StdOut.printf(iterPrint, 2, iteratorSecond.next());

    for (int i = 0; i < 7; i++) {
      iteratorFirst.next();
    }

    StdOut.println(iteratorSecond.hasNext());
    StdOut.println(iteratorFirst.hasNext());
  }

}
