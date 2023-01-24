package org.learning.assure.model.form;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;

@Getter
public class ProductForm {
    @CsvBindByPosition(position = 0, required = true)
    private String clientSkuId;
    @CsvBindByPosition(position = 1, required = true)

    private String name;
    @CsvBindByPosition(position = 2, required = true)
    private String brandId;
    @CsvBindByPosition(position = 3, required = true)
    private Double mrp;
    @CsvBindByPosition(position = 4, required = true)
    private String description;
}
