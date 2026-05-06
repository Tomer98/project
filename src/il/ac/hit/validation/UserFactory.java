package il.ac.hit.validation;

public class UserFactory {

    public static User createUser(String type, String username, String email, String password, int age) {
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
