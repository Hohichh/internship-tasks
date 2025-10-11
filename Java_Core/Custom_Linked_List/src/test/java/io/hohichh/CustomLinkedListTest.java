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
    @DisplayName("add(el, 0) should behave like addFirst()")
    void testAdd_atIndexZero_shouldBehaveLikeAddFirst() {
        list.add("A", 0);
        list.add("B", 0);
        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("A", list.get(1));
    }

    @Test
    @DisplayName("add(el, size) should behave like addLast()")
    void testAdd_atIndexSize_shouldBehaveLikeAddLast() {
        list.add("A");
        list.add("B", 1);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    @DisplayName("add(el, index) in the middle should insert element correctly")
    void testAdd_inTheMiddle_shouldInsertElementCorrectly() {
        list.addLast("A");
        list.addLast("C");
        list.add("B", 1);
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    @DisplayName("add(el, index) with an invalid index should throw IndexOutOfBoundsException")
    void testAdd_withInvalidIndex_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add("A", -1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add("A", 1));
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
        assertThrows(NullPointerException.class, () -> list.getFirst());
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
        assertThrows(NullPointerException.class, () -> list.getLast());
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
        assertThrows(NullPointerException.class, () -> list.removeFirst());
    }

    @Test
    @DisplayName("removeLast() on a single-element list should make the list empty")
    void testRemoveLast_onSingleElementList_shouldMakeListEmpty() {
        list.addLast("A");
        String removed = list.removeLast();
        assertEquals("A", removed);
        assertEquals(0, list.size());
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
        assertThrows(NullPointerException.class, () -> list.removeLast());
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
    @DisplayName("set(index, element) at the beginning of the list should update the element")
    void testSet_atBeginning_shouldUpdateElement() {
        list.addLast("A");
        list.addLast("B");
        String oldElement = list.set(0, "Z");
        assertEquals("A", oldElement);
        assertEquals("Z", list.get(0));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("set(index, element) in the middle of the list should update the element")
    void testSet_inTheMiddle_shouldUpdateElement() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        String oldElement = list.set(1, "Y");
        assertEquals("B", oldElement);
        assertEquals("Y", list.get(1));
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("set(index, element) at the end of the list should update the element")
    void testSet_atEnd_shouldUpdateElement() {
        list.addLast("A");
        list.addLast("B");
        String oldElement = list.set(1, "X");
        assertEquals("B", oldElement);
        assertEquals("X", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("set(index, element) with an invalid index should throw IndexOutOfBoundsException")
    void testSet_withInvalidIndex_shouldThrowException() {
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "Z"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "Z"));
    }



    @Test
    @DisplayName("getNodeByIndex() should find node from head when index is in the first half")
    void testGetNodeByIndex_whenIndexInFirstHalf_shouldIterateFromHead() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");
        list.addLast("E");
        assertEquals("B", list.get(1));
    }


    @Test
    @DisplayName("ListIterator.hasPrevious() should return false at the beginning")
    void testListIterator_hasPrevious_atStartShouldBeFalse() {
        list.add("A");
        var iterator = list.listIterator(0);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    @DisplayName("ListIterator.hasPrevious() should return true in the middle or end")
    void testListIterator_hasPrevious_inMiddleShouldBeTrue() {
        list.add("A");
        list.add("B");
        var iterator = list.listIterator(1);
        assertTrue(iterator.hasPrevious());
    }

    @Test
    @DisplayName("ListIterator.previous() should traverse the list backwards")
    void testListIterator_previous_shouldReturnElementsInReverseOrder() {
        list.add("A");
        list.add("B");
        var iterator = list.listIterator(2);
        assertTrue(iterator.hasPrevious());
        assertEquals("B", iterator.previous());
        assertTrue(iterator.hasPrevious());
        assertEquals("A", iterator.previous());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    @DisplayName("ListIterator.previous() at the beginning should throw NoSuchElementException")
    void testListIterator_previous_atStartShouldThrowException() {
        list.add("A");
        var iterator = list.listIterator(0);
        assertThrows(NoSuchElementException.class, iterator::previous);
    }

    @Test
    @DisplayName("ListIterator indices should be correct during forward and backward traversal")
    void testListIterator_indices_shouldBeCorrect() {
        list.add("A");
        list.add("B");
        var iterator = list.listIterator(0);

        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());

        iterator.next();

        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());

        iterator.next();

        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());

        iterator.previous();

        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());
    }


    @Test
    @DisplayName("ListIterator.remove() after a call to previous() should remove the correct element")
    void testListIterator_removeAfterPrevious_shouldRemoveLastElementReturnedByPrevious() {
        list.add("A");
        list.add("B");
        list.add("C");
        var iterator = list.listIterator();

        iterator.next();
        iterator.next();

        iterator.previous();

        iterator.remove();

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));

        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
    }

}