package com.valeraci.kuzyasocialnetwork.util.mappers;

import com.valeraci.kuzyasocialnetwork.dto.users.RegistrationDto;
import com.valeraci.kuzyasocialnetwork.models.User;
import com.valeraci.kuzyasocialnetwork.models.UserCredential;
import com.valeraci.kuzyasocialnetwork.models.enums.RoleTitle;
import com.valeraci.kuzyasocialnetwork.util.factories.SimpleFamilyStatusFactory;
import com.valeraci.kuzyasocialnetwork.util.factories.SimpleRoleFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserCredentialMapper {
    private final ModelMapper mapper;
    private final SimpleFamilyStatusFactory simpleFamilyStatusFactory;
    private final SimpleRoleFactory simpleRoleFactory;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(RegistrationDto.class, UserCredential.class)
                .addMappings(m -> m.skip(UserCredential::setUser))
                .addMappings(m -> m.skip(UserCredential::setLocks))
                .addMappings(m -> m.skip(UserCredential::setRoles))
                .setPostConverter(
                        registrationDtoToUserCredentialConverter()
                );
    }

    private Converter<RegistrationDto, UserCredential> registrationDtoToUserCredentialConverter() {
        return context -> {
            RegistrationDto source = context.getSource();
            UserCredential destination = context.getDestination();
            mapLoginRequestSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapLoginRequestSpecificFields(RegistrationDto source, UserCredential destination) {
        User user = new User();
        user.setLastName(source.getLastName());
        user.setFirstName(source.getFirstName());
        user.setFamilyStatus(simpleFamilyStatusFactory.createFamilyStatus(source.getFamilyStatusTitle()));

        destination.setRoles(Set.of(simpleRoleFactory.createRole(RoleTitle.ROLE_USER)));
        destination.setUser(user);
    }

    public UserCredential toEntity(RegistrationDto registrationDto) {
        return mapper.map(registrationDto, UserCredential.class);
    }

}
