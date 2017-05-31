import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 *  Name:    Aditya Pillai
 *  Date:    01 May 2017
 *
 *  Description:    [description of program]
 *
 *  Compilation:    [compilation lines]
 *  Execution:      [execute lines]
 *  Dependencies:   [dependencies]
 *  Package:        PACKAGE_NAME
 *
 *  Copyright 2017 Aditya Pillai
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a 
 *  copy of this software and associated documentation files (the "Software"), 
 *  to deal in the Software without restriction, including without limitation 
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 *  and/or sell copies of the Software, and to permit persons to whom the 
 *  Software is furnished to do so, subject to the following conditions:
 *  The above copyright notice and this permission notice shall be included in 
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 *  IN THE SOFTWARE.
 ******************************************************************************/
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size = 0;
    private int capacity = 1;

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = (Item[]) new Object[capacity];
    }

    /**
     * is the queue empty?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * return the number of items on the queue
     * @return int
     */
    public int size() {
        return size;
    }

    /**
     * add the item
     *
     * @param item to be added
     */
    public void enqueue(Item item) {
        // if the item is null, throw exception
        if (item == null) throw new java.lang.NullPointerException();

        // check to resize array
        if (size + 1 > capacity) {
            resizeDouble();
        }

        // add item to end of array
        items[size++] = item;
    }

    private void resizeDouble() {
        capacity *= 2;
        Item[] newItems = (Item[]) new Object[capacity];
        int index = 0;
        for (Item i : items) {
            newItems[index++] = i;
        }
        items = newItems;
    }


    /**
     * remove and return random item
     *
     * @return Item
     */
    public Item dequeue() {
        // check to see if empty
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        // choose random int
        int randInt = StdRandom.uniform(size);

        // set items of random into the very last object
        Item ret = items[randInt];
        items[randInt] = items[--size];
        items[size] = null;

        // check to see if too big
        if (capacity / 4 > size) {
            resizeHalf();
        }

        return ret;
    }

    private void resizeHalf() {
        capacity /= 2;
        Item[] newItems = (Item[]) new Object[capacity];
        int index = 0;
        for (int i = 0; i < capacity; i++) {
            newItems[index++] = items[i];
        }
        items = newItems;
    }

    /**
     * return (but do not remove) a random item
     *
     * @return Item
     */
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();

        // give a random int
        return items[StdRandom.uniform(size)];
    }

    /**
     * returns iterator
     *
     * @return Iterator<Item>
     */
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    /**
     * necessary for iterable
     *
     */
    private class ListIterator implements Iterator<Item> {
        private int current = 0;
        private Item[] shuffledItems = (Item[]) new Object[size];

        public ListIterator() {
            int index = 0;
            for (int i = 0; i < size; i++) {
                shuffledItems[i] = items[i];
            }
            StdRandom.shuffle(shuffledItems);
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return shuffledItems[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * unit testing
     * @param args cmd line
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 50; i++) {
            randomizedQueue.enqueue((i + 1));
        }
        System.out.println(randomizedQueue.dequeue());
    }
}
