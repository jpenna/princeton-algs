package queues;

import edu.princeton.cs.algs4.StdOut;

public class Queue_dstack<T> {
  Stack<T> stackAdd;
  Stack<T> stackPop;

  int countPush = 0;
  int countPop = 0;

  public Queue_dstack() {
    this.stackAdd = new Stack<>();
    this.stackPop = new Stack<>();
  }

  public void enqueue(T item) {
    this.stackAdd.push(item);
  }

  public T dequeue() {
    if (stackPop.isEmpty()) {
      if (stackAdd.isEmpty()) {
        throw new IndexOutOfBoundsException("The queue is empty!");
      }

      for (T item : this.stackAdd) {
        StdOut.printf("Push: %d\n", ++this.countPush);
        this.stackPop.push(item);
      }
      StdOut.printf("Amortized: %d\n", this.countPush / this.getSize());
    }

    StdOut.printf("Pop: %d\n", ++this.countPop);
    return stackPop.pop();
  }

  public int getSize() {
    return stackAdd.getSize() + stackPop.getSize();
  }

  public static void main(String[] args) {
    Queue_dstack<Integer> queue = new Queue_dstack<>();

    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    queue.dequeue();

    queue.enqueue(4);
    queue.enqueue(5);
    queue.enqueue(6);
    queue.dequeue();

    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    queue.dequeue();

    queue.enqueue(4);
    queue.enqueue(5);
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();

    queue.enqueue(6);
    queue.enqueue(1);
    queue.enqueue(2);
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();
    queue.enqueue(3);

    queue.enqueue(4);
    queue.enqueue(5);
    queue.enqueue(6);
    queue.dequeue();

    queue.enqueue(4);
    queue.enqueue(5);
    queue.enqueue(6);
    queue.enqueue(4);
    queue.enqueue(5);
    queue.enqueue(6);
    queue.enqueue(4);
    queue.enqueue(5);
    queue.enqueue(6);
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();
    queue.dequeue();
  }
}
