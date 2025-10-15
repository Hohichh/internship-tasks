package io.hohichh;
import java.util.NoSuchElementException;

public class CustomLinkedList<E> {
    private int size;

    private CustomNode<E> head;
    private CustomNode<E> tail;

    public int size(){
        return size;
    }

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
    public void add(int index, E e){
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException();
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

    public E getFirst(){
        if(head == null){
            throw new NoSuchElementException();
        }
        E el = head.getEl();
        return el;
    }
    public E getLast(){
        if(tail == null){
            throw new NoSuchElementException();
        }
        E el = tail.getEl();
        return el;
    }
    public E get(int index){
        return getNodeByIndex(index).getEl();
    }
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

    public E remove(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
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

    private CustomNode<E> getNodeByIndex(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        CustomNode<E> search;
        if(index < (size >> 1)){
            search = head;
            for(int i = 0; i < index; i++){
                search = search.getNext();
            }
        }
        else{
            search = tail;
            for(int i = size-1; i > index; i--){
                search = search.getPrev();
            }
        }
        return search;
    }

    static class CustomNode<E>{
        private E el;
        private CustomNode<E> next;
        private CustomNode<E> prev;

        public CustomNode(E el){
            this.el = el;
        }

        public E getEl() {
            return el;
        }

        public CustomNode<E> getNext(){
            return next;
        }

        public CustomNode<E> getPrev(){
            return prev;
        }

        public void setEl(E el) {
            this.el = el;
        }

        public void setNext(CustomNode<E> next) {
            this.next = next;
        }

        public void setPrev(CustomNode<E> prev) {
            this.prev = prev;
        }
    }
}