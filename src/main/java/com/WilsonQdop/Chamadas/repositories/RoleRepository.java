package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.enums.RolesType;
import com.WilsonQdop.Chamadas.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    boolean existsByName(String name);
}
