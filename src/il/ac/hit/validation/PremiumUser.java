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
