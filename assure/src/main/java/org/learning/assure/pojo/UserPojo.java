package org.learning.assure.pojo;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class UserPojo {
    @Id
    private long userId;
    private String name;
    private UserType userType;


    public UserPojo() {
    }
}
