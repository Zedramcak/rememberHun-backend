package cz.adamzrcek.service;

import cz.adamzrcek.entity.User;
import cz.adamzrcek.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email address " + email + " not found"));
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
    }
}
