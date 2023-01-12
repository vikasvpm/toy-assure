package org.learning.assure.model.form;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.enums.UserType;

@Getter
@Setter
public class UserForm {
    private String name;
    private UserType userType;
}
