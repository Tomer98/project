package il.ac.hit.validation;

/**
 * A user with platinum tier access.
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
        super(username, email, password, age);
    }

    @Override
    public String toString() {
        return "PlatinumUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
