package com.valeraci.kuzyasocialnetwork.util.factories;

import com.valeraci.kuzyasocialnetwork.models.Role;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoleFactory {
    public Role createRole(RoleTitle title) {
        Role role = new Role();
        role.setTitle(title);

        switch (title) {
            case ROLE_OWNER -> role.setId(1);
            case ROLE_ADMINISTRATOR -> role.setId(2);
            case ROLE_USER -> role.setId(3);
        }

        return role;
    }
}
