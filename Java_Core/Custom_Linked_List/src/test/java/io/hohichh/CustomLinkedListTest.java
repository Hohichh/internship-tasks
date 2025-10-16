package io.hohichh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    private CustomLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    @DisplayName("size() on an empty list should return zero")
    void testSize_onEmptyList_shouldReturnZero() {
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("size() after adding elements should return correct size")
    void testSize_afterAddingElements_shouldReturnCorrectSize() {
        list.addFirst("A");
        list.addLast("B");
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("addFirst() on an empty list should add the element")
    void testAddFirst_onEmptyList_shouldAddElement() {
        list.addFirst("A");
        assertEquals(1, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("A", list.getLast());
    }

    @Test
    @DisplayName("addFirst() on a non-empty list should make the element the new head")
    void testAddFirst_onNonEmptyList_shouldBecomeNewHead() {
        list.addFirst("A");
        list.addFirst("B");
        assertEquals(2, list.size());
        assertEquals("B", list.getFirst());
        assertEquals("A", list.getLast());
    }

    @Test
    @DisplayName("addLast() on an empty list should add the element")
    void testAddLast_onEmptyList_shouldAddElement() {
        list.addLast("A");
        assertEquals(1, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("A", list.getLast());
    }

    @Test
    @DisplayName("addLast() on a non-empty list should make the element the new tail")
    void testAddLast_onNonEmptyList_shouldBecomeNewTail() {
        list.addLast("A");
        list.addLast("B");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("B", list.getLast());
    }

    @Test
    @DisplayName("add(index, el) at index 0 should behave like addFirst()")
    void testAdd_atIndexZero_shouldBehaveLikeAddFirst() {
        list.add(0, "A");
        list.add(0, "B");
        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("A", list.get(1));
    }

    @Test
    @DisplayName("add(index, el) in the middle should insert element correctly")
    void testAdd_inTheMiddle_shouldInsertElementCorrectly() {
        list.addLast("A");
        list.addLast("C");
        list.add(1, "B");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    @DisplayName("add(index, el) at the end of the list should behave like addLast()")
    void testAdd_atEnd_shouldBehaveLikeAddLast() {
        list.addLast("A");
        list.add(1, "B");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("B", list.getLast());
    }


    @Test
    @DisplayName("add(index, el) with an invalid index should throw IndexOutOfBoundsException")
    void testAdd_withInvalidIndex_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "A"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "A"));
    }

    @Test
    @DisplayName("getFirst() on a non-empty list should return the first element")
    void testGetFirst_onNonEmptyList_shouldReturnFirstElement() {
        list.addFirst("A");
        list.addLast("B");
        assertEquals("A", list.getFirst());
    }

    @Test
    @DisplayName("getFirst() on an empty list should throw an exception")
    void testGetFirst_onEmptyList_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> list.getFirst());
    }

    @Test
    @DisplayName("getLast() on a non-empty list should return the last element")
    void testGetLast_onNonEmptyList_shouldReturnLastElement() {
        list.addFirst("A");
        list.addLast("B");
        assertEquals("B", list.getLast());
    }

    @Test
    @DisplayName("getLast() on an empty list should throw an exception")
    void testGetLast_onEmptyList_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> list.getLast());
    }

    @Test
    @DisplayName("get(index) with a valid index should return the correct element")
    void testGet_withValidIndex_shouldReturnCorrectElement() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    @DisplayName("get(index) with an invalid index should throw IndexOutOfBoundsException")
    void testGet_withInvalidIndex_shouldThrowException() {
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    @DisplayName("removeFirst() on a single-element list should make the list empty")
    void testRemoveFirst_onSingleElementList_shouldMakeListEmpty() {
        list.addFirst("A");
        String removed = list.removeFirst();
        assertEquals("A", removed);
        assertEquals(0, list.size());
        assertThrows(NoSuchElementException.class, () -> list.getFirst());
    }

    @Test
    @DisplayName("removeFirst() on a non-empty list should remove the head")
    void testRemoveFirst_onNonEmptyList_shouldRemoveHead() {
        list.addLast("A");
        list.addLast("B");
        String removed = list.removeFirst();
        assertEquals("A", removed);
        assertEquals(1, list.size());
        assertEquals("B", list.getFirst());
    }

    @Test
    @DisplayName("removeFirst() on an empty list should throw an exception")
    void testRemoveFirst_onEmptyList_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    @Test
    @DisplayName("removeLast() on a single-element list should make the list empty")
    void testRemoveLast_onSingleElementList_shouldMakeListEmpty() {
        list.addLast("A");
        String removed = list.removeLast();
        assertEquals("A", removed);
        assertEquals(0, list.size());
        assertThrows(NoSuchElementException.class, () -> list.getLast());
    }

    @Test
    @DisplayName("removeLast() on a non-empty list should remove the tail")
    void testRemoveLast_onNonEmptyList_shouldRemoveTail() {
        list.addLast("A");
        list.addLast("B");
        String removed = list.removeLast();
        assertEquals("B", removed);
        assertEquals(1, list.size());
        assertEquals("A", list.getLast());
    }

    @Test
    @DisplayName("removeLast() on an empty list should throw an exception")
    void testRemoveLast_onEmptyList_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    @DisplayName("remove(0) should remove the head element")
    void testRemove_atIndexZero_shouldRemoveHead() {
        list.addLast("A");
        list.addLast("B");
        String removed = list.remove(0);
        assertEquals("A", removed);
        assertEquals(1, list.size());
        assertEquals("B", list.get(0));
    }

    @Test
    @DisplayName("remove(lastIndex) should remove the tail element")
    void testRemove_atLastIndex_shouldRemoveTail() {
        list.addLast("A");
        list.addLast("B");
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(1, list.size());
        assertEquals("A", list.get(0));
    }

    @Test
    @DisplayName("remove(index) from the middle should remove the correct element")
    void testRemove_fromTheMiddle_shouldRemoveCorrectly() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    @DisplayName("remove(index) with an invalid index should throw IndexOutOfBoundsException")
    void testRemove_withInvalidIndex_shouldThrowException() {
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    @DisplayName("getNodeByIndex() should find node from head when index is in the first half")
    void testGetNodeByIndex_whenIndexInFirstHalf_shouldIterateFromHead() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");
        list.addLast("E");
        // This test indirectly checks the getNodeByIndex optimization
        assertEquals("B", list.get(1));
    }

    @Test
    @DisplayName("getNodeByIndex() should find node from tail when index is in the second half")
    void testGetNodeByIndex_whenIndexInSecondHalf_shouldIterateFromTail() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");
        list.addLast("E");
        // This test indirectly checks the getNodeByIndex optimization
        assertEquals("D", list.get(3));
    }
}