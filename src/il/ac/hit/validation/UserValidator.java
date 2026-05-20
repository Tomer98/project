package il.ac.hit.validation;

import java.util.function.Function;

/**
 * A composable validation rule for a User.
 *
 * <p>Implementations receive a User and return a ValidationResult. Rules can be
 * combined with {@link #and}, {@link #or}, and {@link #xor} to build complex policies.
 */
public interface UserValidator extends Function<User, ValidationResult> {

    // --- Composition operators ---

    /**
     * Returns a validation that passes only when both this rule and {@code other} pass.
     *
     * @param other the second rule to evaluate
     * @return a combined rule representing logical AND
     */
    default UserValidator and(UserValidator other) {
        return user -> {
            // Short-circuit: return the failure immediately if this rule fails
            ValidationResult result = this.apply(user);
            if (!result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    /**
     * Returns a validation that passes when at least one of this rule or {@code other} passes.
     *
     * @param other the second rule to evaluate
     * @return a combined rule representing logical OR
     */
    default UserValidator or(UserValidator other) {
        return user -> {
            // Short-circuit: return success immediately if this rule passes
            ValidationResult result = this.apply(user);
            if (result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    /**
     * Returns a validation that passes only when exactly one of this rule or {@code other} passes.
     *
     * @param other the second rule to evaluate
     * @return a combined rule representing logical XOR
     */
    default UserValidator xor(UserValidator other) {
        return user -> {
            ValidationResult first = this.apply(user);
            ValidationResult second = other.apply(user);
            boolean firstValid = first.isValid();
            boolean secondValid = second.isValid();
            // XOR fails when both rules agree (both pass or both fail)
            if (firstValid ^ secondValid) {
                return new Valid();
            }
            return new Invalid("XOR condition failed: both conditions have the same result");
        };
    }

    // --- Aggregate operators ---

    /**
     * Returns a validation that passes only when every rule in {@code validations} passes.
     *
     * @param validations the rules to evaluate
     * @return a combined rule that requires all rules to pass
     */
    static UserValidator all(UserValidator... validations) {
        return user -> {
            // Return the first failure encountered, or Valid if all pass
            for (UserValidator validation : validations) {
                ValidationResult result = validation.apply(user);
                if (!result.isValid()) {
                    return result;
                }
            }
            return new Valid();
        };
    }

    /**
     * Returns a validation that passes only when none of the rules in {@code validations} passes.
     *
     * @param validations the rules to evaluate
     * @return a combined rule that requires all rules to fail
     */
    static UserValidator none(UserValidator... validations) {
        return user -> {
            // Return Invalid as soon as one rule passes unexpectedly
            for (UserValidator validation : validations) {
                ValidationResult result = validation.apply(user);
                if (result.isValid()) {
                    return new Invalid("Expected no conditions to pass, but one did");
                }
            }
            return new Valid();
        };
    }

    // --- Email rules ---

    /**
     * Returns a rule that checks whether the user's email ends with "il".
     *
     * @return the email domain rule
     */
    static UserValidator emailEndsWithIl() {
        return user -> {
            // Check the final two characters of the email address
            if (user.getEmail().endsWith("il")) {
                return new Valid();
            }
            return new Invalid("Email does not end with 'il'");
        };
    }

    /**
     * Returns a rule that checks whether the user's email is longer than 10 characters.
     *
     * @return the email length rule
     */
    static UserValidator emailLengthBiggerThan10() {
        return user -> {
            // Minimum email length guards against placeholder addresses
            if (user.getEmail().length() > 10) {
                return new Valid();
            }
            return new Invalid("Email length is not greater than 10");
        };
    }

    // --- Password rules ---

    /**
     * Returns a rule that checks whether the user's password is longer than 8 characters.
     *
     * @return the password length rule
     */
    static UserValidator passwordLengthBiggerThan8() {
        return user -> {
            // Passwords shorter than 9 characters are considered too weak
            if (user.getPassword().length() > 8) {
                return new Valid();
            }
            return new Invalid("Password length is not greater than 8");
        };
    }

    /**
     * Returns a rule that checks whether the user's password contains only letters and digits.
     *
     * @return the password character set rule
     */
    static UserValidator passwordIncludesLettersNumbersOnly() {
        return user -> {
            // Regex ensures no special characters are present
            if (user.getPassword().matches("[a-zA-Z0-9]+")) {
                return new Valid();
            }
            return new Invalid("Password includes characters other than letters and numbers");
        };
    }

    // Dollar sign rule — checks for mandatory special character
    /**
     * Returns a rule that checks whether the user's password contains a dollar sign.
     *
     * @return the password dollar sign rule
     */
    static UserValidator passwordIncludesDollarSign() {
        return user -> {
            // Dollar sign is required as a special character in certain tiers
            if (user.getPassword().contains("$")) {
                return new Valid();
            }
            return new Invalid("Password does not include a dollar sign");
        };
    }

    /**
     * Returns a rule that checks whether the user's password differs from their username.
     *
     * @return the password-username inequality rule
     */
    static UserValidator passwordIsDifferentFromUsername() {
        return user -> {
            // Using the username as a password is a well-known security anti-pattern
            if (!user.getPassword().equals(user.getUsername())) {
                return new Valid();
            }
            return new Invalid("Password must be different from username");
        };
    }

    // --- Age and username rules ---

    /**
     * Returns a rule that checks whether the user's age is greater than 18.
     *
     * @return the minimum age rule
     */
    static UserValidator ageBiggerThan18() {
        return user -> {
            // Enforce legal age requirement
            if (user.getAge() > 18) {
                return new Valid();
            }
            return new Invalid("Age is not greater than 18");
        };
    }

    // Username length rule — checks minimum identifier length
    /**
     * Returns a rule that checks whether the user's username is longer than 8 characters.
     *
     * @return the username length rule
     */
    static UserValidator usernameLengthBiggerThan8() {
        return user -> {
            // Short usernames are more susceptible to brute-force guessing
            if (user.getUsername().length() > 8) {
                return new Valid();
            }
            return new Invalid("Username length is not greater than 8");
        };
    }
}
