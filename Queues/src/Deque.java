import java.util.Iterator;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    30 April 2017
 *
 *  Description:    Singly-linked list with functions for add to front and end,
 *                  remove from front and end, and iteration.
 *
 *  Compilation:    javac-algs4 Deque.java
 *  Execution:      java-algs4 Deque
 *  Dependencies:   algs4.jar
 *  Package:        default
 *
 *  Copyright 2017 Aditya Pillai
 *
 ******************************************************************************/
@SuppressWarnings({"WeakerAccess", "unused"})
public class Deque<Item> implements Iterable<Item> {


    private int size = 0;
    private Node first = null;
    private Node last = null;

    /**
     * constructs an empty deque
     */
    public Deque() {
        // nothing needed
    }


    /**
     * is the deque empty?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * return the number of items on the deque
     *
     * @return int
     */
    public int size() {
        return size;
    }


    /**
     * add the item to the front
     *
     * @param item to be added
     */
    public void addFirst(Item item) {
        // check if valid then add 1
        checkValidItem(item);
        size++;

        // if the size = 1, then the first and last should be the same item
        if (size == 1) {
            first = new Node();
            first.item = item;
            last = first;
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.next = oldFirst;
            first.item = item;
            oldFirst.previous = first;
        }
    }

    /**
     * checks if Item is null
     * @param item to be checked
     */
    private void checkValidItem(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
    }

    /**
     * add item ot the end
     *
     * @param item to be added
     */
    public void addLast(Item item) {
        // check if valid then add 1
        checkValidItem(item);
        size++;

        // if the size is 1, then first and last should be the same item
        if (size == 1) {

            first = new Node();
            first.item = item;
            last = first;

        }
        else { // else, swap the old first with the new first

            Node oldLast = last;
            last = new Node();
            last.previous = oldLast;
            last.item = item;
            oldLast.next = last;

        }

    }

    /**
     * remove and return the item from the front
     *
     * @return Item
     */
    public Item removeFirst() {
        checkValidRemove();

        // create newNext object
        Node newNext = first.next;

        // save first object
        Item ret = first.item;

        // set first to the next
        first = null;
        first = newNext;

//        // set the previous to null
//        first.previous = null;

        // remove one from size
        size--;
        
        // return Item
        return ret;
    }

    /**
     * checks if item can be removed from deque
     */
    private void checkValidRemove() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
    }


    /**
     * remove and return the item from the end
     *
     * @return Item
     */
    public Item removeLast() {
        checkValidRemove();

        // create newLast object
        Node newLast = last.previous;

        // save item to return
        Item ret = last.item;

        // set newLast to last
        last = null;
        last = newLast;

//        // set the next to null
//        last.next = null;

        // remove size
        size--;
        
        // return Item
        return ret;
    }

    /**
     * return an iterator over items in order
     * @return Iterator of Item
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    /**
     * creates a listiterator for iterable interface
     */
    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {

            // checks if has next
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    /**
     * unit testing
     * @param args cmd-line
     */
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 50; i++) {
            deque.addLast((i + 1));
        }
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        for (Integer i :
            deque) {
            System.out.println(i);
        }

    }


    /**
     * Node for each position
     */
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
}
