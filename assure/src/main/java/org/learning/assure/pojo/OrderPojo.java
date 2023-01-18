package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.learning.assure.model.enums.OrderStatus;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



}
