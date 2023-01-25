package org.learning.assure.dto.helper;

import org.learning.assure.exception.ApiException;

import java.util.List;

public class ThrowExceptionHelper {
    public static void throwIfErrors(List<String> errorList) throws ApiException {
        if(!errorList.isEmpty()) {
            String errorMessage = String.join(", ", errorList);
            throw new ApiException(errorMessage);
        }
    }
}
