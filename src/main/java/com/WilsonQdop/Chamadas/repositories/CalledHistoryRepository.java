package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.models.CalledHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalledHistoryRepository extends JpaRepository<CalledHistory, Long> {
}
