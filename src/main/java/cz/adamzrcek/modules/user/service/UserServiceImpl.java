package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.privacy.annotation.LogDataAccess;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
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
    private final UserDetailService userDetailService;

    @Override
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User currentUser = getCurrentUser(email);
        currentUser.setUserDetail(userDetailService.getUserDetail(currentUser.getUserDetail().getId()));

        return currentUser;
    }

    @LogDataAccess(entity = "user")
    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email address " + email + " not found"));
    }

    @Override
    public User getUserById(Long id){
        User user = getUserFromRepository(id);
        UserDetail userDetail = userDetailService.getUserDetail(user.getUserDetail().getId());
        user.setUserDetail(userDetail);
        return user;
    }

    @LogDataAccess(entity = "user")
    private User getUserFromRepository(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
    }
}
