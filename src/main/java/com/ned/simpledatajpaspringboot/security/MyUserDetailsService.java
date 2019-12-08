package com.ned.simpledatajpaspringboot.security;

import com.ned.simpledatajpaspringboot.security.domain.Role;
import com.ned.simpledatajpaspringboot.security.domain.RoleRepository;
import com.ned.simpledatajpaspringboot.security.domain.User;
import com.ned.simpledatajpaspringboot.security.domain.UserRepository;

import java.util.Arrays;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

  // @Autowired
  private UserRepository userRepo;

  // @Autowired
  private RoleRepository roleRepo;

  @Autowired
  public MyUserDetailsService(UserRepository userRepo, RoleRepository roleRepo) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    initUserAndRole();
  }

  @Transactional
  public void initUserAndRole() {
    initUser();
    initAdmin();
  }

  private void initUser() {
    Optional<User> modifiedUserOpt =
        this.userRepo
            .findById(1L)
            .map(
                u -> {
                  Optional<Role> userRoleOpt = roleRepo.findById(1L);
                  if (!userRoleOpt.isPresent()) return null;
                  u.setAuthorities(Arrays.asList(userRoleOpt.get()));
                  return u;
                });

    if (modifiedUserOpt.isPresent()) this.userRepo.save(modifiedUserOpt.get());
  }

  private void initAdmin() {
    Optional<User> modifiedAdminOpt =
        this.userRepo
            .findById(2L)
            .map(
                u -> {
                  Optional<Role> userRoleOpt = roleRepo.findById(1L);
                  Optional<Role> adminRoleOpt = roleRepo.findById(2L);
                  if (!userRoleOpt.isPresent() || !adminRoleOpt.isPresent()) return null;
                  u.setAuthorities(Arrays.asList(userRoleOpt.get(), adminRoleOpt.get()));
                  return u;
                });

    if (modifiedAdminOpt.isPresent()) this.userRepo.save(modifiedAdminOpt.get());
  }

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    // TODO: it maybe use cache first.
    return this.userRepo.findUserByUsername(userName).get();

    // return Option.of(userName)
    //     .map(u -> {
    //         this.userRepo.findUserByUsername(userName)
    //         User exampleUser = new User();
    //         exampleUser.setUsername(userName);
    //         Optional<User> userOpt = userRepo.findOne(Example.of(exampleUser));
    //         if (!userOpt.isPresent()) return null;
    //         User retUser = userOpt.get();
    //         Hibernate.initialize(retUser.getAuthorities());
    //         return retUser;
    //     })
    //     .getOrNull();

    // Because hibernate retrieve user with relative role, we don't need to get role again.
    // if (userOpt.isPresent()) {
    //     User user = userOpt.get();
    //     List<Role> roles = roleMapper.getRolesByUserId( user.getId() );
    //     user.setAuthorities( roles );
    //     return user;
    // } else {
    //     return null;
    // }
  }
}
