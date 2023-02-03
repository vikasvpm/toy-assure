package org.learning.assure.dto.helper;

import org.learning.commons.exception.ApiException;

import java.util.List;

public class ThrowExceptionHelper {
    public static void throwIfErrors(List<String> errorList) throws ApiException {
        if(!errorList.isEmpty()) {
            throw new ApiException(errorList);
        }
    }
}
