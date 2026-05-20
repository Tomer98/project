package il.ac.hit.validation;

import java.util.Optional;

/**
 * A ValidationResult representing a successful validation.
 */
public class Valid implements ValidationResult {

    // --- ValidationResult implementation ---

    /**
     * Returns true because this result represents a passed validation.
     *
     * @return true always
     */
    @Override
    public boolean isValid() {
        // A valid result always reports true
        return true;
    }

    /**
     * Returns an empty Optional because a valid result has no failure reason.
     *
     * @return an empty Optional
     */
    @Override
    public Optional<String> getReason() {
        // No failure reason exists for a successful validation
        return Optional.empty();
    }

    /**
     * Returns a human-readable representation of this result.
     *
     * @return "Valid{}"
     */
    @Override
    public String toString() {
        // Produce a fixed label — Valid carries no extra state
        return "Valid{}";
    }
}
