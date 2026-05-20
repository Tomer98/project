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
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("User type must not be null or empty");
        }
        switch (type.toLowerCase()) {
            case "basic":
                return new BasicUser(username, email, password, age);
            case "premium":
                return new PremiumUser(username, email, password, age);
            case "platinum":
                return new PlatinumUser(username, email, password, age);
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}
