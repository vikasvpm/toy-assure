package org.learning.assure.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class BinPojo {

    @Id
    private Long binId;
}
