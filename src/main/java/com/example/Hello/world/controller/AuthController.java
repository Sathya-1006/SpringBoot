package com.example.Hello.world.controller;

import com.example.Hello.world.models.User;
import com.example.Hello.world.repository.UserRepository;
import com.example.Hello.world.service.UserService;
import com.example.Hello.world.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> body){
        //Getting email & password from body
        String email=body.get("email");
        String password=passwordEncoder.encode(body.get("password"));
    // checking - is it a valid email address? is it in DB?
        if(userRepository.findByEmail(email).isPresent())
        {
            //If already the user exists, create a conflict
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);

        }
        // if user not found, create the user and display success msg
        userService.createUser(User.builder().email(email).password(password).build());
        return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);

    }

    @PostMapping("/login")
    //ResponseEntity<?> =>  states that, its a response entity, it can be of any data type
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body){
        String email=body.get("email");
        String password=body.get("password");
        //String password=passwordEncoder.encode(body.get("password")); // to encrypt password on DB

         var userOptional = userRepository.findByEmail(email);
         //if no user = unauthorized /  not found
         if(userOptional.isEmpty()){
             return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
         }
         User user = userOptional.get();
         //if Pswrd in DB (hashed password) != pswrd entered by user
         if(!passwordEncoder.matches(password, user.getPassword())){
             return new ResponseEntity<>("Invalid User", HttpStatus.UNAUTHORIZED);
         }
         //If matches , generate a token
        String token = jwtUtil.generateToken(email);//gives token using email
        return ResponseEntity.ok(Map.of("token",token));
    }

}
