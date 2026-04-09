package com.WilsonQdop.Chamadas.config;

import com.WilsonQdop.Chamadas.enums.RolesType;
import com.WilsonQdop.Chamadas.models.Role;
import com.WilsonQdop.Chamadas.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            for (RolesType roleType : RolesType.values()) {

                if (!roleRepository.existsByName(roleType.name())) {

                    Role role = new Role();
                    role.setName(roleType.name());

                    roleRepository.save(role);
                    role.setName(roleType.name());

                    roleRepository.save(role);
                }
            }

            System.out.println("Roles criadas automaticamente!");
        };
    }
}
