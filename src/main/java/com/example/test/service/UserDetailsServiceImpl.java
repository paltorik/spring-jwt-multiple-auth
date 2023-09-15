package com.example.test.service;

import com.example.test.repository.UserRepository;
import com.example.test.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    @Override
  @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    } 

    public UserDetails LoadUserByPhone(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));

        return UserDetailsImpl.build(user);
    } 

    public UserDetails LoadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));

        return UserDetailsImpl.build(user);
    } 
    public UserDetails LoadUserById(Long userId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));

        return UserDetailsImpl.build(user);
    }
    
}
