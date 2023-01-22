package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.enums.UserType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserPojo {

    @NotNull(message = "User can not be added without name")
    private String name;
    private UserType userType;
}
