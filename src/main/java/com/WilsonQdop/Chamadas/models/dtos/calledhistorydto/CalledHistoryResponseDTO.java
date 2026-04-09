package com.WilsonQdop.Chamadas.models.dtos.calledhistorydto;

import java.time.LocalDateTime;

public record CalledHistoryResponseDTO (String observation, String technicalName, LocalDateTime createdAt){
}
