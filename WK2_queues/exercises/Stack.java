package queues;

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<T> implements Iterable<T> {
  private Node top;
  private int size = 0;

  private class Node {
    Node prev;
    T item;

    public Node(T elem) {
      this.item = elem;
    }
  }

  public Iterator<T> iterator() {
    return new StackIterator(this);
  }

  class StackIterator implements Iterator<T> {
    Stack<T> stack;

    StackIterator(Stack<T> stackArg) {
      stack = stackArg;
    }

    public boolean hasNext() {
      return !stack.isEmpty();
    }

    public T next() {
      if (stack.isEmpty()) {
        throw new NoSuchElementException("Stack is empty");
      }
      return stack.pop();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void push(T elem) {
    Node node = new Node(elem);
    node.prev = this.top;
    this.top = node;
    this.size += 1;
  }

  public T pop() {
    if (this.isEmpty()) {
      throw new IndexOutOfBoundsException("The stack is empty!");
    }
    Node popped = this.top;
    this.top = popped.prev;
    this.size -= 1;
    return popped.item;
  }

  public int getSize() {
    return this.size;
  }

  public static void main(String[] args) {
    Stack<Integer> stack = new Stack<>();

    // StdOut.printf("Is empty: %b\n", stack.isEmpty());
    // stack.push(1);
    // stack.push(2);
    // stack.push(3);
    // stack.push(4);
    // StdOut.printf("#4: %d\n", stack.pop());
    // StdOut.printf("#3: %d\n", stack.pop());
    // stack.push(5);
    // stack.push(6);
    // StdOut.printf("#6: %d\n", stack.pop());
    // stack.push(7);
    // StdOut.printf("#7: %d\n", stack.pop());
    // StdOut.printf("#5: %d\n", stack.pop());
    // StdOut.printf("Is empty: %b\n", stack.isEmpty());
    // StdOut.printf("#2: %d\n", stack.pop());
    // StdOut.printf("#1: %d\n", stack.pop());
    // StdOut.printf("Fail\n", stack.pop());

    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(4);
    stack.push(5);
    stack.push(6);
    stack.push(7);

    for (Integer i : stack) {
      StdOut.printf("%d\n", i);
    }
  }
}
