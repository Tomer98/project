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
        return Objects.hash(username, email, password, age);
    }

    /**
     * Returns a human-readable representation of this user.
     *
     * @return a string containing username, email, and age
     */
    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
