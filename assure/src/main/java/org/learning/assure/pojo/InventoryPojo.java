package org.learning.assure.pojo;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class InventoryPojo {

    @Id
    private Long inventoryId;

    private Long globalSkuId;

    private Long availableQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

}
