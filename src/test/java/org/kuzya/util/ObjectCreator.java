package org.kuzya.util;

import org.kuzya.models.FamilyStatus;
import org.kuzya.models.Role;
import org.kuzya.models.User;
import org.kuzya.models.enums.FamilyStatusTitle;
import org.kuzya.models.enums.RoleTitle;

import java.util.HashSet;
import java.util.Set;

public class ObjectCreator {

    public static Role createUserRole(){
        Role role = new Role();
        role.setId(3);
        role.setTitle(RoleTitle.ROLE_USER);

        return role;
    }

    public static FamilyStatus createFamilyStatusSingle(){
        FamilyStatus familyStatus = new FamilyStatus();
        familyStatus.setId(1);
        familyStatus.setTitle(FamilyStatusTitle.SINGLE);

        return familyStatus;
    }
    public static User createUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Set.of(createUserRole()));
        user.setFamilyStatus(createFamilyStatusSingle());
        user.setMediaFiles(new HashSet<>());
        user.setActive(true);

        return user;
    }
}