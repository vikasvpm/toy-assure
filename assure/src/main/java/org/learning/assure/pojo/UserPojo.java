package org.learning.assure.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserPojo {
    @Id
    private long userId;
    private String name;
    private UserType userType;

}
