package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrderForm {
    private String channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
