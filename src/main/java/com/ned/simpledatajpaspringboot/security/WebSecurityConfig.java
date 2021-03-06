package com.ned.simpledatajpaspringboot.security;

import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

  private MyUserDetailsService userService;

  @Autowired
  public WebSecurityConfig(MyUserDetailsService userService) {
    this.userService = userService;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    // 校验用户
    auth.userDetailsService(userService)
        .passwordEncoder(
            new PasswordEncoder() {
              private Charset charset = Charset.forName("UTF-8");

              // 对密码进行加密
              @Override
              public String encode(CharSequence charSequence) {
                // logger.info(charSequence.toString());
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes(charset));
              }

              // 对密码进行判断匹配
              @Override
              public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes(charset));
                boolean res = s.equals(encode);
                return res;
              }
            });
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable().authorizeRequests().anyRequest().anonymous().and().formLogin().permitAll().and().logout().permitAll();

    // http.authorizeRequests()
    //         //.antMatchers("/", "/h2-console").permitAll()
    //         .anyRequest().anonymous()
    //         .and()
    //     .formLogin()
    //         .permitAll()
    //         .and()
    //     .logout()
    //         .permitAll()
    //         .logoutSuccessUrl("/login");
  }

  // @Bean
  // @Override
  // public UserDetailsService userDetailsService() {
  //     UserDetails user =
  //          User.withDefaultPasswordEncoder()
  //             .username("user")
  //             .password("password")
  //             .roles("USER")
  //             .build();

  //     return new InMemoryUserDetailsManager(user);
  // }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    return this.userService;
  }
}
