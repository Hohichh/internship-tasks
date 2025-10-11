/*
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: A custom implementation of a doubly-linked list in Java.
 *
 * This file contains the CustomLinkedList class and its necessary inner classes
 * (CustomIterator, CustomNode) to provide a complete and functional list data structure.
 */
package io.hohichh;

import java.util.*;

/**
 * A custom implementation of a doubly-linked list.
 * This class extends {@link AbstractSequentialList} to minimize the implementation effort.
 * Most of the random-access methods (like {@code get(int)}, {@code add(int, E)}, {@code remove(int)})
 * are implemented by the parent class by leveraging the {@link #listIterator(int)} method,
 * which is the core of this implementation.
 *
 * <p>This implementation is not synchronized.
 *
 * @param <E> the type of elements held in this collection
 */
public class CustomLinkedList<E> extends AbstractSequentialList<E> {
    /**
     * Pointer to the first node of the list.
     */
    private CustomNode<E> head;
    /**
     * Pointer to the last node of the list.
     */
    private CustomNode<E> tail;

    /**
     * The number of elements in the list.
     */
    private int size;

    /**
     * Constructs an empty list.
     */
    public CustomLinkedList() {}

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns the element at the specified position in this list.
     * This implementation delegates to the parent class, which uses the list iterator.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E get(int index) {
        return super.get(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence),
     * starting at the specified position in the list.
     *
     * @param index index of the first element to be returned from the list iterator (by a call to next)
     * @return a ListIterator of the elements in this list (in proper sequence), starting at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new CustomIterator(index);
    }

    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    @Override
    public void addFirst(E e){
        linkToHead(e);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e the element to add
     */
    @Override
    public void addLast(E e){
        linkToTail(e);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * This implementation delegates to the parent class, which uses the list iterator.
     *
     * @param index index at which the specified element is to be inserted
     * @param e element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    public void add(E e, int index) {
        super.add(index, e);
    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public E getFirst(){
        if (head == null) throw new NoSuchElementException();
        return head.getEl();
    }
    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public E getLast(){
        if (tail == null) throw new NoSuchElementException();
        return tail.getEl();
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public E removeFirst(){
        if (head == null) throw new NoSuchElementException();
        E temp = head.getEl();
        unlinkHead();
        return temp;
    }
    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
    @Override
    public E removeLast(){
        if (tail == null) throw new NoSuchElementException();
        E temp = tail.getEl();
        unlinkTail();
        return temp;
    }

    /**
     * Removes the element at the specified position in this list.
     * This implementation delegates to the parent class, which uses the list iterator.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @Override
    public E remove(int index){
        return super.remove(index);
    }

    /**
     * Links e as the last element.
     * @param e the element to link
     */
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

    /**
     * Links e as the first element.
     * @param e the element to link
     */
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

    /**
     * Inserts element e before non-null Node node.
     * @param e the element to link
     * @param node the node to link before
     */
    private void linkBefore(E e, CustomNode<E> node){
        CustomNode<E> newNode = new CustomNode<>(e);
        CustomNode<E> prev = node.getPrev();
        // If the node to link before is the head, this becomes a linkToHead operation
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

    /**
     * Unlinks the first node (head) of the list. Assumes head is not null.
     */
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

    /**
     * Unlinks the last node (tail) of the list. Assumes tail is not null.
     */
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

    /**
     * Unlinks a non-null node from the list. Handles cases where the node is the head or tail.
     * @param node the node to unlink
     */
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
        node = null; // Help GC
    }

    /**
     * Returns the Node at the specified element index.
     * This method contains an optimization: it traverses from the beginning
     * or the end of the list, whichever is closer to the specified index.
     *
     * @param idx index of the node to return
     * @return the node at the specified index
     */
    private CustomNode<E> getNodeByIndex(int idx){
        // A special case for the iterator to get a cursor position at the end.
        if(idx == size){
            return null;
        }

        CustomNode<E> node;
        if (idx < (size >> 1)) { // Check if index is in the first half
            node = head;
            for (int i = 0; i < idx; i++)
                node = node.getNext();
        } else { // Index is in the second half
            node = tail;
            for (int i = size - 1; i > idx; i--)
                node = node.getPrev();
        }
        return node;
    }

    /**
     * An iterator for the CustomLinkedList.
     * This private inner class provides the core logic for traversing and modifying the list.
     */
    private class CustomIterator implements ListIterator<E> {
        private CustomNode<E> lastAccessed;
        private CustomNode<E> next;
        private int nextIndex;

        public CustomIterator(int idx){
            if(idx > size() || idx < 0){
                throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
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
            // This condition checks if previous() was the last call that moved the cursor.
            if (lastAccessed == next){
                next = next.next; // Must adjust 'next' pointer before unlinking
            } else{
                // If next() was the last call, the index has already been incremented,
                // so we must decrement it to reflect the removal.
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
            if(next == null){ // Iterator at the end of the list
                linkToTail(e);
            } else { // Iterator is somewhere in the middle or at the start
                linkBefore(e, next);
            }
            lastAccessed = null;
            nextIndex++;
        }

    }

    /**
     * A static nested class representing a node in the linked list.
     * Each node contains an element, a reference to the next node, and a reference to the previous node.
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