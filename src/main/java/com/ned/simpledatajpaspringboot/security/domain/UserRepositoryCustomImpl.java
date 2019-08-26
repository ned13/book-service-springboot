package com.ned.simpledatajpaspringboot.security.domain;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager entityManager;


    public Optional<User> findUserByUsername(String username) {
        List<User> users = entityManager.createQuery(
            "select u " +
            "from User u " +
            "join fetch u.authorities " +
            "where u.username = :username"
            , User.class)
            .setParameter("username", username)
            .getResultList();

        return users.size() > 0 ? Optional.of(users.get(0)) : Optional.empty();
    }
}