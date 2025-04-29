package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.auth.security.model.CustomUserPrincipal;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
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
        Long id = authentication.getPrincipal() instanceof CustomUserPrincipal principal ? principal.id() : null;
        User currentUser = getUserFromRepositoryById(id);
        currentUser.setUserDetail(userDetailService.getUserDetail(currentUser.getUserDetail().getId()));

        return currentUser;
    }

    @Override
    public User getUserById(Long id){
        User user = getUserFromRepositoryById(id);
        UserDetail userDetail = userDetailService.getUserDetail(user.getUserDetail().getId());
        user.setUserDetail(userDetail);
        return user;
    }

    private User getUserFromRepositoryById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
    }
}
