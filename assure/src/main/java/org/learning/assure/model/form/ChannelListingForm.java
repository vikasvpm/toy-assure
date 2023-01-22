package org.learning.assure.model.form;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingForm {

    @CsvBindByPosition(position = 0, required = true)
    private String clientSkuId;

    @CsvBindByPosition(position = 1, required = true)
    private String channelSkuId;
}
