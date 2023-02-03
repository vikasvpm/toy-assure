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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "inventory_generator")
    @TableGenerator(name = "inventory_generator", initialValue = 100, allocationSize = 1)
    private Long inventoryId;

    private Long globalSkuId;

    private Long availableQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

}
