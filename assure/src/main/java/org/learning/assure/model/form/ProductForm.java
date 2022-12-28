package org.learning.assure.model.form;

import lombok.Getter;

@Getter
public class ProductForm {
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
