javaLinq
========

Clean, freely extensible Java-8 Linq style collection implementation.

### Where

Where filters the set of items based on a predicate:

```Java
public void where() {
    QList<String> list = new QList<String>("One", "Two", "Three", "Four", "Five");

    // Capture only the items that have a length of three.
    QSet<String> lengthThreeStrings = list.where(item -> item.length() == 3).toSet();

    assert(lengthThreeStrings.size() == 2);
    assert(lengthThreeStrings.contains("One"));
    assert(lengthThreeStrings.contains("Two"));
}
```
    
### Map

Map performs a transformation on each item:

```Java
public void map() {
    QList<String> list = new QList<String>("One", "Two", "Three", "Four", "Five");

    // Map each item onto it's length.
    QIterable<Integer> stringLengths = list.map(item -> item.length());

    assert(stringLengths.get(0) == 3);
    assert(stringLengths.get(1) == 3);
    assert(stringLengths.get(2) == 5);
    assert(stringLengths.get(3) == 4);
    assert(stringLengths.get(4) == 4);
}
```
    
### Distinct

Distinct captures only those items in a set that are distinct:

```Java
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
```

Note that distinct can also be used with a lambda:

```Java
public void distinctWithLambda() {
    QList<String> list = new QList<String>("Five", "Four", "Three", "Two", "One");

    // Capture only items with distinct lengths.
    QSet<String> distinctStrings = list.distinct(item -> item.length()).toSet();


    assert (distinctStrings.size() == 3);
    assert (distinctStrings.contains("Five"));  // First item of length 4
    assert (distinctStrings.contains("Three")); // First item of length 5
    assert (distinctStrings.contains("Two"));   // First item of length 3
}
```

### Sort

Sort puts all the items in a set in order (depending on the property pointed to by the lambda):

```Java
public void ordered() {
    QList<Integer> list = new QList<Integer>(1, 0, 2, 9, 3, 8, 4, 7, 5, 6);

    // Sort numbers based on the property '<self>'.
    // We could use a different property, but in this instance
    // we really just want to sort based on the number itself.
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
```

### Partition

Use partition to split a group of items based on a property:

```Java
public void parition() {
    QList<Integer> list = new QList<Integer>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    // Partition the numbers into two groups, those less than 5 and those greater than 5.
    Partition<Boolean, Integer> parition = list.parition(number -> number < 5);

    // The items in the lower partition (for which number < 5 is 'true').
    assert (parition.get(true).count() == 5);

    assert (parition.get(true).get(0) == 0);
    assert (parition.get(true).get(1) == 1);
    assert (parition.get(true).get(2) == 2);
    assert (parition.get(true).get(3) == 3);
    assert (parition.get(true).get(4) == 4);

    // The items in the upper partition (for which number < 5 is 'false').
    assert (parition.get(false).count() == 5);

    assert (parition.get(false).get(0) == 5);
    assert (parition.get(false).get(1) == 6);
    assert (parition.get(false).get(2) == 7);
    assert (parition.get(false).get(3) == 8);
    assert (parition.get(false).get(4) == 9);

}
```

### Flatten

Flatten can be used to flatten nested sets of items. Here we flatten a partition back to its original form:

```Java
public void flatten() {
    QList<Integer> list = new QList<Integer>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    // Partition the numbers into two groups: odds and evens.
    Partition<Boolean, Integer> parition = list.parition(number -> number % 2 == 0);

    // Undo the partition (flatten the set)
    // Then sort to put them in order.
    QIterable<Integer> flattened = parition.flatten().sort(number -> number);

    // Make sure every number is accounted for.
    for (int index = 0; index < 10; index++) {
        assert (flattened.get(index) == index);
    }
}
```
    
Flatten can also be used with a lambda that points to the property to flatten on.