package com.example.streams;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Enhanced coding kata on the Stream API with exception handling, generics, and advanced concepts.
 * All methods include proper validation and can be completed with a single return statement plus validation.
 */
public class GentlyDownTheStream {

    protected List<String> fruits;
    protected List<String> veggies;
    protected List<Integer> integerValues;

    public GentlyDownTheStream() {
        fruits = Arrays.asList("Apple", "Orange", "Banana", "Pear", "Peach", "Tomato");
        veggies = Arrays.asList("Corn", "Potato", "Carrot", "Pea", "Tomato");
        integerValues = new Random().ints(0, 1001)
                .boxed()
                .limit(1000)
                .collect(Collectors.toList());
    }

    /** ✅ Return a sorted list of fruits */
    public List<String> sortedFruits() throws InvalidDataException {
        try {
            validateCollection(fruits, "Fruits collection");

            return fruits.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (EmptyCollectionException | IllegalArgumentException e) {
            throw e; // expected by tests
        } catch (Exception e) {
            throw new InvalidDataException("Failed to sort fruits: " + e.getMessage(), e);
        }
    }

    /** ✅ Return a sorted list of fruits excluding those starting with 'A' */
    public List<String> sortedFruitsException() throws InvalidDataException {
        try {
            validateCollection(fruits, "Fruits collection");
            return fruits.stream()
                    .filter(Objects::nonNull)
                    .filter(f -> !f.startsWith("A"))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to sort fruits excluding 'A': " + e.getMessage());
        }
    }

    /** ✅ Return the first 2 elements of a sorted fruit list */
    public List<String> sortedFruitsFirstTwo() throws InvalidDataException {
        try {
            validateCollection(fruits, "Fruits collection");

            return fruits.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .limit(2)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to get first two sorted fruits: " + e.getMessage());
        }
    }

    /** ✅ Return a comma-separated String of sorted fruits */
    public String commaSeparatedListOfFruits() throws InvalidDataException {
        try {
            validateCollection(fruits, "Fruits collection");

            String result = fruits.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.joining(", "));

            if (result.isEmpty()) {
                throw new EmptyCollectionException("No fruits to join");
            }
            return result;
        } catch (Exception e) {
            throw new InvalidDataException("Failed to create comma-separated list: " + e.getMessage());
        }
    }

    /** ✅ Return a list of veggies sorted in reverse order */
    public List<String> reverseSortedVeggies() throws InvalidDataException {
        try {
            validateCollection(veggies, "Veggies collection");

            return veggies.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to reverse sort veggies: " + e.getMessage());
        }
    }

    /** ✅ Return a list of veggies sorted in reverse order and upper case */
    public List<String> reverseSortedVeggiesInUpperCase() throws InvalidDataException {
        try {
            validateCollection(veggies, "Veggies collection");

            return veggies.stream()
                    .filter(Objects::nonNull)
                    .map(String::toUpperCase)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to reverse sort veggies in upper case: " + e.getMessage());
        }
    }

    /** ✅ Return the top 10 values from random integers */
    public List<Integer> topTen() throws InvalidDataException {
        try {
            validateCollection(integerValues, "Integer values");

            return integerValues.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.reverseOrder())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to get top 10 values: " + e.getMessage());
        }
    }

    /** ✅ Return the top 10 unique values from random integers */
    public List<Integer> topTenUnique() throws InvalidDataException {
        try {
            validateCollection(integerValues, "Integer values");

            return integerValues.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to get top 10 unique values: " + e.getMessage());
        }
    }

    /** ✅ Return the top 10 unique odd values */
    public List<Integer> topTenUniqueOdd() throws InvalidDataException {
        try {
            validateCollection(integerValues, "Integer values");

            return integerValues.stream()
                    .filter(Objects::nonNull)
                    .filter(n -> n % 2 != 0)
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to get top 10 unique odd values: " + e.getMessage());
        }
    }

    /** ✅ Return average of all random integers */
    public Double average() throws InvalidDataException {
        try {
            validateCollection(integerValues, "Integer values");

            OptionalDouble avg = safeAverage(integerValues);
            if (avg.isEmpty()) {
                throw new EmptyCollectionException("Cannot compute average of empty list");
            }
            return avg.getAsDouble();
        } catch (Exception e) {
            throw new InvalidDataException("Failed to calculate average: " + e.getMessage());
        }
    }

    // ---------- Utility / Helper Methods ----------

    private <T> void validateCollection(Collection<T> collection, String collectionName) throws EmptyCollectionException {
        if (collection == null) {
            throw new IllegalArgumentException(collectionName + " cannot be null");
        }
        if (collection.isEmpty()) {
            throw new EmptyCollectionException(collectionName + " cannot be empty");
        }
    }

    private <T> List<T> sortedWithFilter(Collection<T> collection,
                                         Predicate<T> filter,
                                         Comparator<T> comparator) throws InvalidDataException {
        try {
            validateCollection(collection, "Input collection");

            return collection.stream()
                    .filter(Objects::nonNull)
                    .filter(filter)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InvalidDataException("Failed to sort and filter collection: " + e.getMessage());
        }
    }

    private List<String> sortedFruitsWithFilter(Predicate<String> filter) throws InvalidDataException {
        return sortedWithFilter(fruits, filter, String::compareTo);
    }

    private OptionalDouble safeAverage(Collection<Integer> numbers) {
        return numbers.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average();
    }
}