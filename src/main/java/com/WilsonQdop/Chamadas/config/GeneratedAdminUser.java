package com.WilsonQdop.Chamadas.config;

import com.WilsonQdop.Chamadas.enums.RolesType;
import com.WilsonQdop.Chamadas.models.AdminUser;
import com.WilsonQdop.Chamadas.models.Person;
import com.WilsonQdop.Chamadas.models.Role;
import com.WilsonQdop.Chamadas.repositories.PersonRepository;
import com.WilsonQdop.Chamadas.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

// @Configuration
@Component
public class GeneratedAdminUser implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public GeneratedAdminUser(RoleRepository roleRepository, PersonRepository personRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role roleAdmin = roleRepository.findByName(RolesType.ADM.name());

        Optional<Person> userAdmin = personRepository.findByEmail("admin");

        if (personRepository.count() == 0) {
            AdminUser admin = new AdminUser();

            admin.setEmail("Admin@Admin.com");
            admin.setName("ADM");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.getRoles().add(roleAdmin);

            personRepository.save(admin);
            System.out.println("Usuário admin criado com sucesso!");
        } else {
            System.out.println("Usuários já cadastrados. Nenhuma ação realizada.");
        }
    }
}
