package co.com.myproject.api.mapper;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(RegisterUserDto dto);
    RegisterUserDto toDto(User entity);
}
