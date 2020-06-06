package com.example.customuser.dao;

import com.example.customuser.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    @Select("select * from users where username = #{username}")
    User findUserByName(@Param("username") String username);
}
