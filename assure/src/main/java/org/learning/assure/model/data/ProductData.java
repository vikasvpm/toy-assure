package org.learning.assure.model.data;

import lombok.Data;
import org.learning.assure.model.form.ProductForm;

@Data
public class ProductData extends ProductForm {
    private Long globalSkuId;
}
