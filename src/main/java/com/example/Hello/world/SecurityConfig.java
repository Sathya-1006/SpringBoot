package com.example.Hello.world;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


//telling the prgrm to take this code as configuration
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() throws Exception{
        return new BCryptPasswordEncoder();// a type of pswrd encoder -> hashes into a secure form
    }
    //this filter code use :  can't access the controller directly, anything that comes in should satisfy the filter condition, gets in else reverted back.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception{
        http
                //disabling csrf(Cross-site request forgery) - mainly used during session usage
                .csrf(AbstractHttpConfigurer::disable)
                //allow filter if it passes this cors config - else, don't
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((auth) -> auth
                        //inside auth (both register and login - anyone can access)
                        .requestMatchers("/**").permitAll()

                        .anyRequest().authenticated() // any request incoming should be authenticated.

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                //Authenticating with default username and password.
//              //  .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration=new CorsConfiguration();
        //What to allow
        configuration.setAllowedOrigins(List.of("localhost","*")); //means - req made from localhost origin is allowed ---"*" - means, req can be accepted from any origin
        configuration.setAllowedOrigins(List.of("GET","POST","PUT","DELETE"));//allowed HTTP req
        configuration.setAllowedOrigins(List.of("*"));//Allow all origins

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
        //also add this cors config to securityfilter
    }


}
