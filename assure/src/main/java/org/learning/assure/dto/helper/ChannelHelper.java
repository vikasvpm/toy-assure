package org.learning.assure.dto.helper;

import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.ChannelPojo;

public class ChannelHelper {
    public static ChannelPojo convertChannelFormToChannelPojo(ChannelForm channelForm) {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());
        return channelPojo;
    }
}
