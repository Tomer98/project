package il.ac.hit.validation;

import java.util.Optional;

/**
 * A ValidationResult representing a successful validation.
 */
public class Valid implements ValidationResult {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Optional<String> getReason() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Valid{}";
    }
}
