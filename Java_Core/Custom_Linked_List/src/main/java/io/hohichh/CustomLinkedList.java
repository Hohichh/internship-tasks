package io.hohichh;

import java.util.*;


public class CustomLinkedList<E> extends AbstractSequentialList<E>
{
    private CustomNode<E> head;
    private CustomNode<E> tail;

    private int size;

    public CustomLinkedList() {}

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public E get(int index) {
        return super.get(index);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new CustomIterator(index);
    }
    @Override
    public void addFirst(E e){
        linkToHead(e);
    }
    @Override
    public void addLast(E e){
        linkToTail(e);
    }

    public void add(E e, int index) {
        listIterator(index).add(e);
    }

    @Override
    public E getFirst(){
        return head.getEl();
    }
    @Override
    public E getLast(){
        return tail.getEl();
    }

    @Override
    public E removeFirst(){
        E temp = head.getEl();
        unlinkHead();
        return temp;
    }
    @Override
    public E removeLast(){
        E temp = tail.getEl();
        unlinkTail();
        return temp;
    }
    @Override
    public E remove(int index){
        ListIterator<E> it = listIterator(index);
        E el = it.next();
        it.remove();
        return el;
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
            } else if(next == head){ //if iterator at the start
                linkToHead(e);
            } else{ //linking in the middle
                linkBefore(e, next);
            }
            lastAccessed = null;
            nextIndex++;
        }

    }

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
