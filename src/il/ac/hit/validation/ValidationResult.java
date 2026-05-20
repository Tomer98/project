package il.ac.hit.validation;

import java.util.Optional;

/**
 * Represents the outcome of a user validation check.
 */
public interface ValidationResult {

    // --- Query methods ---

    /**
     * Returns true if the validation passed.
     *
     * @return true when valid, false otherwise
     */
    boolean isValid();

    /**
     * Returns the failure reason, or empty if the result is valid.
     *
     * @return an Optional containing the reason for failure
     */
    Optional<String> getReason();
}
