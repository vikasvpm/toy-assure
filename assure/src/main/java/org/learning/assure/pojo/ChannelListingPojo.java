package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ChannelListingPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "channellisting_generator")
    @TableGenerator(name = "channellisting_generatorr", initialValue = 100, allocationSize = 1)
    private Long channelListingId;
    private Long channelId;
    private String channelSkuId;
    private Long clientId;
    private Long globalSkuId;

}
