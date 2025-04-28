package cz.adamzrcek.modules.user.service;

import cz.adamzrcek.modules.user.entity.UserDetail;

public interface UserDetailService {
    UserDetail getUserDetail(Long userId);
}
