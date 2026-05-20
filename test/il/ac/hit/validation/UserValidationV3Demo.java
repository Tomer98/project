package il.ac.hit.validation;

/**
 * Demonstrates composing UserValidator rules with logical AND.
 */
public class UserValidationV3Demo {

    /**
     * Entry point for the validation demo.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Use BasicUser — User is abstract and cannot be instantiated directly
        User user = new BasicUser("admin", "my-long-email@example.com", "abc123456", 34);

        System.out.println("--- Running validation test ---");

        /* Build the rule: email must be longer than 10 chars AND end with "il" */
        UserValidator validation = UserValidator.emailLengthBiggerThan10()
                .and(UserValidator.emailEndsWithIl());

        // Apply the composed rule to the user and inspect the result
        ValidationResult result = validation.apply(user);

        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid. Reason: " + result.getReason().orElse("No reason given"));
        }

        System.out.println("--- Test finished ---");
    }
}
