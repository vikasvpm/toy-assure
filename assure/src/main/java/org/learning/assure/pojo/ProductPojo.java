package org.learning.assure.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ProductPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalSkuId;
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;

    private Double mrp;
    private String description;


}
