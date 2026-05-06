package il.ac.hit.validation;

import java.util.Optional;

public interface ValidationResult {

    boolean isValid();

    Optional<String> getReason();
}
