package az.library.management.mapper;

import az.library.management.dao.entity.User;
import az.library.management.model.dto.user.NewUserDTO;
import az.library.management.model.dto.user.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T00:37:09+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public UserDTO mapUserToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );
        userDTO.setEmail( user.getEmail() );

        return userDTO;
    }

    @Override
    public User mapNewUserToUser(NewUserDTO newUserDTO) {
        if ( newUserDTO == null ) {
            return null;
        }

        User user = new User();

        user.setName( newUserDTO.getName() );
        user.setEmail( newUserDTO.getEmail() );
        user.setPassword( newUserDTO.getPassword() );

        return user;
    }
}
