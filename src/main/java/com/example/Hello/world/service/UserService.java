package com.example.Hello.world.service;



import com.example.Hello.world.models.User;
import com.example.Hello.world.repository.TodoRepository;
import com.example.Hello.world.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class UserService {

    @Autowired //dependency injection
    private UserRepository userRepository; //creating instance for user repository


    //CREATE
    public User createUser(User user){
        return userRepository.save(user);

    }


    //READ
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
