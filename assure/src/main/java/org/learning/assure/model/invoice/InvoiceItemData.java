package org.learning.assure.model.invoice;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceItemData {
    @XmlElement(name = "client-sku-id")
    private String clientSkuId;

    @XmlElement(name = "product-name")
    private String productName;

    @XmlElement(name = "quantity")
    private Long quantity;

    @XmlElement(name = "selling-price-per-unit")
    private Double sellingPricePerUnit;

    @XmlElement(name = "amount")
    private Double amount;

}
