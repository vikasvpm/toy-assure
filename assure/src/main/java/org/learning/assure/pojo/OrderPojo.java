package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class OrderPojo {
    @Id
    private Long orderId;
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    private OrderStatus orderStatus;



}
