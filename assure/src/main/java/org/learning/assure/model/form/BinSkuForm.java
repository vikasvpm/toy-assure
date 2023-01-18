package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
public class BinSkuForm {

    @NotNull(message = "bin ID can not be null")
    private Long binId;

    @NotBlank(message = "Client SKU ID can not be null")
    private String clientSkuId;

    @NotBlank(message = "Quantity can not be null")
    private Long quantity;
}
