package org.learning.assure.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.collections.CollectionUtils;
import org.learning.commons.exception.ApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class csvParser {
    public static <T> List<T> parseCSV(byte[] csvBytes, Class<? extends T> type) throws ApiException {
        CsvToBean<T> csvBean = null;
        List<T> parsedData = new ArrayList<>();
        try {
            csvBean = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvBytes), "UTF8"))
                    .withType(type).withThrowExceptions(false).withSkipLines(1).build();
            parsedData = csvBean.parse();
            List<CsvException> errors = csvBean.getCapturedExceptions();
            if (CollectionUtils.isNotEmpty(errors))
                handleErrors(errors);
        }
        catch (UnsupportedEncodingException e) {
            throw new ApiException("Error parsing CSV File");
        }
        return parsedData;
    }

    private static void handleErrors(List<CsvException> errors) throws ApiException {
        List<String> errorMessages = new ArrayList<>();
        for (CsvException e : errors) {
            String errorMessage = e.getMessage().replaceAll("\\.", "");
            errorMessages.add(errorMessage + " at line number " + e.getLineNumber());
        }
        throw new ApiException(errorMessages);
    }
}
