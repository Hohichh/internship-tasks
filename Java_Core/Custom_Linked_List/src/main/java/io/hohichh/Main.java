package io.hohichh;

import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        CustomLinkedList<String> list = new CustomLinkedList();
        list.add("A", 0);
        list.add("B", 1);
        list.addFirst("First ");
        list.addLast(" Last");
        list.add(" mid ", 2);
        printList(list);

        list.removeFirst();
        printList(list);

        list.removeLast();
        printList(list);

        list.remove(1);
        printList(list);

    }

    public static void printList(CustomLinkedList<String> list){
        for(String el: list){
            System.out.println(el);
        }
        System.out.println("______________________");
    }
}