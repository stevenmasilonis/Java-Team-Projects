import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /**
     * Routine.
     */
    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testAddToEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        m.add("green");
        mExpected.add("green");
        assertEquals(mExpected, m);
    }

    /**
     * Routine.
     */
    @Test
    public final void testAddToNonEmptyOne() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("blue");
        mExpected.add("blue");
        assertEquals(mExpected, m);
    }

    /**
     * Routine.
     */
    @Test
    public final void testAddToNonEmptyMany() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue", "red", "orange", "two", "three", "four");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue", "red", "orange", "two", "three", "four");
        m.add("one");
        mExpected.add("one");
        assertEquals(mExpected, m);
    }

    /**
     * Routine.
     */
    @Test
    public final void testChangeToExtractionModeBoolean() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        m.changeToExtractionMode();
        mExpected.changeToExtractionMode();
        boolean test = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();
        assertEquals(test, expected);
    }

    /**
     * Routine.
     */
    @Test
    public final void testChangeToExtractionModeSorted() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue", "yellow", "red");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue", "yellow", "red");
        m.changeToExtractionMode();
        mExpected.changeToExtractionMode();

        assertEquals(mExpected, m);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testRemoveFirstToEmpty() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                false, "green");
        String expected = sortingExpected.removeFirst();
        String test = sorting.removeFirst();
        assertEquals(sortingExpected, sorting);
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveFirstToOne() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, false,
                "green", "blue");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                false, "green", "blue");
        String expected = sortingExpected.removeFirst();
        String test = sorting.removeFirst();
        assertEquals(sortingExpected, sorting);
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveFirst5Entries() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, false,
                "green", "blue", "red", "orange", "hello");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                false, "green", "blue", "red", "orange", "hello");
        String expected = sortingExpected.removeFirst();
        String test = sorting.removeFirst();
        assertEquals(sortingExpected, sorting);
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveFirst10Entries() {
        SortingMachine<String> sort = this.createFromArgsTest(ORDER, false,
                "one", "two", "three", "four", "five", "six", "seven", "eight",
                "nine", "ten");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                false, "one", "two", "three", "four", "five", "six", "seven",
                "eight", "nine", "ten");
        String expected = sortingExpected.removeFirst();
        String test = sort.removeFirst();
        assertEquals(sortingExpected, sort);
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testIsInInsertionModeTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue");
        boolean test = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testIsInInsertionModeFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "blue", "red", "orange", "one", "two", "three",
                "four");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "blue", "red", "orange", "one", "two", "three",
                "four");
        boolean test = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();
        assertEquals(test, expected);
    }

    /**
     * Routine.
     */
    @Test
    public final void testOrder() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue");
        Comparator<String> test = m.order();
        Comparator<String> expected = mExpected.order();
        assertEquals(test, expected);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testSizeEmpty() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                true);
        int expected = sortingExpected.size();
        int test = sorting.size();
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testSizeNonEmpty() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, true,
                "green", "blue");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                true, "green", "blue");
        int expected = sortingExpected.size();
        int test = sorting.size();
        assertEquals(expected, test);
    }

    /**
     * Routine.
     */
    @Test
    public final void testSizeNonEmptyMany() {
        SortingMachine<String> sorting = this.createFromArgsTest(ORDER, true,
                "green", "blue", "red", "orange", "one", "two", "three",
                "four");
        SortingMachine<String> sortingExpected = this.createFromArgsRef(ORDER,
                true, "green", "blue", "red", "orange", "one", "two", "three",
                "four");
        int expected = sortingExpected.size();
        int test = sorting.size();
        assertEquals(expected, test);
    }

}
