package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class OrderItemPojo {

    @Id
    private Long orderItemId;
    private Long orderId;
    private Long globalSkuId;
    private Long orderedQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
    private Double sellingPricePerUnit;

}
