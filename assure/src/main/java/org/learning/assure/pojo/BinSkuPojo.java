package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BinSkuPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "binsku_generator")
    @TableGenerator(name = "binsku_generator", initialValue = 100, allocationSize = 1)
    private Long binSkuId;
    private Long binId;
    private Long globalSkuId;
    private Long quantity;



}
