package il.ac.hit.validation;

/**
 * Manual test entry point for UserValidator AND composition.
 */
public class UserValidationV2Demo {

    /**
     * Entry point.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Use BasicUser — User is abstract and cannot be instantiated directly
        User user = new BasicUser("admin", "admin@#yzw.co.il", "abc123", 34);

        /* Compose two rules with AND: both email conditions must hold */
        UserValidator validation1 = UserValidator.emailLengthBiggerThan10();
        UserValidator validation2 = UserValidator.emailEndsWithIL();

        // Apply the combined rule and print the outcome
        ValidationResult result = (validation1.and(validation2)).apply(user);

        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid");
            result.getReason().ifPresent(r -> System.out.println("Reason: " + r));
        }
    }
}
