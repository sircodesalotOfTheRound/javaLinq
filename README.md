javaLinq
========

Clean, freely extensible Java-8 Linq style collection implementation.

## Available Operations

The following operations are available right out of the box.


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

### Reduce

Use reduce to fold all items in a collection into a final form:

```Java
public void testReduce() {
    QIterable<Integer> numbers = new QList<Integer>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // Use reduce to fold the entire collection into a value
    // We insert the initial sum value (0), and then fold each item into the total.
    Integer total = numbers.reduce(0, (sum, next) -> sum + next);

    assert (total == 55);
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

Use partition to split a collection into segments:

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

### Count

Count can be used to count the number of items in a set.

```Java
public void count() {
    QList<Integer> list = new QList<Integer>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    // Count the number of items in the set
    assert (list.count() == 10);
    
    // Count the number of items with a predicate (here: odds)
    assert (list.count(item -> item % 2 == 1) == 5);
}
```    

### First, Last, Single

Find the first, last and single items:

```Java
public void sequence() {
    QList<Integer> list = new QList<Integer>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    // Find the first and last items
    assert (list.first() == 0);
    assert (list.last() == 9);

    // Find items with predicate
    assert (list.first(number -> number % 2 == 0) == 0); // First even
    assert (list.last(number -> number % 2 == 0) == 8); // Last even
    assert (list.single(number -> {
        boolean isGreaterThanZero = number > 0;
        boolean isDivisibleByTwo = number % 2 == 0;
        boolean isDivisibleByThree = number % 3 == 0;

        // Only 6 should be divisible by two and three.
        return isGreaterThanZero && isDivisibleByTwo && isDivisibleByThree;
    }) == 6);
}
```

Note that single enforces that only a single matching item exists (Exception if more than one item exists).

### OfType

Find items that descend from a particular class or interface:

```Java
public void ofType() {
    QList<Object> list = new QList<Object>("One", 2, "Three", 4, "Five", 6);

    // Get items of a particular type.
    QIterable<String> strings = list.ofType(String.class);
   
    // Should only have three items
    assert (strings.count() == 3);
    
    for (int index = 0; index < strings.count();  index++) {
        assert (strings.get(index) instanceof String);
    }
}
```

### Cast

Cast items in a set to a an interface or class:

```Java

class Base { }
class Derived extends Base { }

public void cast() {
    // Create a list of base classes
    QList<Base> bases = new QList<Base>(new Derived(){}, new Derived(){});   

    // Cast the base classes to derived
    QIterable<Derived> deriveds = bases.cast(Derived.class);
    
    // Verify
    for (Derived item : deriveds) {
        assert (item instanceof Derived);
    }
}
```

### Concat, Union, Intersect, Exclude

Perform set operations on two sets of items:

```Java
public void setOperations() {
    QIterable<Integer> lhs = new QList<Integer>(2, 3, 4);
    QIterable<Integer> rhs = new QList<Integer>(4, 5, 6);

    // Concat combines two sets including duplicates:
    QIterable<Integer> concat = lhs.concat(rhs).sort(self -> self);

    // UnionDistinct combines the distinct members of two sets
    QIterable<Integer> union = lhs.unionDistinct(rhs).sort(self -> self);

    // Intersect finds the intersection of two sets
    QIterable<Integer> intersect = lhs.intersect(rhs).sort(self -> self);

    // Except finds the difference between two sets
    QIterable<Integer> exclusion = lhs.except(rhs).sort(self -> self);

    assert (concat.count() == 6);
    assert (concat.get(0) == 2);
    assert (concat.get(1) == 3);
    assert (concat.get(2) == 4);
    assert (concat.get(3) == 4);
    assert (concat.get(4) == 5);
    assert (concat.get(5) == 6);

    assert (union.count() == 5);
    assert (union.get(0) == 2);
    assert (union.get(1) == 3);
    assert (union.get(2) == 4);
    assert (union.get(3) == 5);
    assert (union.get(4) == 6);

    assert (intersect.count() == 1);
    assert (intersect.get(0) == 4);

    assert (exclusion.count() == 2);
    assert (exclusion.get(0) == 2);
    assert (exclusion.get(1) == 3);
}
    
```

### Reverse

Reverse a set of items:

```Java
public void reverse() {
    QIterable<Integer> lhs = new QList<Integer>(1, 2, 3, 4, 5);

    // Reverse the set of items
    QIterable<Integer> reversed = lhs.reverse();

    for (Integer value : reversed) {
        System.out.println(value);
    }

    assert (reversed.get(0) == 5);
    assert (reversed.get(1) == 4);
    assert (reversed.get(2) == 3);
    assert (reversed.get(3) == 2);
    assert (reversed.get(4) == 1);
}
```

### Any, All

Determine whether there are any elements that fit a predicate description:

```Java
public void anyAll() {
    QIterable<Integer> lhs = new QList<Integer>(1, 2, 3, 4, 5);

    assert (lhs.any()); // At least one item
    assert (lhs.any(number -> number > 2)); // At least one item greater than two
    assert (!lhs.all(number -> number > 2)); // Not all items greater than two
}
```

### ToList, ToSet, Partition

Create a `QList<T>` (good for fast indexing) or `QSet<T>` (good for fast searching) from a raw `QIterable<T>`:

```Java
public void changeContainer() {
    QIterable<Object> numbers = new QList<Object>(0, "One", 2, "Three", 4, "Five", 6);

    // Create a duplicate list, which is good for fast indexing.
    QList<Object> list = numbers.toList();

    assert (list.get(2).equals(2));

    // Create a duplicate set, which is good for fast searching.
    QSet<Object> set = numbers.toSet();

    assert (set.contains("Three"));
}
```

You can also use `.partition()` to easily create key-value pair mappings of a set.

```Java
public void changeContainer() {
    QIterable<Object> numbers = new QList<Object>(0, "One", 2, "Three", 4, "Five", 6);
    
    // Use partition to create a key-value pair of items.
    // Here we partition the set by class.
    Partition<Class, Object> parition = numbers.parition(number -> number.getClass());

    assert(parition.get(Integer.class).count() == 4);
    assert(parition.get(String.class).count() == 3);
}
```


## Create Your Own Collections with Ease!

To create a collection, simply implement the `QIterable<T>` interface, and it's member method `iterator':

```Java
class MyCollection<T> implements QIterable<T> {

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
```

for example, we'll create a simple iterator that counts to 5 using an anonymous class.

```Java
public void createQIterable() {
    // To create a QIterable, simply implement the interface.
    QIterable<Integer> iterable = new QIterable<Integer>() {
        
        // And it's single method 'iterator'.
        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                private int current = 0;
                
                @Override
                public boolean hasNext() {
                    // Continue iterating so long as 'current' is less than 5.
                    return current < 5;
                }

                @Override
                public Integer next() {
                    // Increment.
                    return current++;
                }
            };
        }
    };
   
    assert (iterable instanceof QIterable);
    assert (iterable.count() == 5);
    assert (iterable.get(0) == 0);
    assert (iterable.get(1) == 1);
    assert (iterable.get(2) == 2);
    assert (iterable.get(3) == 3);
    assert (iterable.get(4) == 4);
}
```