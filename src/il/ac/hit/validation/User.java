package il.ac.hit.validation;

import java.util.Objects;

/**
 * Represents a user in the system with login credentials and demographic info.
 */
public class User {

    /** The user's login name. */
    private String username;

    /** The user's email address. */
    private String email;

    /** The user's password. */
    private String password;

    /** The user's age. */
    private int age;

    /**
     * Creates a fully initialized User.
     *
     * @param username the login name
     * @param email    the email address
     * @param password the password
     * @param age      the user's age
     * @throws IllegalArgumentException if any argument fails validation
     */
    public User(String username, String email, String password, int age) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setAge(age);
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the login name
     * @throws IllegalArgumentException if username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        this.username = username;
    }

    /**
     * Returns the email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address
     * @throws IllegalArgumentException if email is null or empty
     */
    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        this.email = email;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password
     * @throws IllegalArgumentException if password is null or empty
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        this.password = password;
    }

    /**
     * Returns the age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age.
     *
     * @param age the user's age
     * @throws IllegalArgumentException if age is negative
     */
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age must not be negative");
        }
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return age == other.age
                && Objects.equals(username, other.username)
                && Objects.equals(email, other.email)
                && Objects.equals(password, other.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, age);
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
