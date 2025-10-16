/*
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: Create a custom realization of LinkedList and implement a set of standard operations.
 * The implementation must be covered with unit tests using JUnit 5.
 */

package io.hohichh;
import java.util.NoSuchElementException;

/**
 * A custom implementation of a doubly-linked list.
 * This class provides basic linked list operations such as adding, getting, and removing elements.
 *
 * @param <E> the type of elements held in this collection
 */
public class CustomLinkedList<E> {
    private int size;

    private CustomNode<E> head;
    private CustomNode<E> tail;

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size(){
        return size;
    }

    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    public void addFirst(E e){
        CustomNode<E> newNode = new CustomNode<>(e);
        if(head == null){
            head = tail = newNode;
        } else{
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e the element to add
     */
    public void addLast(E e){
        CustomNode<E> newNode = new CustomNode<>(e);
        if(tail == null){
            head = tail = newNode;
        } else{
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index index at which the specified element is to be inserted
     * @param e element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size)
     */
    public void add(int index, E e){
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if(index == size){
            addLast(e);
        } else if(index == 0){
            addFirst(e);
        } else{
            CustomNode<E> newNode = new CustomNode<>(e);
            CustomNode<E> currNode = getNodeByIndex(index);
            CustomNode<E> prevCurr = currNode.getPrev();
            currNode.setPrev(newNode);
            newNode.setNext(currNode);

            prevCurr.setNext(newNode);
            newNode.setPrev(prevCurr);
            size++;
        }

    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getFirst(){
        if(head == null){
            throw new NoSuchElementException();
        }
        return head.getEl();
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getLast(){
        if(tail == null){
            throw new NoSuchElementException();
        }
        return tail.getEl();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    public E get(int index){
        return getNodeByIndex(index).getEl();
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeFirst(){
        if(head == null){
            throw new NoSuchElementException();
        }
        E headEl = head.getEl();
        CustomNode<E> newHead = head.getNext();

        head.setNext(null);
        head = newHead;
        if(head == null){
            tail = null;
        } else{
            newHead.setPrev(null);
        }
        size--;
        return headEl;
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeLast(){
        if(tail == null){
            throw new NoSuchElementException();
        }
        E tailEl = tail.getEl();
        CustomNode<E> newTail = tail.getPrev();

        tail.setPrev(null);
        tail = newTail;
        if(tail == null){
            head = null;
        } else{
            newTail.setNext(null);
        }
        size--;
        return tailEl;
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    public E remove(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if(index == size - 1){
            return removeLast();
        } else if(index == 0){
            return removeFirst();
        } else{
            CustomNode<E> curr = getNodeByIndex(index);
            E el = curr.getEl();
            CustomNode<E> prevCurr = curr.getPrev();
            CustomNode<E> nextCurr = curr.getNext();

            prevCurr.setNext(nextCurr);
            nextCurr.setPrev(prevCurr);
            curr = null;

            size--;
            return el;
        }
    }

    /**
     * Returns the node at the specified element index.
     * This method contains an optimization: it traverses from the beginning of the list
     * if the index is in the first half, and from the end of the list if it's in the second half.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
    private CustomNode<E> getNodeByIndex(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        CustomNode<E> search;
        if(index < (size >> 1)){ // (size >> 1) is a faster way of doing size / 2
            search = head;
            for(int i = 0; i < index; i++){
                search = search.getNext();
            }
        }
        else{
            search = tail;
            for(int i = size - 1; i > index; i--){
                search = search.getPrev();
            }
        }
        return search;
    }

    /**
     * Represents a node in the doubly-linked list.
     * Each node contains an element and references to the next and previous nodes.
     * @param <E> the type of element stored in the node
     */
    static class CustomNode<E>{
        private E el;
        private CustomNode<E> next;
        private CustomNode<E> prev;

        /**
         * Constructs a new node with the specified element.
         *
         * @param el the element to store in this node
         */
        public CustomNode(E el){
            this.el = el;
        }

        /**
         * Returns the element stored in this node.
         * @return the element
         */
        public E getEl() {
            return el;
        }

        /**
         * Returns the next node in the list.
         * @return the next node
         */
        public CustomNode<E> getNext(){
            return next;
        }

        /**
         * Returns the previous node in the list.
         * @return the previous node
         */
        public CustomNode<E> getPrev(){
            return prev;
        }

        /**
         * Sets the element for this node.
         * @param el the new element
         */
        public void setEl(E el) {
            this.el = el;
        }

        /**
         * Sets the reference to the next node.
         * @param next the next node
         */
        public void setNext(CustomNode<E> next) {
            this.next = next;
        }

        /**
         * Sets the reference to the previous node.
         * @param prev the previous node
         */
        public void setPrev(CustomNode<E> prev) {
            this.prev = prev;
        }
    }
}