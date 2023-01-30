package org.learning.assure.model.form;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BinSkuForm {

    @CsvBindByPosition(position = 0, required = true)
    private Long binId;

    @CsvBindByPosition(position = 1, required = true)
    private String clientSkuId;

    @CsvBindByPosition(position = 2, required = true)
    private Long quantity;
}
