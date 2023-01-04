package org.learning.assure.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ChannelListingPojo extends AbstractPojo {

    @Id
    private Long channelListingId;

    private Long channelId;
    private String channelSkuId;
    private Long clientId;
    private Long globalSkuId;

}
