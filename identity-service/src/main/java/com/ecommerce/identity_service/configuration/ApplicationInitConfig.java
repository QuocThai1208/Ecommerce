package com.ecommerce.identity_service.configuration;

import com.ecommerce.identity_service.entity.Role;
import com.ecommerce.identity_service.entity.User;
import com.ecommerce.identity_service.repository.RoleRepository;
import com.ecommerce.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        return args -> {
            log.info("Initializing default roles and admin user...");
            // Initialize default admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Create USER role if it doesn't exist
                roleRepository.findById("USER")
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name("USER")
                                .description("Default user role with limited permissions")
                                .build())
                        );

                // Create ADMIN role if it doesn't exist
                Role adminRole = roleRepository.findById("ADMIN")
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name("ADMIN")
                                .description("Administrator role with full permissions")
                                .build())
                        );

                var roles = Set.of(adminRole);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin, please changde it.");
            }
        };
    }
}