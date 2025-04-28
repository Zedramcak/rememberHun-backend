package cz.adamzrcek.modules.shared.startup;

import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
import cz.adamzrcek.repository.RoleRepository;
import cz.adamzrcek.modules.user.repository.UserDetailRepository;
import cz.adamzrcek.modules.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
@RequiredArgsConstructor
@Log4j2
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDetailRepository userDetailRepository;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${admin.role:ADMIN}")
    private String adminRole;

    @PostConstruct
    public void init() {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .username(adminUsername)
                    .role(roleRepository.findByName(adminRole))
                    .userDetail(createAdminUserDetail())
                    .build();
            userRepository.save(admin);
            log.info("✅ Default admin account created (email: {})", adminEmail);
        } else {
            log.info("👀 Admin account already exists. Skipping creation.");
        }
    }

    private UserDetail createAdminUserDetail() {
        var adminUserDetail = UserDetail.builder().firstName("admin").lastName("admin").birthDate(LocalDate.of(1991, Month.DECEMBER, 17)).build();
        return userDetailRepository.save(adminUserDetail);
    }
}
