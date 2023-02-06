package com.eazybytes.eazyschool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
////        httpSecurity.authorizeHttpRequests().anyRequest().permitAll()
//        httpSecurity.authorizeHttpRequests().anyRequest().denyAll()
//                .and().formLogin()
//                .and().httpBasic();
//        return httpSecurity.build();
//    }

    @Bean
    SecurityFilterChain customSecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/dashboard").authenticated()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/home").permitAll()
                .requestMatchers("/holidays/**").permitAll()
                .requestMatchers("/courses").denyAll()
                .requestMatchers("/saveMsg").permitAll()
                .requestMatchers("/contact").permitAll()
                .requestMatchers("/about").permitAll()
                .and().formLogin()
                .and().httpBasic();
        return httpSecurity.build();
    }
}
