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
                .and(UserValidator.emailEndsWithIL());
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
