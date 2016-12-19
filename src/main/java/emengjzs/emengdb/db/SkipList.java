/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import emengjzs.emengdb.util.Validate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by emengjzs on 2016/8/28.
 */
public class SkipList<E> {

    private final static int MAX_HEIGHT = 16;

    /**
     * the max height of current list, not more than MAX_HIGHT
     */
    private volatile int height;

    /**
     * dummy head of the skipList.
     */
    private final Node<E> head;

    /**
     * cmp func to make elements in order
     */
    private final Comparator<E> cmp;

    private final Random random;

    public SkipList(Comparator<E> cmp) {
        this.cmp = cmp;
        this.height = 1;
        this.random = new Random();
        this.head = new Node<E>(null, MAX_HEIGHT);
        for (int i = 0; i < MAX_HEIGHT; ++i) {
            head.setNext(i, null);
        }
    }

    private boolean isKeyAfterNode(E key, Node<E> node) {
        // assume that a null node has key that greater than any another key.
        return (node != null) && (cmp.compare(node.key, key) < 0);
    }


    public boolean contains(E key) {
        Node<E> a;
        return (a = findGreaterOrEqual(key)) != null && cmp.compare(a.key, key) == 0;
    }

    public void insert(E key) {
        Node<E> prev[] = new Node[MAX_HEIGHT];
        Node<E> x = findGreaterOrEqual(key, prev);
        Validate.isTrue(x == null || cmp.compare(key, x.key) != 0);


        int nodeHeight = getRandomHeight();
        x = new Node<E>(key, nodeHeight);


        if (nodeHeight > this.height) {

            for (int i = height; i < nodeHeight; i++) {
                prev[i] = head;
            }

            this.height = nodeHeight;
        }

        for (int i = 0; i < nodeHeight; i++) {
            x.setNext(i, prev[i].getNext(i));
            prev[i].setNext(i, x);
        }


    }

    // height in [1, MAX_HEIGHT]
    private int getRandomHeight() {
        int height = 1;
        while (height < MAX_HEIGHT && random.nextDouble() < 0.25) {
            height++;
        }
        return height;
    }

    private Node<E> findGreaterOrEqual(E key) {
        return findGreaterOrEqual(key, null);
    }


    private Node<E> findGreaterOrEqual(E key, Node[] prev) {
        Node<E> current = head;
        Node<E> next = null;
        int index = height - 1;
        while (true) {
            // still ok when height has increased.
            next = current.getNext(index);
            if (isKeyAfterNode(key, next)) {
                current = next;
            } else {
                if (prev != null) {
                    prev[index] = current;
                }
                if (index == 0) {
                    return next;
                } else {
                    index--;
                }
            }
        }
    }

    private Node<E> findLessThan(E key) {
        Node<E> current = head;
        Node<E> next = null;
        int index = height - 1;
        while (true) {
            next = current.getNext(index);
            if (isKeyAfterNode(key, next)) {
                current = next;
            } else {
                if (index == 0) {
                    return current;
                } else {
                    index--;
                }
            }
        }
    }

    // return head if list is empty
    private Node<E> findLast() {
        Node<E> cur = head;
        Node<E> next = null;
        int index = height - 1;
        while (true) {
            if ((next = cur.getNext(0)) != null) {
                cur = next;
            } else {
                if (index == 0) {
                    return cur;
                } else {
                    index--;
                }
            }
        }
    }



    private static class Node<E> {
        // final constraints make sure that the object is fully visible after
        // construct
        final E key;
        final AtomicReferenceArray<Node<E>> next;

        Node(E key, int height) {
            this.key = key;
            this.next = new AtomicReferenceArray<Node<E>>(height);
        }


        void setNext(int i, Node<E> node) {
            next.set(i, node);
        }

        void lazySetNext(int i, Node<E> node) {
            next.lazySet(i, node);
        }

        Node<E> getNext(int i) {
            return next.get(i);
        }
    }


    public ListIterator<E> listIterator() {
        return new SkipListIterator();
    }

    /**
     * return a iterator where the next key is equal or greater than the key.
     * if such key is not exited, the return value of next() is null.
     * @param key
     * @return
     */
    public ListIterator<E> listIterator(E key) {
        return new SkipListIterator(findLessThan(key));
    }

    public class SkipListIterator implements ListIterator<E> {

        // private int index;
        private Node<E> current;

        SkipListIterator() {
            // index = 0;
            current = head;
        }

        SkipListIterator(Node<E> current) {
            this.current = current;
        }


        @Override
        public boolean hasNext() {
            return current.getNext(0) != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            // index++;
            current = current.getNext(0);
            return current.key;
        }

        @Override
        public boolean hasPrevious() {
            return current != head && current != head.getNext(0);
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            current = findLessThan(current.key);
            return current.key;
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * must invoke hasNext() , next() to get the first element
         */
        public void seekToFirst() {
            // index = 0;
            current = head;
        }

        public void seekToLast() {

        }
    }


    public List<E> toList() {
        LinkedList<E> l = new LinkedList<E>();
        Node<E> c = this.head.getNext(0);
        while (c != null) {
            l.add(c.key);
            c = c.getNext(0);
        }
        return l;
    }

}
