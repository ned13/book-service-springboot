package com.ned.simpledatajpaspringboot.security.domain;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserByUsername(String username);
}