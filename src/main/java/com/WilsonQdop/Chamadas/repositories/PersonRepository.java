package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByEmail(String name);
}
