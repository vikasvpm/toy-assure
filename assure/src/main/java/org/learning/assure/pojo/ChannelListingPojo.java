package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ChannelListingPojo {

    @Id
    private Long channelListingId;

    private Long channelId;
    private String channelSkuId;
    private Long clientId;
    private Long globalSkuId;

}
