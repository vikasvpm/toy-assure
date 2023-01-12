package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.enums.InvoiceType;

@Getter
@Setter
public class ChannelForm {
    private String name;
    private InvoiceType invoiceType;
}
