package org.learning.assure.api.flow;

import org.apache.fop.apps.*;
import org.learning.assure.api.InventoryApi;
import org.learning.assure.api.OrderApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.model.invoice.InvoiceData;
import org.learning.assure.model.invoice.InvoiceItemData;
import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.sql.Timestamp;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceFlowApi {
    private FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    private String xslTemplateName = "xslTemplate.xsl";

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private OrderApi orderApi;

    public byte[] generateInvoice(Long orderId) throws ApiException {
        OrderPojo orderPojo = orderApi.getOrderByOrderId(orderId);
        List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderId);
        try {
            File xslFile = new File(Thread.currentThread().getContextClassLoader().getResource(xslTemplateName).toURI());
            String xmlInput = getXmlString(orderItemPojoList, orderPojo);
            return createInvoicePdf(xmlInput, xslFile);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("Issue while generating XML for orders");
        }
        finally {
            orderPojo.setOrderStatus(OrderStatus.FULFILLED);
            for(OrderItemPojo orderItemPojo : orderItemPojoList) {
                orderItemPojo.setFulfilledQuantity(orderItemPojo.getAllocatedQuantity());
                orderItemPojo.setAllocatedQuantity(0L);
                InventoryPojo inventoryPojo = inventoryApi.getByGlobalSkuId(orderItemPojo.getGlobalSkuId());
                inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() - orderItemPojo.getFulfilledQuantity());
                inventoryPojo.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity() + orderItemPojo.getFulfilledQuantity());
            }
        }
    }

    private String getXmlString(List<OrderItemPojo> orderItemPojoList, OrderPojo orderPojo) throws ApiException {
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceItemData(convertOrderItemListToInvoiceItemDataList(orderItemPojoList));
        invoiceData.setInvoiceNumber(orderPojo.getOrderId());
        invoiceData.setInvoiceDate(new Timestamp(System.currentTimeMillis()).toString());
        invoiceData.setClientId(orderPojo.getClientId().toString());
        invoiceData.setInvoiceTotal(orderItemPojoList.stream().mapToDouble(orderItemPojo -> orderItemPojo.getAllocatedQuantity() * orderItemPojo.getSellingPricePerUnit()).sum());
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(InvoiceData.class);
            Marshaller marshallerObj = context.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(invoiceData, stringWriter);
        }
        catch (Exception e) {
            throw new ApiException("Issue while generating XML for orders");
        }
        return stringWriter.toString();
    }


    private byte[] createInvoicePdf(String xmlInput, File xslFile) throws FOPException, TransformerException {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
        Source source = new StreamSource(new StringReader(xmlInput));
        Result result = new SAXResult(fop.getDefaultHandler());
        transformer.transform(source,result);
        return outputStream.toByteArray();

    }

    private List<InvoiceItemData> convertOrderItemListToInvoiceItemDataList(List<OrderItemPojo> orderItemPojoList) {
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            ProductPojo productPojo = productApi.getProductByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            invoiceItemData.setProductName(productPojo.getName());
            invoiceItemData.setClientSkuId(productPojo.getClientSkuId());
            invoiceItemData.setQuantity(orderItemPojo.getAllocatedQuantity());
            invoiceItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
            invoiceItemData.setAmount(orderItemPojo.getSellingPricePerUnit() * orderItemPojo.getAllocatedQuantity());
            invoiceItemDataList.add(invoiceItemData);
        }
        return invoiceItemDataList;
    }
}
