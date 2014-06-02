package test;

import com.javalinq.implementations.QList;
import com.javalinq.implementations.QSet;
import com.javalinq.interfaces.QIterable;
import org.junit.Test;

public class Tests {
    @Test
    public void where() {
        QList<String> list = new QList<String>("One", "Two", "Three", "Four", "Five");

        // Capture only the items that have a length of three.
        QSet<String> lengthThreeStrings = list.where(item -> item.length() == 3).toSet();

        assert (lengthThreeStrings.size() == 2);
        assert (lengthThreeStrings.contains("One"));
        assert (lengthThreeStrings.contains("Two"));
    }

    @Test
    public void map() {
        QList<String> list = new QList<String>("One", "Two", "Three", "Four", "Five");

        // Map each item onto it's length.
        QIterable<Integer> stringLengths = list.map(item -> item.length());

        assert (stringLengths.get(0) == 3);
        assert (stringLengths.get(1) == 3);
        assert (stringLengths.get(2) == 5);
        assert (stringLengths.get(3) == 4);
        assert (stringLengths.get(4) == 4);
    }

    @Test
    public void distinct() {
        QList<String> list = new QList<String>("One", "Two", "Two", "Three", "One", "Two");

        // Capture only the distinct items in the set.
        QSet<String> distinctStrings = list.distinct().toSet();

        // Should only be three strings.
        assert (distinctStrings.size() == 3);
        assert (distinctStrings.contains("One"));
        assert (distinctStrings.contains("Two"));
        assert (distinctStrings.contains("Three"));
    }

    @Test
    public void distinctWithLambda() {
        QList<String> list = new QList<String>("Five", "Four", "Three", "Two", "One");

        // Capture only items with distinct lengths.
        QSet<String> distinctStrings = list.distinct(item -> item.length()).toSet();


        assert (distinctStrings.size() == 3);
        assert (distinctStrings.contains("Five"));  // First item of length 4
        assert (distinctStrings.contains("Three")); // First item of length 5
        assert (distinctStrings.contains("Two"));   // First item of length 3
    }

    @Test
    public void ordered() {
        QList<Integer> list = new QList<Integer>(1, 0, 2, 9, 3, 8, 4, 7, 5, 6);

        QIterable<Integer> sortedNumbers = list.sort(number -> number);

        assert(sortedNumbers.get(0) == 0);
        assert(sortedNumbers.get(1) == 1);
        assert(sortedNumbers.get(2) == 2);
        assert(sortedNumbers.get(3) == 3);
        assert(sortedNumbers.get(4) == 4);
        assert(sortedNumbers.get(5) == 5);
        assert(sortedNumbers.get(6) == 6);
        assert(sortedNumbers.get(7) == 7);
        assert(sortedNumbers.get(8) == 8);
        assert(sortedNumbers.get(9) == 9);
    }

}