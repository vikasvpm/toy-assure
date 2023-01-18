package org.learning.assure.dto;

import org.learning.assure.api.ChannelApi;
import org.learning.assure.dto.helper.ChannelHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.ChannelPojo;
import org.learning.assure.model.enums.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelDto {
    @Autowired
    private ChannelApi channelApi;
    public void addChannel(ChannelForm channelForm) throws ApiException {
        createDefaultIfNotExists();
        validateForDuplicateName(channelForm);
        validateForInvoiceType(channelForm);
        ChannelPojo channelPojo = ChannelHelper.convertChannelFormToChannelPojo(channelForm);
        channelApi.addChannel(channelPojo);
    }

    private void validateForInvoiceType(ChannelForm channelForm) throws ApiException {
        if(channelForm.getInvoiceType().equals(InvoiceType.SELF)) {
            throw new ApiException("SELF invoice can only be used by INTERNAL channel");
        }
    }

    private void createDefaultIfNotExists() {
        ChannelPojo channelPojo = channelApi.getDefault();
        if(channelPojo == null) {
            ChannelPojo internal = new ChannelPojo();
            internal.setName("INTERNAL");
            internal.setInvoiceType(InvoiceType.SELF);
            channelApi.addChannel(internal);
        }
    }

    private void validateForDuplicateName(ChannelForm channelForm) throws ApiException {
        ChannelPojo channelPojo= channelApi.getChannelByName(channelForm.getName());
        if(channelPojo != null) {
            throw new ApiException("Channel with name " + channelForm.getName() + "already exists");
        }

    }
}
