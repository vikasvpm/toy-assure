package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class BinPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO: Generation type table
    private Long binId;
}
