package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ProductPojo {

    @Id
    private Long globalSkuId;
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;

    private Double mrp;
    private String description;


}