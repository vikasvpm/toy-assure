package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalOrderForm {
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
