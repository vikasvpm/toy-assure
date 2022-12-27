package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ChannelPojo {

    private Long channelId;
    private String name;

    private InvoiceType invoiceType;
}
