package cz.adamzrcek.startup;

import cz.adamzrcek.entity.User;
import cz.adamzrcek.repository.RoleRepository;
import cz.adamzrcek.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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
                    .build();
            userRepository.save(admin);
            log.info("✅ Default admin account created (email: {})", adminEmail);
        } else {
            log.info("👀 Admin account already exists. Skipping creation.");
        }
    }
}
