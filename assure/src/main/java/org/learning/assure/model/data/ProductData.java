package org.learning.assure.model.data;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.form.ProductForm;

@Getter
@Setter
public class ProductData extends ProductForm {
    private Long globalSkuId;
    private Long clientId;
}
