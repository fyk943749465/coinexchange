package com.bjsxt.mappers;

import com.bjsxt.domain.User;
import com.bjsxt.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用来做对象的映射转换，将entity 转换为 dto
 * 或者 将 dto转换为 entiy
 */
@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    // 获取实例
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    /**
     * entity convert to dto
     * @param source
     * @return
     */
    UserDto convert2Dto(User source);

    /**
     * dto to entity
     * @param userDto
     * @return
     */
    User convert2Entity(UserDto userDto);

    /**
     * entity convert to dto
     * @param source
     * @return
     */
    List<UserDto> convert2Dto(List<User> source);

    /**
     * dto to entity
     * @param userDto
     * @return
     */
    List<User> convert2Entity(List<UserDto> userDto);

}
