package com.WilsonQdop.Chamadas.models.dtos.calleddto;

import com.WilsonQdop.Chamadas.enums.CategoryEnum;
import com.WilsonQdop.Chamadas.enums.StatusEnum;
import com.WilsonQdop.Chamadas.models.Called;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalledResponseDTO(String title, String description, BigDecimal value, boolean payment,
                                CategoryEnum category, StatusEnum status, LocalDateTime createdAt) {
}
