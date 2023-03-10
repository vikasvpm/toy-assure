package org.learning.assure.dto;

import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.ChannelApi;
import org.learning.assure.api.ChannelListingApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ChannelListingHelper;
import org.learning.assure.dto.helper.ThrowExceptionHelper;
import org.learning.commons.exception.ApiException;
import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ChannelPojo;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.util.csvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ChannelListingDto {

    @Autowired
    private UserApi userApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelListingApi channelListingApi;
    public List<ChannelListingPojo> addChannelListing(MultipartFile channelListingCsv, Long clientId, Long channelId) throws ApiException, IOException {
        validateClientIdAndChannelId(clientId, channelId);
        List<ChannelListingForm> channelListingFormList = parseCsv(channelListingCsv);
        List<String> errorList = new ArrayList<>();
        Map<String, Long> map = validateAndMapToGlobalSkuId(channelListingFormList, clientId, errorList);
        List<ChannelListingPojo> channelListingPojoList = ChannelListingHelper.convertChannelListingFormListToChannelListingPojoList(channelListingFormList, clientId, channelId, map);
        return channelListingApi.addChannelListing(channelListingPojoList);
    }
    private List<ChannelListingForm> parseCsv(MultipartFile binSkuCsvFile) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(binSkuCsvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<ChannelListingForm> channelListingFormList = csvParser.parseCSV(binSkuCsvFile.getBytes(), ChannelListingForm.class);
        return channelListingFormList;
    }

    private Map<String, Long> validateAndMapToGlobalSkuId(List<ChannelListingForm> channelListingFormList, Long clientId, List<String> errorList) throws ApiException {
        Map<String, Long> map = new HashMap<>();
        for(ChannelListingForm channelListingForm : channelListingFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, channelListingForm.getClientSkuId());
            if(Objects.isNull(productPojo)) {
                errorList.add("There is no product with Client SKU ID = " + channelListingForm.getClientSkuId() + " for client " + clientId);
            }
            else {
                map.put(channelListingForm.getClientSkuId(), productPojo.getGlobalSkuId());
            }
        }
        ThrowExceptionHelper.throwIfErrors(errorList);
        return map;
    }

    private void validateClientIdAndChannelId(Long clientId, Long channelId) throws ApiException {
        List<String> errors = new ArrayList<>();
        if(Objects.isNull(clientId)) {
            errors.add("Client ID can not be null");
        }
        if(Objects.isNull(channelId)) {
            errors.add("Channel ID can not be null");
        }
        ThrowExceptionHelper.throwIfErrors(errors);
        userApi.invalidClientCheck(clientId);
        ChannelPojo channelPojo = channelApi.getChannelById(channelId);
        if(Objects.isNull(channelPojo)) {
            throw new ApiException("Channel with channelId " + channelId + " does not exist");
        }
    }
}
