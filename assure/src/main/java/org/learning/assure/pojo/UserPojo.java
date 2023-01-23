package org.learning.assure.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.learning.assure.model.enums.UserType;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;

}
