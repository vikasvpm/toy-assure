package org.learning.assure.model.data;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.form.UserForm;

@Getter
@Setter
public class UserData extends UserForm {
    private Long userId;
}
