package il.ac.hit.validation;

import java.util.Optional;

/**
 * A ValidationResult representing a failed validation with a reason.
 */
public class Invalid implements ValidationResult {

    /** The explanation of why validation failed. */
    private final String reason;

    /**
     * Creates an Invalid result with the given failure reason.
     *
     * @param reason the explanation of why validation failed
     * @throws IllegalArgumentException if reason is null or empty
     */
    public Invalid(String reason) {
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Reason must not be null or empty");
        }
        this.reason = reason;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Optional<String> getReason() {
        return Optional.of(reason);
    }

    @Override
    public String toString() {
        return "Invalid{reason='" + reason + "'}";
    }
}
