package org.learning.assure.model.form;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
public class BinSkuForm {

    @NotNull(message = "Bin ID can not be null")
    @CsvBindByPosition(position = 0, required = true)
    private Long binId;

    @NotBlank(message = "Client SKU ID can not be null")
    @CsvBindByPosition(position = 1, required = true)
    private String clientSkuId;

    @NotBlank(message = "Quantity can not be null")
    @CsvBindByPosition(position = 2, required = true)
    private Long quantity;
}
