package com.ned.simpledatajpaspringboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .formLogin().permitAll()
            .and()
                .logout().permitAll();

        // http.authorizeRequests()
        //         .antMatchers("/", "/home", "/h2-console").permitAll()
        //         .anyRequest().authenticated()
        //         .and()
        //     .formLogin()
        //         .permitAll()
        //         .and()
        //     .logout()
        //         .permitAll();
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
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
