package com.example.springproject.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.springproject.Model.Status;
import com.example.springproject.Model.Users;
import com.example.springproject.respository.UsersQueries;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserServices {

    @Autowired
    private UsersQueries query;
    @Autowired
    private StudentServices studentServices;


    public Optional<Users> getUser(String id){
        return query.findById(Integer.parseInt(id));
    }

    public Integer insertUser(String user, String password) {

        String hash= BCrypt.withDefaults().hashToString(12,password.toCharArray());
        return (query.createUser(user,hash,"USER").get());
    }
    public Optional<Users> getUserDetails(String id){
        return query.findById(Integer.parseInt(id));
    }

    public Optional<Users> findUser(String user){
        return query.findByUsername(user);
    }

    public Integer insertAdmin(String user, String password, String role) {

        String hash= BCrypt.withDefaults().hashToString(12,password.toCharArray());
        return (query.createAdmin(user,hash,"ADMIN").get());
    }
    public Integer updatePassword(Users user) {
        var rs = findUser(user.getUsername());
        if(rs.isPresent()){

            String hash= BCrypt.withDefaults().hashToString(12,user.getPassword().toCharArray());
            return query.updatePassword(user.getUsername(),hash).get();
        }
        return -1;
    }

}
