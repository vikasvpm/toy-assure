package org.learning.assure.model.form;

import lombok.Getter;
import org.learning.assure.model.enums.UserType;

@Getter
public class UserForm {
    private String name;
    private UserType userType;
}
