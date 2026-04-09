package com.WilsonQdop.Chamadas.repositories;

import com.WilsonQdop.Chamadas.enums.StatusEnum;
import com.WilsonQdop.Chamadas.models.Called;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalledRepository extends JpaRepository<Called, Long> {
    List<Called> findByStatusIn(List<StatusEnum> status);

}
