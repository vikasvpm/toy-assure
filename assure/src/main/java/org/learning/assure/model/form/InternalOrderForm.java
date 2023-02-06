package org.learning.assure.model.form;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalOrderForm {
    @CsvBindByPosition(position = 0, required = true)
    private String clientSkuId;

    @CsvBindByPosition(position = 1, required = true)
    private Long orderedQuantity;

    @CsvBindByPosition(position = 2, required = true)
    private Double sellingPricePerUnit;
}
