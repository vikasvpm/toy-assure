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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "order_generator")
    @TableGenerator(name = "order_generator", initialValue = 10000, allocationSize = 1)
    private Long orderId;
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



}
