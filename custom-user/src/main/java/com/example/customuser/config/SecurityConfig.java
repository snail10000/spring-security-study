package com.example.customuser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

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
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("zhen")
//                .password("123")
//                .roles("admin")
//                .and()
//                .withUser("江南一点雨")
//                .password("123")
//                .roles("user");
//    }
//    @Autowired
//    DataSource dataSource;

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
//        manager.setDataSource(dataSource);
//        if(!manager.userExists("javaboy")){
//            manager.createUser(User.withUsername("javaboy").password("123").roles("admin").build());
//        }
//        if(!manager.userExists("江南一点雨")){
//            manager.createUser(User.withUsername("江南一点雨").password("123").roles("user").build());
//        }
//        return manager;
//    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return roleHierarchy;
    }

    // 登录页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 所有请求都需要认证
        http.authorizeRequests()
                // 从上往下
                .antMatchers("/admin/**").hasRole("admin")// admin可以访问
                .antMatchers("/user/**").hasRole("user") // user可以访问
                .anyRequest().authenticated()// 认证可以访问
                .and()
                .formLogin()
                .loginProcessingUrl("/doLogin")
//              处理全后端分离
               .successHandler((req,res,authentication)->{
                   res.setContentType("application/json;charset=utf-8");
                   try(PrintWriter out = res.getWriter()) {
                       // 判断异常类型进行提示
                       out.write(new ObjectMapper().writeValueAsString(authentication.getPrincipal()));
                       out.flush();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               })
                .failureHandler((req,res,exception)->{
                    res.setContentType("application/json;charset=utf-8");
                    try(PrintWriter out = res.getWriter()) {
                        out.write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req,res,exception)->{
                    res.setContentType("application/json;charset=utf-8");
                    try(PrintWriter out = res.getWriter()) {
                        out.write(new ObjectMapper().writeValueAsString("注销成功"));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((req,res,exception)->{
                    res.setContentType("application/json;charset=utf-8");
                    try(PrintWriter out = res.getWriter()) {
                        out.write(new ObjectMapper().writeValueAsString("尚未登录,请登录"));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


}