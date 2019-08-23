package com.ned.simpledatajpaspringboot.security;

import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.security.domain.Role;
import com.ned.simpledatajpaspringboot.security.domain.RoleRepository;
import com.ned.simpledatajpaspringboot.security.domain.User;
import com.ned.simpledatajpaspringboot.security.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //TODO: it maybe use cache first.

        User exampleUser = new User();
        exampleUser.setUsername(userName);
        Optional<User> userOpt = userRepo.findOne(Example.of(exampleUser));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Role> roles = roleMapper.getRolesByUserId( user.getId() );
            user.setAuthorities( roles );
            return user;
        } else {
            return null;
        }
    }


}