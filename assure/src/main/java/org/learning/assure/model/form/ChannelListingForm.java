package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingForm {

    private Long globalSkuId;
    // TODO : Take clientSkuId here and then fetch globalSkuId from that
    private String channelSkuId;
}
