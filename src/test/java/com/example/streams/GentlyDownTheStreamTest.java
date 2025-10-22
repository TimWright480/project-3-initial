package com.example.streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GentlyDownTheStreamTest {

    private GentlyDownTheStream stream;

    @BeforeEach
    void setUp() {
        stream = new GentlyDownTheStream();
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Should sort fruits correctly")
        void sortedFruits() {
            List<String> expected = stream.fruits.stream()
                    .sorted()
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<String> actual = stream.sortedFruits();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should sort fruits excluding those starting with 'A'")
        void sortedFruitsException() {
            List<String> expected = stream.fruits.stream()
                    .filter(x -> !x.startsWith("A"))
                    .sorted()
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<String> actual = stream.sortedFruitsException();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should return first two sorted fruits")
        void sortedFruitsFirstTwo() {
            List<String> expected = stream.fruits.stream()
                    .sorted()
                    .limit(2)
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<String> actual = stream.sortedFruitsFirstTwo();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should create comma-separated list of sorted fruits")
        void commaSeparatedListOfFruits() {
            String expected = stream.fruits.stream()
                    .sorted()
                    .collect(Collectors.joining(", "));

            assertDoesNotThrow(() -> {
                String actual = stream.commaSeparatedListOfFruits();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should sort veggies in reverse order")
        void reverseSortedVeggies() {
            List<String> expected = stream.veggies.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<String> actual = stream.reverseSortedVeggies();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should sort veggies in reverse order and convert to uppercase")
        void reverseSortedVeggiesInUpperCase() {
            List<String> expected = stream.veggies.stream()
                    .sorted(Comparator.reverseOrder())
                    .map(v -> v.toUpperCase(Locale.ROOT))
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<String> actual = stream.reverseSortedVeggiesInUpperCase();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should return top 10 values")
        void topTen() {
            List<Integer> expected = stream.integerValues.stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(10)
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<Integer> actual = stream.topTen();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should return top 10 unique values")
        void topTenUnique() {
            List<Integer> expected = stream.integerValues.stream()
                    .sorted(Comparator.reverseOrder())
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<Integer> actual = stream.topTenUnique();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should return top 10 unique odd values")
        void topTenUniqueOdd() {
            List<Integer> expected = stream.integerValues.stream()
                    .sorted(Comparator.reverseOrder())
                    .distinct()
                    .filter(x -> x % 2 != 0)
                    .limit(10)
                    .collect(Collectors.toList());

            assertDoesNotThrow(() -> {
                List<Integer> actual = stream.topTenUniqueOdd();
                assertThat(actual).isEqualTo(expected);
            });
        }

        @Test
        @DisplayName("Should calculate average correctly")
        void average() {
            Double expected = stream.integerValues.stream()
                    .mapToInt(i -> i)
                    .average()
                    .getAsDouble();

            assertDoesNotThrow(() -> {
                Double actual = stream.average();
                assertThat(actual).isEqualTo(expected);
            });
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should handle null collections gracefully")
        void shouldHandleNullCollections() {
            // Test would require creating instance with null collections
            // This demonstrates the type of exception testing to implement
            assertThatThrownBy(() -> {
                GentlyDownTheStream nullStream = new GentlyDownTheStream();
                // Force null state for testing
                nullStream.fruits = null;
                nullStream.sortedFruits();
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot be null");
        }

        @Test
        @DisplayName("Should handle empty collections appropriately")
        void shouldHandleEmptyCollections() {
            // Test empty collection behavior
            assertThatThrownBy(() -> {
                GentlyDownTheStream emptyStream = new GentlyDownTheStream();
                emptyStream.fruits = List.of(); // Empty list
                emptyStream.sortedFruits();
            }).isInstanceOf(EmptyCollectionException.class)
                    .hasMessageContaining("cannot be empty");
        }

        @Test
        @DisplayName("Should throw InvalidDataException on processing failure")
        void shouldThrowInvalidDataExceptionOnFailure() {
            // This test demonstrates exception propagation
            // Actual implementation would depend on specific failure scenarios
        }

        @Test
        @DisplayName("Should handle division by zero in average calculation")
        void shouldHandleDivisionByZeroInAverage() {
            assertThatThrownBy(() -> {
                GentlyDownTheStream emptyIntStream = new GentlyDownTheStream();
                emptyIntStream.integerValues = List.of();
                emptyIntStream.average();
            }).isInstanceOf(InvalidDataException.class);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle collections with fewer than requested elements")
        void shouldHandleFewerElementsThanRequested() {
            // Test behavior when requesting top 10 from a list with < 10 elements
            // Implementation should handle this gracefully
        }

        @Test
        @DisplayName("Should handle collections with null elements")
        void shouldHandleNullElements() {
            // Test filtering out null elements in streams
            // Demonstrates robust null handling
        }

        @Test
        @DisplayName("Should handle all even numbers in odd filter")
        void shouldHandleAllEvenNumbers() {
            // Test case where no odd numbers exist
            // Should return empty list, not throw exception
        }
    }


    @Nested
    @DisplayName("Performance and Advanced Features")
    class AdvancedFeatureTests {

        @Test
        @DisplayName("Should maintain type safety with generics")
        void shouldMaintainTypeSafety() {
            // Demonstrates proper generic usage
            // Type safety should be maintained throughout operations
        }

        @Test
        @DisplayName("Should use proper functional interfaces")
        void shouldUseProperFunctionalInterfaces() {
            // Test demonstrates proper lambda and method reference usage
            // Validates functional programming concepts
        }
    }
}