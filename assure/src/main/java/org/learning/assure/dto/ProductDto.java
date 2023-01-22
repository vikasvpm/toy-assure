package org.learning.assure.dto;


import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ProductHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.util.csvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductDto {

    @Autowired
    private ProductApi productApi;
    @Autowired
    private UserApi userApi;


    public void deleteProduct(Long id) {
        productApi.deleteProduct(id);
    }

    public List<ProductPojo> getAllProducts() {
        return productApi.getAllProducts();
    }

    public void addProducts(MultipartFile productCsvFile, Long clientId) throws ApiException, IOException {
        validateForClientId(clientId);
        List<ProductForm> productFormList = parseCSV(productCsvFile);
        validateForDuplicate(productFormList, clientId);
        List<ProductPojo> productPojoList = ProductHelper.convertListOfProductFormToListOfProductPojo(productFormList, clientId);
        productApi.addProducts(productPojoList);
    }

    private List<ProductForm> parseCSV(MultipartFile csvFile) throws IOException, ApiException {
        if (!FilenameUtils.isExtension(csvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<ProductForm> productFormList = null;
        productFormList = csvParser.parseCSV(csvFile.getBytes(), ProductForm.class);
        return productFormList;
    }

    private void validateForDuplicate(List<ProductForm> productFormList, Long clientId) throws ApiException {
        Set<String> clientSkuIdSet = new HashSet<>();
        for(ProductForm productForm : productFormList) {
            if(clientSkuIdSet.contains(productForm.getClientSkuId())) {
                throw new ApiException("Duplicate Client SKU " + productForm.getClientSkuId() + " in the upload");
            }
            clientSkuIdSet.add(productForm.getClientSkuId());
        }
    }
    private void validateForClientId(Long clientId) throws ApiException {
        userApi.invalidClientCheck(clientId);
    }

}
