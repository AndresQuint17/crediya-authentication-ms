package co.com.myproject.usecase.validation;

public enum EValidationFieldMessages {
    FIRST_NAME_REQUIRED("First name cannot be null or empty", 400),
    LAST_NAME_REQUIRED("Last name cannot be null or empty", 400),
    ID_CARD_REQUIRED("ID card cannot be null or empty", 400),
    DOB_MUST_BE_IN_PAST("Date of birth must be in the past", 400),
    EMAIL_REQUIRED("Email cannot be null or empty", 400),
    EMAIL_INVALID("Email must be valid", 400),
    EMAIL_ALREADY_REGISTERED("Email user already registered: ", 400),
    BASE_SALARY_REQUIRED("Base salary cannot be null", 400),
    BASE_SALARY_MIN("Base salary must be greater than or equal to 0", 400),
    BASE_SALARY_MAX("Base salary must be less than or equal to 15000000", 400),
    USER_DOES_NOT_EXIST("User does not exits", 404),
    ROLE_DOES_NOT_EXIST("User does not have a role assigned", 404);

    private final String message;
    private final int statusCode;

    EValidationFieldMessages(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
