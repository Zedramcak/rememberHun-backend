package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;
import cz.adamzrcek.modules.user.entity.UserDetail;
import cz.adamzrcek.modules.user.repository.UserDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {
    private final UserDetailRepository userDetailRepository;

    @Override
    public UserDetail getUserDetail(Long userId) {
        return userDetailRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserDetail for user " + userId + " not found"));
    }
}
