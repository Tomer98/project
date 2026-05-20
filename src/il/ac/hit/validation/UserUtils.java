package il.ac.hit.validation;

import java.util.Comparator;

/**
 * Utility methods for working with User arrays.
 */
public class UserUtils {

    /**
     * Sorts the given array in place using bubble sort and the provided comparator.
     *
     * @param users      the array to sort
     * @param comparator the ordering strategy
     * @throws IllegalArgumentException if users or comparator is null
     */
    public static void sort(User[] users, Comparator<User> comparator) {
        if (users == null) {
            throw new IllegalArgumentException("Users array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        for (int i = 0; i < users.length - 1; i++) {
            for (int j = 0; j < users.length - 1 - i; j++) {
                if (comparator.compare(users[j], users[j + 1]) > 0) {
                    User temp = users[j];
                    users[j] = users[j + 1];
                    users[j + 1] = temp;
                }
            }
        }
    }
}
