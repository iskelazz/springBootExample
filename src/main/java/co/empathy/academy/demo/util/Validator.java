package co.empathy.academy.demo.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Validator {
    public static void validateMandatoryString(String propertyName,
            String stringValue) throws InputValidationException {

        if ( (stringValue == null) || (stringValue.trim().length() == 0) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + propertyName +
                    " value (it cannot be null neither empty): " +
                    stringValue);
        }

    }
}
