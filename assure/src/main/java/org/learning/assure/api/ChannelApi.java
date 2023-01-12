package org.learning.assure.api;

import org.learning.assure.dao.ChannelDao;
import org.learning.assure.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChannelApi {

    @Autowired
    private ChannelDao channelDao;

    public void addChannel(ChannelPojo channelPojo) {
        channelDao.addChannel(channelPojo);
    }

    public ChannelPojo getChannelByName(String name) {
        return channelDao.getChannelByName(name);
    }

    public ChannelPojo getChannelById(Long channelId) {
        return channelDao.getChannelById(channelId);
    }

    public ChannelPojo getDefault() {
        return channelDao.getDefault();
    }
}
