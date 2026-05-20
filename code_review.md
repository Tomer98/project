# Code Review — User Validation Project
## Package: `il.ac.hit.validation`

---

## 1. ValidationResult.java

```java
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
```

---

## 2. Valid.java

```java
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

    // Reason accessor — always empty for a successful result
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

    // String representation — fixed since Valid holds no state
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
```

---

## 3. Invalid.java

```java
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
        // A failed result always reports false
        return false;
    }

    // Reason accessor — always present since Invalid requires a non-empty reason
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

    // String representation — includes reason for easy debugging
    /**
     * Returns a human-readable representation of this result.
     *
     * @return a string containing the failure reason
     */
    @Override
    public String toString() {
        // Include the reason so callers can read it directly from the string
        return "Invalid{reason='" + reason + "'}";
    }
}
```

---

## 4. UserValidator.java

```java
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
```

---

## 5. User.java

```java
package il.ac.hit.validation;

import java.util.Objects;

/**
 * Abstract base class representing a user with login credentials and demographic info.
 *
 * <p>Defines the template method {@link #validate()} which calls the hook
 * {@link #buildValidator()} so each tier applies its own validation rules.
 */
public abstract class User {

    // --- Fields ---

    /** The user's login name. */
    private String username;

    /** The user's email address. */
    private String email;

    // Password and age complete the core user profile
    /** The user's password. */
    private String password;

    /** The user's age. */
    private int age;

    // --- Constructor ---

    /**
     * Creates a fully initialized User by delegating all assignment to setters.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     * @throws IllegalArgumentException if any argument fails validation
     */
    public User(String username, String email, String password, int age) {
        // Delegating to setters centralizes all validation logic in one place
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setAge(age);
    }

    // --- Template method ---

    /**
     * Validates this user using the tier-specific rule.
     * This is the template method — it calls {@link #buildValidator()} as its hook.
     *
     * @return the validation result for this user
     */
    public final ValidationResult validate() {
        // Retrieve the tier-specific rule and apply it to this instance
        return buildValidator().apply(this);
    }

    /**
     * Returns the validation rule specific to this user tier.
     * Hook method — each concrete subclass defines its own implementation.
     *
     * @return the UserValidator for this tier
     */
    protected abstract UserValidator buildValidator();

    // --- Getters ---

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        // Return the stored username
        return username;
    }

    /**
     * Returns the email address.
     *
     * @return the email
     */
    public String getEmail() {
        // Return the stored email address
        return email;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        // Return the stored password
        return password;
    }

    /**
     * Returns the age.
     *
     * @return the age
     */
    public int getAge() {
        // Return the stored age
        return age;
    }

    // --- Setters with validation ---

    /**
     * Sets the username.
     *
     * @param username the login name
     * @throws IllegalArgumentException if username is null or empty
     */
    public void setUsername(String username) {
        // Reject null or empty values before assignment
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        this.username = username;
    }

    // Email field setter
    /**
     * Sets the email address.
     *
     * @param email the email address
     * @throws IllegalArgumentException if email is null or empty
     */
    public void setEmail(String email) {
        // Reject null or empty values before assignment
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        this.email = email;
    }

    // Password field setter
    /**
     * Sets the password.
     *
     * @param password the password
     * @throws IllegalArgumentException if password is null or empty
     */
    public void setPassword(String password) {
        // Reject null or empty values before assignment
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        this.password = password;
    }

    // Age field setter
    /**
     * Sets the age.
     *
     * @param age the user's age
     * @throws IllegalArgumentException if age is negative
     */
    public void setAge(int age) {
        // Age cannot be negative
        if (age < 0) {
            throw new IllegalArgumentException("Age must not be negative");
        }
        this.age = age;
    }

    // --- Object overrides ---

    /**
     * Checks equality based on all four fields.
     *
     * @param o the object to compare with
     * @return true if all fields match
     */
    @Override
    public boolean equals(Object o) {
        // Check reference equality first as a fast path
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        // Compare every field to determine logical equality
        User other = (User) o;
        return age == other.age
                && Objects.equals(username, other.username)
                && Objects.equals(email, other.email)
                && Objects.equals(password, other.password);
    }

    /**
     * Returns a hash code consistent with {@link #equals}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // Combine all fields for a hash consistent with equals
        return Objects.hash(username, email, password, age);
    }

    /**
     * Returns a human-readable representation of this user.
     *
     * @return a string containing username, email, and age
     */
    @Override
    public String toString() {
        // Format identifying fields as a readable string
        return "User{username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
```

---

## 6. BasicUser.java

```java
package il.ac.hit.validation;

/**
 * A user with basic tier access.
 *
 * <p>Applies minimal validation: age must be over 18 and
 * username must be longer than 8 characters.
 */
public class BasicUser extends User {

    /**
     * Creates a BasicUser with the given credentials and age.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     */
    public BasicUser(String username, String email, String password, int age) {
        // Delegate full initialization to the primary constructor in User
        super(username, email, password, age);
    }

    // Hook implementation for tier-specific validation
    /**
     * Returns the validation rule for the basic tier.
     * Hook implementation — requires age over 18 and username longer than 8 chars.
     *
     * @return the validator for basic users
     */
    @Override
    protected UserValidator buildValidator() {
        // Basic tier enforces minimum age and username length only
        return UserValidator.ageBiggerThan18()
                .and(UserValidator.usernameLengthBiggerThan8());
    }

    /**
     * Returns a human-readable representation of this BasicUser.
     *
     * @return a string containing the tier label, username, email, and age
     */
    @Override
    public String toString() {
        // Include tier label so the output distinguishes BasicUser from other subtypes
        return "BasicUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
```

---

## 7. PremiumUser.java

```java
package il.ac.hit.validation;

/**
 * A user with premium tier access.
 *
 * <p>Applies stricter validation than the basic tier: all basic rules plus
 * password must be longer than 8 characters.
 */
public class PremiumUser extends User {

    /**
     * Creates a PremiumUser with the given credentials and age.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     */
    public PremiumUser(String username, String email, String password, int age) {
        // Delegate full initialization to the primary constructor in User
        super(username, email, password, age);
    }

    // Hook implementation for tier-specific validation
    /**
     * Returns the validation rule for the premium tier.
     * Hook implementation — adds a password-length requirement on top of basic rules.
     *
     * @return the validator for premium users
     */
    @Override
    protected UserValidator buildValidator() {
        // Premium tier: basic rules plus password must exceed 8 characters
        return UserValidator.ageBiggerThan18()
                .and(UserValidator.usernameLengthBiggerThan8())
                .and(UserValidator.passwordLengthBiggerThan8());
    }

    /**
     * Returns a human-readable representation of this PremiumUser.
     *
     * @return a string containing the tier label, username, email, and age
     */
    @Override
    public String toString() {
        // Include tier label so the output distinguishes PremiumUser from other subtypes
        return "PremiumUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
```

---

## 8. PlatinumUser.java

```java
package il.ac.hit.validation;

/**
 * A user with platinum tier access.
 *
 * <p>Applies the strictest validation: all premium rules plus
 * password must differ from username and email must end with "il".
 */
public class PlatinumUser extends User {

    /**
     * Creates a PlatinumUser with the given credentials and age.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     */
    public PlatinumUser(String username, String email, String password, int age) {
        // Delegate full initialization to the primary constructor in User
        super(username, email, password, age);
    }

    // Hook implementation for tier-specific validation
    /**
     * Returns the validation rule for the platinum tier.
     * Hook implementation — the strictest rule set, requiring Israeli email and unique password.
     *
     * @return the validator for platinum users
     */
    @Override
    protected UserValidator buildValidator() {
        // Platinum tier: premium rules plus password uniqueness and Israeli email domain
        return UserValidator.ageBiggerThan18()
                .and(UserValidator.usernameLengthBiggerThan8())
                .and(UserValidator.passwordLengthBiggerThan8())
                .and(UserValidator.passwordIsDifferentFromUsername())
                .and(UserValidator.emailEndsWithIl());
    }

    /**
     * Returns a human-readable representation of this PlatinumUser.
     *
     * @return a string containing the tier label, username, email, and age
     */
    @Override
    public String toString() {
        // Include tier label so the output distinguishes PlatinumUser from other subtypes
        return "PlatinumUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
```

---

## 9. UserFactory.java

```java
package il.ac.hit.validation;

/**
 * Factory for creating User instances by tier type.
 */
public class UserFactory {

    /**
     * Creates a User of the specified tier type.
     *
     * @param type     the user tier ("basic", "premium", or "platinum")
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     * @return a User instance of the requested type
     * @throws IllegalArgumentException if type is null, empty, or unrecognized
     */
    public static User createUser(String type, String username, String email, String password, int age) {
        // Validate the type argument before calling toLowerCase to avoid NullPointerException
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("User type must not be null or empty");
        }
        // Dispatch to the concrete subclass matching the requested tier
        switch (type.toLowerCase()) {
            case "basic":
                return new BasicUser(username, email, password, age);
            case "premium":
                return new PremiumUser(username, email, password, age);
            case "platinum":
                return new PlatinumUser(username, email, password, age);
            default:
                // No matching tier — inform the caller with a descriptive message
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
```

---

## 10. UserUtils.java

```java
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
        // Validate both arguments before touching the array
        if (users == null) {
            throw new IllegalArgumentException("Users array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null");
        }
        /* Bubble sort: each pass bubbles the largest unsorted element to its final position */
        for (int i = 0; i < users.length - 1; i++) {
            for (int j = 0; j < users.length - 1 - i; j++) {
                // Swap adjacent elements when they are out of order
                if (comparator.compare(users[j], users[j + 1]) > 0) {
                    User temp = users[j];
                    users[j] = users[j + 1];
                    users[j + 1] = temp;
                }
            }
        }
    }
}
```

---

## 11. UserValidationV3Demo.java

```java
package il.ac.hit.validation;

/**
 * Demonstrates composing UserValidator rules with logical AND.
 */
public class UserValidationV3Demo {

    /**
     * Entry point for the validation demo.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Use BasicUser — User is abstract and cannot be instantiated directly
        User user = new BasicUser("admin", "my-long-email@example.com", "abc123456", 34);

        System.out.println("--- Running validation test ---");

        /* Build the rule: email must be longer than 10 chars AND end with "il" */
        UserValidator validation = UserValidator.emailLengthBiggerThan10()
                .and(UserValidator.emailEndsWithIl());

        // Apply the composed rule to the user and inspect the result
        ValidationResult result = validation.apply(user);

        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid. Reason: " + result.getReason().orElse("No reason given"));
        }

        System.out.println("--- Test finished ---");
    }
}
```
