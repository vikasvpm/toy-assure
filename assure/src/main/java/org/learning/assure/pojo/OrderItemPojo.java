package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderItemPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "orderitem_generator")
    @TableGenerator(name = "orderitem_generator", initialValue = 100, allocationSize = 1)
    private Long orderItemId;
    private Long orderId;
    private Long globalSkuId;
    private Long orderedQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
    private Double sellingPricePerUnit;

}
