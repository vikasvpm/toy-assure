package org.learning.assure.dto;

import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.*;
import org.learning.assure.api.flow.BinWiseInventoryFlowApi;
import org.learning.assure.dto.helper.BinSkuHelper;
import org.learning.assure.dto.helper.InventoryHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.util.csvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class BinSkuDto {
    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private BinWiseInventoryFlowApi binWiseInventoryFlowApi;

    public List<BinSkuPojo> addBinSkus(MultipartFile binSkuCsvFile, Long clientId) throws ApiException, IOException {
        validateClient(clientId);
        List<BinSkuForm>  binSkuFormList = parseCsv(binSkuCsvFile);
        validateForBinId(binSkuFormList);
        validateForClientSkuId(binSkuFormList, clientId);
        validateForQuantity(binSkuFormList);
        Map<String, Long> map = mapToGlobalSkuId(binSkuFormList, clientId);
        List<BinSkuPojo> binSkuPojoList = BinSkuHelper.convertBinSkuFormListToBinSkuPojoList(binSkuFormList, clientId, map);
        List<InventoryPojo> inventoryPojoList = InventoryHelper.convertToInventoryPojoList(binSkuFormList, map);
        return binWiseInventoryFlowApi.addBinWiseInventory(binSkuPojoList, inventoryPojoList);
    }

    private List<BinSkuForm> parseCsv(MultipartFile binSkuCsvFile) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(binSkuCsvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<BinSkuForm> binSkuFormList = csvParser.parseCSV(binSkuCsvFile.getBytes(), BinSkuForm.class);
        return binSkuFormList;
    }

    private void validateClient(Long clientId) throws ApiException {
        userApi.invalidClientCheck(clientId);
    }

    private void validateForQuantity(List<BinSkuForm> binSkuFormList) throws ApiException {
        for(BinSkuForm binSkuForm : binSkuFormList) {
            if(binSkuForm.getQuantity() < 1) {
                throw new ApiException("Quantity of item can not be 0 or negative, Found such value for Product with client SKU ID = " + binSkuForm.getClientSkuId());
            }
        }
    }

    private Map<String, Long> mapToGlobalSkuId(List<BinSkuForm> binSkuFormList, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(BinSkuForm binSkuForm : binSkuFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, binSkuForm.getClientSkuId());
            map.put(binSkuForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }


    private void validateForBinId(List<BinSkuForm> binSkuFormList) throws ApiException {
        for(BinSkuForm binSkuForm : binSkuFormList) {
            if(Objects.isNull(binApi.getBinByBinId(binSkuForm.getBinId()))) {
                throw new ApiException("Bin with id " + binSkuForm.getBinId() + " does not exist");
            }
        }
    }

    private void validateForClientSkuId(List<BinSkuForm> binSkuFormList, Long clientId) {
        Set<String> clientSkuIdSet = new HashSet<>();
        binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .forEach(clientSkuId -> {
                    if(Objects.isNull(productApi.getProductByClientIdAndClientSkuId(clientId,clientSkuId))) {
                        try {
                            throw new ApiException("Product with Client SKU ID " + clientSkuId + " does not exist for Client " + clientId + " in the system");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(clientSkuIdSet.contains(clientSkuId)) {
                        try {
                            throw new ApiException("Duplicate Client SKU ID " + clientSkuId + " present in the upload");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clientSkuIdSet.add(clientSkuId);
                    
                });
    }


}
