/*
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: Create a custom realization of LinkedList and implement a set of standard operations.
 * The implementation must be covered with unit tests using JUnit 5.
 */
package io.hohichh;

import java.util.*;

/**
 * A custom implementation of a doubly-linked list, extending {@link AbstractSequentialList}
 * to simplify the implementation of index-based operations. This class manages the list's
 * structure through references to its head and tail nodes.
 *
 * @param <E> the type of elements held in this collection
 */
public class CustomLinkedList<E> extends AbstractSequentialList<E>
{
    private CustomNode<E> head;
    private CustomNode<E> tail;

    private int size;

    public CustomLinkedList() {}

    /**
     * Returns the number of elements in this list.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns the element at the specified position in this list.
     */
    @Override
    public E get(int index) {
        return super.get(index);
    }

    /**
     * Returns a list iterator over the elements in this list.
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new CustomIterator(index);
    }
    /**
     * Inserts the specified element at the beginning of this list.
     */
    @Override
    public void addFirst(E e){
        linkToHead(e);
    }
    /**
     * Appends the specified element to the end of this list.
     */
    @Override
    public void addLast(E e){
        linkToTail(e);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     */
    public void add(E e, int index) {
        super.add(index, e);
    }

    /**
     * Returns the first element in this list.
     */
    @Override
    public E getFirst(){
        return head.getEl();
    }
    /**
     * Returns the last element in this list.
     */
    @Override
    public E getLast(){
        return tail.getEl();
    }

    /**
     * Removes and returns the first element from this list.
     */
    @Override
    public E removeFirst(){
        E temp = head.getEl();
        unlinkHead();
        return temp;
    }
    /**
     * Removes and returns the last element from this list.
     */
    @Override
    public E removeLast(){
        E temp = tail.getEl();
        unlinkTail();
        return temp;
    }
    /**
     * Removes the element at the specified position in this list.
     */
    @Override
    public E remove(int index){
        return super.remove(index);
    }

    private void linkToTail(E e){
        CustomNode<E> newNode = new CustomNode<>(e);
        if(head == null){ //empty list
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    private void linkToHead(E e){
        CustomNode<E> newNode = new CustomNode<>(e);
        if (head == null){ //empty list
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    private void linkBefore(E e, CustomNode<E> node){
        CustomNode<E> newNode = new CustomNode<>(e);
        CustomNode<E> prev = node.getPrev();
        if(prev == null){
            linkToHead(e);
        } else {
            newNode.setNext(node);
            newNode.setPrev(prev);

            prev.setNext(newNode);
            node.setPrev(newNode);
            size++;
        }

    }

    private void unlinkHead(){
        CustomNode<E> newHead = head.next;
        if(newHead == null){
            head = tail = null;
        }else{
            newHead.prev = null;
            head.next = null;
            head = newHead;
        }
        size--;
    }

    private void unlinkTail(){
        CustomNode<E> newTail = tail.prev;
        if(newTail == null){
            head = tail = null;
        } else{
            newTail.next = null;
            tail.prev = null;
            tail = newTail;
        }
        size--;
    }


    private void unlink(CustomNode<E> node){
        CustomNode<E> prevUnlinked = node.prev;
        CustomNode<E> nextUnlinked = node.next;
        if(prevUnlinked == null){
            unlinkHead();
        }else if(nextUnlinked == null){
            unlinkTail();
        } else{
            prevUnlinked.setNext(nextUnlinked);
            nextUnlinked.setPrev(prevUnlinked);
            size--;
        }
        node = null;
    }

    private CustomNode<E> getNodeByIndex(int idx){
        if(idx == size){
            return null;
        }
        int lstSize = size();
        CustomNode<E> node;
        if (idx < (lstSize >> 1)) {
            node = head;
            for (int i = 0; i < idx; i++)
                node = node.getNext();
        } else {
            node = tail;
            for (int i = lstSize - 1; i > idx; i--)
                node = node.getPrev();
        }
        return node;
    }

    /**
     * An inner class that provides the core logic for traversing and modifying the list.
     * Its implementation of the {@link ListIterator} interface is essential for the parent
     * class, {@link AbstractSequentialList}, to function correctly.
     */
    private class CustomIterator implements ListIterator<E> {
        CustomNode<E> lastAccessed;
        CustomNode<E> next;
        int nextIndex;

        public CustomIterator(int idx){
            if(idx > size() || idx < 0){
                throw new IndexOutOfBoundsException();
            }
            next = getNodeByIndex(idx);
            nextIndex = idx;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }

        @Override
        public E next() {
            if(!hasNext()) throw new NoSuchElementException();

            lastAccessed = next;
            next = next.getNext();
            nextIndex++;
            return lastAccessed.getEl();
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if(!hasPrevious()) throw new NoSuchElementException();

            next = (next == null) ? tail : next.getPrev();
            lastAccessed = next;
            nextIndex--;
            return lastAccessed.getEl();
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastAccessed == null) throw new IllegalStateException();
            if (lastAccessed == next){ //if previous() was called before remove
                next = next.next;
            } else{
                nextIndex--;
            }
            unlink(lastAccessed);
            lastAccessed = null;
        }

        @Override
        public void set(E e) {
            if(lastAccessed == null) throw new IllegalStateException();
            lastAccessed.setEl(e);
        }

        @Override
        public void add(E e) {
            if(next == null){ //if list is empty, or oterator at the end of list
                linkToTail(e);
            } else{ //linking in the middle
                linkBefore(e, next);
            }
            lastAccessed = null;
            nextIndex++;
        }

    }

    /**
     * A static nested class representing a single node within the linked list.
     * Each node holds a reference to its data element and pointers to the previous
     * and next nodes in the sequence, forming the chain of the list.
     *
     * @param <E> The type of element stored in the node
     */
    private static class CustomNode<E> {
        private E el;
        private CustomNode<E> next;
        private CustomNode<E> prev;
        CustomNode(E el) {
            this.el = el;
        }

        public CustomNode<E> getPrev() {
            return prev;
        }

        public void setPrev(CustomNode<E> prev) {
            this.prev = prev;
        }

        public CustomNode<E> getNext() {
            return next;
        }

        public void setNext(CustomNode<E> next) {
            this.next = next;
        }

        public E getEl() {
            return el;
        }

        public void setEl(E el) {
            this.el = el;
        }
    }
}