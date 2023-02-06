package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.enums.InvoiceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChannelForm {

    private String name;

    private InvoiceType invoiceType;
}
