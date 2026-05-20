package il.ac.hit.validation;

/**
 * A user with basic tier access.
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
        super(username, email, password, age);
    }

    @Override
    public String toString() {
        return "BasicUser{username='" + getUsername() + "', email='" + getEmail() + "', age=" + getAge() + "}";
    }
}
