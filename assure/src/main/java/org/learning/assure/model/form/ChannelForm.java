package org.learning.assure.model.form;

import lombok.Data;
import org.learning.assure.pojo.InvoiceType;

@Data
public class ChannelForm {
    private String name;

    private InvoiceType invoiceType;
}
