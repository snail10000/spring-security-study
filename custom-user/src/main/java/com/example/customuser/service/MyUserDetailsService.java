package com.example.customuser.service;

import com.example.customuser.dao.UserDao;
import com.example.customuser.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @Date: 2020/6/3 15:31
 */
@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        // 从数据库尝试读取该用户
        User user1 = userDao.findUserByName(user);
        // 用户不存在抛出异常
        if(user1 == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 将数据库的roles 解析为UserDetails 的权限集
        // AuthorityUtils.commaSeparatedStringToAuthorityList() 是Spring Security
        // 提供的,该方法用于将逗号隔开的权限集字符串切割成可以使用的权限列表
        user1.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user1.getRoles()));
        return user1;
    }
}