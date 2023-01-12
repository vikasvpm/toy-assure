package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class OrderItemPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    private Long orderId;
    private Long globalSkuId;
    private Long orderedQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
    private Double sellingPricePerUnit;

}
