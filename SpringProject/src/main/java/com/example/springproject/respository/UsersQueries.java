package com.example.springproject.respository;

import com.example.springproject.Model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersQueries extends JpaRepository<Users,Integer> {

    @Query(value="SELECT * FROM users WHERE user_id = :user_id",nativeQuery = true)
    Optional<Users> findById(Integer user_id);

    @Query(value = "SELECT * FROM users WHERE username = :username",nativeQuery = true)
    Optional<Users> findByUsername(String username);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (username,password,role) VALUES (:user,:password,:role)",nativeQuery = true)
    Optional<Integer> createUser(String user, String password, String role);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO admin_users (username,password,role) VALUES (:username,:password,:role)",nativeQuery = true)
    Optional<Integer> createAdmin(String user, String password,String role);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password = :password WHERE username = :username",nativeQuery = true)
    Optional<Integer> updatePassword(@Param("username") String username, @Param("password") String password);
}
