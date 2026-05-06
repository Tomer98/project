package il.ac.hit.validation;

public class UserValidationV2Demo {
    public static void main(String[] args) {
        // יצירת משתמש לבדיקה
        User user = new User("admin", "my-long-email@example.com", "abc123456", 34);

        System.out.println("--- Running validation test ---");

        // בניית חוק אימות: אימייל ארוך מ-10 תווים וגם מסתיים ב-"il"
        UserValidation validation = UserValidation.emailLengthBiggerThan10()
                .and(UserValidation.emailEndsWithIL());

        // הרצת האימות על המשתמש
        ValidationResult result = validation.apply(user);

        // בדיקת התוצאה והדפסה למסך
        if (result.isValid()) {
            System.out.println("User is valid");
        } else {
            System.out.println("User is not valid. Reason: " + result.getReason().orElse("No reason given"));
        }

        System.out.println("--- Test finished ---");
    }
}
    