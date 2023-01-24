package org.learning.assure.model.invoice;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

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

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }

    public void setSellingPricePerUnit(Double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
