package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.enums.InvoiceType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChannelForm {

    @NotNull(message = "boom")
    private String name;

    @NotNull(message = "lala")
    private InvoiceType invoiceType;
}
