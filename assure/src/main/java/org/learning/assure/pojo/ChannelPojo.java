package org.learning.assure.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class ChannelPojo extends AbstractPojo {

    @Id
    private Long channelId;
    private String name;

    private InvoiceType invoiceType;
}
