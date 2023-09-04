import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Aaron Lucas and Steven Masilonis
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Boundary.
     */
    @Test
    public final void testRemoveRootLeavingEmpty() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest("one");
        Set<String> setExpected = this.createFromArgsRef("one");

        /*
         * Call method under test
         */
        String removed = set.remove("one");
        String removedExpected = setExpected.remove("one");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(setExpected, set);
        assertEquals(removedExpected, removed);
    }

    /**
     * Challenging.
     */
    @Test
    public final void testRemoveRootLeavingNonEmpty() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest("5", "3", "7", "8", "1");
        Set<String> setExpected = this.createFromArgsRef("5", "3", "7", "8",
                "1");

        /*
         * Call method under test
         */
        String removed = set.remove("5");
        String removedExpected = setExpected.remove("5");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(setExpected, set);
        assertEquals(removedExpected, removed);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveLeftTree() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest("5", "3", "7", "8", "1");
        Set<String> setExpected = this.createFromArgsRef("5", "3", "7", "8",
                "1");

        /*
         * Call method under test
         */
        String removed = set.remove("1");
        String removedExpected = setExpected.remove("1");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(setExpected, set);
        assertEquals(removedExpected, removed);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveRightTree() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest("5", "3", "7", "8", "1");
        Set<String> setExpected = this.createFromArgsRef("5", "3", "7", "8",
                "1");

        /*
         * Call method under test
         */
        String removed = set.remove("8");
        String removedExpected = setExpected.remove("8");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(setExpected, set);
        assertEquals(removedExpected, removed);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testSizeEmpty() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest();
        Set<String> setExpected = this.createFromArgsRef();

        /*
         * Call method under test
         */
        int size = set.size();
        int sizeExpected = setExpected.size();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sizeExpected, size);
        assertEquals(setExpected, set);
    }

    /**
     * Routine.
     */
    @Test
    public final void testSizeNonEmpty() {
        /*
         * Set up variables
         */
        Set<String> set = this.createFromArgsTest("one", "1", "two", "2");
        Set<String> setExpected = this.createFromArgsRef("one", "1", "two",
                "2");

        /*
         * Call method under test
         */
        int size = set.size();
        int sizeExpected = setExpected.size();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sizeExpected, size);
        assertEquals(setExpected, set);
    }

    /**
     * Routine.
     */
    @Test
    public final void testAdd1() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        Set<String> expected = this.createFromArgsRef("one", "two", "three",
                "four");
        test.add("four");
        assertEquals(test, expected);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testAdd2() {
        Set<String> test = this.createFromArgsTest();
        Set<String> expected = this.createFromArgsRef("one");
        test.add("one");
        assertEquals(test, expected);
    }

    /**
     * Challenge.
     */
    @Test
    public final void testAdd3() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        Set<String> expected = this.createFromArgsRef("one", "two", "three",
                "");
        test.add("");
        assertEquals(test, expected);
    }

    /**
     * Routine.
     */
    @Test
    public final void testRemoveAny() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        test.removeAny();
        int size = test.size();
        int sizeExpected = 2;
        assertEquals(size, sizeExpected);
    }

    /**
     * Challenge.
     */
    @Test
    public final void testRemoveAny2() {
        Set<String> test = this.createFromArgsTest();
        test.removeAny();
        int size = test.size();
        int sizeExpected = 0;
        assertEquals(size, sizeExpected);
    }

    /**
     * Boundary.
     */
    @Test
    public final void testRemoveAny3() {
        Set<String> test = this.createFromArgsTest("one");
        test.removeAny();
        int size = test.size();
        int sizeExpected = 0;
        assertEquals(size, sizeExpected);
    }

    /**
     * Routine.
     */
    @Test
    public final void testContainsYes() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        boolean check = test.contains("one");
        assertEquals(check, true);
    }

    /**
     * Routine.
     */
    @Test
    public final void testContainsNo() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        boolean check = test.contains("four");
        assertEquals(check, false);
    }

    /**
     * Routine.
     */
    @Test
    public final void testContainsYes2() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        boolean check = test.contains("two");
        assertEquals(check, true);
    }

    /**
     * Routine.
     */
    @Test
    public final void testContainsYes3() {
        Set<String> test = this.createFromArgsTest("one", "two", "three");
        boolean check = test.contains("three");
        assertEquals(check, true);
    }

    /**
     * Challenge.
     */
    @Test
    public final void testContainsNo2() {
        Set<String> test = this.createFromArgsTest("one");
        boolean check = test.contains("three");
        assertEquals(check, false);
    }

    /**
     * Challenge.
     */
    @Test
    public final void testContainsYes4() {
        Set<String> test = this.createFromArgsTest("one");
        boolean check = test.contains("one");
        assertEquals(check, true);
    }

}
