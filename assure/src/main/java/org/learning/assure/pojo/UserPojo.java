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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_generator")
    @TableGenerator(name = "user_generator", initialValue = 100, allocationSize = 1)
    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType userType;

}
