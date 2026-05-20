package il.ac.hit.validation;

import java.util.Optional;

/**
 * A ValidationResult representing a failed validation with a reason.
 */
public class Invalid implements ValidationResult {

    /** The explanation of why validation failed. */
    private final String reason;

    // --- Constructor ---

    /**
     * Creates an Invalid result with the given failure reason.
     *
     * @param reason the explanation of why validation failed
     * @throws IllegalArgumentException if reason is null or empty
     */
    public Invalid(String reason) {
        // A meaningful reason is required so callers can diagnose the failure
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Reason must not be null or empty");
        }
        this.reason = reason;
    }

    // --- ValidationResult implementation ---

    /**
     * Returns false because this result represents a failed validation.
     *
     * @return false always
     */
    @Override
    public boolean isValid() {
        return false;
    }

    /**
     * Returns the failure reason wrapped in an Optional.
     *
     * @return an Optional containing the reason for failure
     */
    @Override
    public Optional<String> getReason() {
        // Always present — Invalid is always constructed with a non-empty reason
        return Optional.of(reason);
    }

    /**
     * Returns a human-readable representation of this result.
     *
     * @return a string containing the failure reason
     */
    @Override
    public String toString() {
        return "Invalid{reason='" + reason + "'}";
    }
}
