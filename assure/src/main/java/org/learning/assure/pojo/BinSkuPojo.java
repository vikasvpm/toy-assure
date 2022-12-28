package org.learning.assure.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class BinSkuPojo {

    @Id
    private Long binSkuId;
    private Long binId;
    private Long globalSkuId;
    private Long quantity;



}
