package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.learning.assure.model.enums.InvoiceType;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ChannelPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "channel_generator")
    @TableGenerator(name = "channel_generator", initialValue = 100, allocationSize = 1)
    private Long channelId;
    private String name;
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
}
