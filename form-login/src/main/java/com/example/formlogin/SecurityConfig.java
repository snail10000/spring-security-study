package com.example.formlogin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Date: 2020/5/30 11:13
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 密码加密策略
    @Bean
    PasswordEncoder passwordEncoder(){
        // 返回一个不需要加密的策略
        return NoOpPasswordEncoder.getInstance();
    }

    // 用户方法
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("zhen")
                .password("123456")
                .roles("admin");
                // 配置多个用户使用and 连接
//                .and().withUser()

    }

    // 登录页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 所有请求都需要认证
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login.html").permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }
}