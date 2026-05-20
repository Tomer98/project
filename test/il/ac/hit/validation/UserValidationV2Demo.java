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
        User user = new User("admin", "admin@#yzw.co.il", "abc123", 34);

        UserValidator validation1 = UserValidator.emailLengthBiggerThan10();
        UserValidator validation2 = UserValidator.emailEndsWithIL();

        ValidationResult result = (validation1.and(validation2)).apply(user);

        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid");
            result.getReason().ifPresent(r -> System.out.println("Reason: " + r));
        }
    }
}
