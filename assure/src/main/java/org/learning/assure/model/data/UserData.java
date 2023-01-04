package org.learning.assure.model.data;

import lombok.Data;
import org.learning.assure.model.form.UserForm;

@Data
public class UserData extends UserForm {
    private Long userId;
}
