package org.learning.assure.model.form;

import lombok.Data;

@Data
public class InternalOrderForm {
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
