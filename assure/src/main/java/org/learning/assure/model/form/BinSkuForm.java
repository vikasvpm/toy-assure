package org.learning.assure.model.form;

import lombok.Data;

@Data
public class BinSkuForm {
    private Long binId;
    private String clientSkuId;
    private Long quantity;
}
