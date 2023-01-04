package org.learning.assure.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class InventoryPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long inventoryId;

    private Long globalSkuId;

    private Long availableQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

}
