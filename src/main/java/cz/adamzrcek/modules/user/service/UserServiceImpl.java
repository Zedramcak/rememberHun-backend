package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;
import cz.adamzrcek.modules.user.repository.UserDetailRepository;
import cz.adamzrcek.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    @Override
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email address " + email + " not found"));
        currentUser.setUserDetail(userDetailRepository.findById(currentUser.getUserDetail().getId()).orElseThrow(() -> new ResourceNotFoundException("UserDetail for user " + currentUser.getId() + " not found")));

        return currentUser;
    }

    @Override
    public User getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
        UserDetail userDetail = userDetailRepository.findById(user.getUserDetail().getId()).orElseThrow(() -> new ResourceNotFoundException("UserDetail for user " + user.getId() + " not found"));
        user.setUserDetail(userDetail);
        return user;
    }
}
