package org.learning.assure.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiException extends Exception{
    List<String> errors;
    public ApiException(List<String> errorList) {
        this.errors = errorList;
    }

    public ApiException(String error) {
        List<String> errors = new ArrayList<>();
        errors.add(error);
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        String msg = String.join(", ", errors);
        return msg;
    }
}
