package il.ac.hit.validation;

/**
 * A user with premium tier access.
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
        super(username, email, password, age);
    }

    @Override
    public String toString() {
        return "PremiumUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
