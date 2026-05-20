package il.ac.hit.validation;

/**
 * Demonstrates composing UserValidator rules with logical AND.
 */
public class UserValidationV2Demo {

    /**
     * Entry point for the validation demo.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        User user = new User("admin", "my-long-email@example.com", "abc123456", 34);

        System.out.println("--- Running validation test ---");

        UserValidator validation = UserValidator.emailLengthBiggerThan10()
                .and(UserValidator.emailEndsWithIL());

        ValidationResult result = validation.apply(user);

        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid. Reason: " + result.getReason().orElse("No reason given"));
        }

        System.out.println("--- Test finished ---");
    }
}
