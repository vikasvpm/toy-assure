package org.learning.assure.dto;

import org.learning.assure.api.ChannelApi;
import org.learning.assure.dto.helper.ChannelHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.ChannelPojo;
import org.learning.assure.model.enums.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ChannelDto {
    @Autowired
    private ChannelApi channelApi;
    public ChannelPojo addChannel(ChannelForm channelForm) throws ApiException {
        createDefaultIfNotExists();
        validateForDuplicateName(channelForm);
        ChannelPojo channelPojo = ChannelHelper.convertChannelFormToChannelPojo(channelForm);
        return channelApi.addChannel(channelPojo);
    }

    private void createDefaultIfNotExists() {
        ChannelPojo channelPojo = channelApi.getDefault();
        if(Objects.isNull(channelPojo)) {
            ChannelPojo internal = new ChannelPojo();
            internal.setName("INTERNAL");
            internal.setInvoiceType(InvoiceType.SELF);
            channelApi.addChannel(internal);
        }
    }

    private void validateForDuplicateName(ChannelForm channelForm) throws ApiException {
        ChannelPojo channelPojo= channelApi.getChannelByName(channelForm.getName());
        if(!Objects.isNull(channelPojo)) {
            throw new ApiException("Channel with name = " + channelForm.getName() + " already exists");
        }

    }
}
