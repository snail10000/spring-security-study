package com.example.formlogin2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("user")
                .passwordParameter("pwd")
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

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }
}