package az.library.management.mapper;

import az.library.management.dao.entity.User;
import az.library.management.model.dto.user.NewUserDTO;
import az.library.management.model.dto.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserDTO mapUserToUserDto(User user);

    public abstract User mapNewUserToUser(NewUserDTO newUserDTO);


}
