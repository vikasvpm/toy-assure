package org.learning.assure.model.form;

import lombok.Data;

@Data
public class ChannelOrderForm {
    private String channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
