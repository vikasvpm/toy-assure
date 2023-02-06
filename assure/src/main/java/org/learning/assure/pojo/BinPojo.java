package org.learning.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BinPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "bin_generator")
    @TableGenerator(name = "bin_generator", initialValue = 100, allocationSize = 1)
    private Long binId;
}
