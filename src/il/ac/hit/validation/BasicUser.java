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
        return "BasicUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
