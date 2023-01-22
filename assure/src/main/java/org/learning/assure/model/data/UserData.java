package org.learning.assure.model.data;

import lombok.Getter;
import lombok.Setter;
import org.learning.assure.model.form.UserPojo;

@Getter
@Setter
public class UserData extends UserPojo {
    private Long userId;
}
