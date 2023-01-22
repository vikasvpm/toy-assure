package org.learning.assure.dto;

import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.ChannelApi;
import org.learning.assure.api.ChannelListingApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ChannelListingHelper;
import org.learning.assure.exception.ApiException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public void addChannelListing(MultipartFile channelListingCsv, Long clientId, Long channelId) throws ApiException, IOException {
        validateClientIdAndChannelId(clientId, channelId);
        List<ChannelListingForm> channelListingFormList = parseCsv(channelListingCsv);
        Map<String, Long> map = validateAndMapToGlobalSkuId(channelListingFormList, clientId);
        List<ChannelListingPojo> channelListingPojoList = ChannelListingHelper.convertChannelListingFormListToChannelListingPojoList(channelListingFormList, clientId, channelId, map);
        channelListingApi.addChannelListing(channelListingPojoList);
    }
    private List<ChannelListingForm> parseCsv(MultipartFile binSkuCsvFile) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(binSkuCsvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<ChannelListingForm> channelListingFormList = csvParser.parseCSV(binSkuCsvFile.getBytes(), ChannelListingForm.class);
        return channelListingFormList;
    }

    private Map<String, Long> validateAndMapToGlobalSkuId(List<ChannelListingForm> channelListingFormList, Long clientId) throws ApiException {
        Map<String, Long> map = new HashMap<>();
        for(ChannelListingForm channelListingForm : channelListingFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, channelListingForm.getClientSkuId());
            if(Objects.isNull(productPojo)) {
                throw new ApiException("There is no product with Client SKU ID = " + channelListingForm.getChannelSkuId() + " for client " + clientId);
            }
            map.put(channelListingForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }

    private void validateClientIdAndChannelId(Long clientId, Long channelId) throws ApiException {
        userApi.invalidClientCheck(clientId);
        ChannelPojo channelPojo = channelApi.getChannelById(channelId);
        if(Objects.isNull(channelPojo)) {
            throw new ApiException("Channel with channelId " + channelId + " does not exist");
        }
    }
}
