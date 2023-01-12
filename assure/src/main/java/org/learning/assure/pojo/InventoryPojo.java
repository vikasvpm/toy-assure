package org.learning.assure.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class InventoryPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    private Long globalSkuId;

    private Long availableQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

}
