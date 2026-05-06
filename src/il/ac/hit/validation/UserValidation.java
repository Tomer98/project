package il.ac.hit.validation;

import java.util.function.Function;

public interface UserValidation extends Function<User, ValidationResult> {

    default UserValidation and(UserValidation other) {
        return user -> {
            ValidationResult result = this.apply(user);
            if (!result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    default UserValidation or(UserValidation other) {
        return user -> {
            ValidationResult result = this.apply(user);
            if (result.isValid()) {
                return result;
            }
            return other.apply(user);
        };
    }

    default UserValidation xor(UserValidation other) {
        return user -> {
            ValidationResult first = this.apply(user);
            ValidationResult second = other.apply(user);
            boolean firstValid = first.isValid();
            boolean secondValid = second.isValid();
            if (firstValid ^ secondValid) {
                return new Valid();
            }
            return new Invalid("XOR condition failed: both conditions have the same result");
        };
    }

    static UserValidation all(UserValidation... validations) {
        return user -> {
            for (UserValidation validation : validations) {
                ValidationResult result = validation.apply(user);
                if (!result.isValid()) {
                    return result;
                }
            }
            return new Valid();
        };
    }

    static UserValidation none(UserValidation... validations) {
        return user -> {
            for (UserValidation validation : validations) {
                ValidationResult result = validation.apply(user);
                if (result.isValid()) {
                    return new Invalid("Expected no conditions to pass, but one did");
                }
            }
            return new Valid();
        };
    }

    static UserValidation emailEndsWithIL() {
        return user -> {
            if (user.getEmail().endsWith("il")) {
                return new Valid();
            }
            return new Invalid("Email does not end with 'il'");
        };
    }

    static UserValidation emailLengthBiggerThan10() {
        return user -> {
            if (user.getEmail().length() > 10) {
                return new Valid();
            }
            return new Invalid("Email length is not greater than 10");
        };
    }

    static UserValidation passwordLengthBiggerThan8() {
        return user -> {
            if (user.getPassword().length() > 8) {
                return new Valid();
            }
            return new Invalid("Password length is not greater than 8");
        };
    }

    static UserValidation passwordIncludesLettersNumbersOnly() {
        return user -> {
            if (user.getPassword().matches("[a-zA-Z0-9]+")) {
                return new Valid();
            }
            return new Invalid("Password includes characters other than letters and numbers");
        };
    }

    static UserValidation passwordIncludesDollarSign() {
        return user -> {
            if (user.getPassword().contains("$")) {
                return new Valid();
            }
            return new Invalid("Password does not include a dollar sign");
        };
    }

    static UserValidation passwordIsDifferentFromUsername() {
        return user -> {
            if (!user.getPassword().equals(user.getUsername())) {
                return new Valid();
            }
            return new Invalid("Password must be different from username");
        };
    }

    static UserValidation ageBiggerThan18() {
        return user -> {
            if (user.getAge() > 18) {
                return new Valid();
            }
            return new Invalid("Age is not greater than 18");
        };
    }

    static UserValidation usernameLengthBiggerThan8() {
        return user -> {
            if (user.getUsername().length() > 8) {
                return new Valid();
            }
            return new Invalid("Username length is not greater than 8");
        };
    }
}
