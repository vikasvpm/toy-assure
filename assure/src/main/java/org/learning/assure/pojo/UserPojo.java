package org.learning.assure.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.learning.assure.model.enums.UserType;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class UserPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;

}
