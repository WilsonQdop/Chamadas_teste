package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.models.Technical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TechnicalRepository extends JpaRepository<Technical, UUID> {
}
