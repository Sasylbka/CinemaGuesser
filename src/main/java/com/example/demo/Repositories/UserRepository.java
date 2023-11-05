package com.example.demo.Repositories;

import java.util.Optional;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);
    @Query("select u from User u where u.username = ?1")
    User findByUsername1(String username);



}

