package com.WilsonQdop.Chamadas.services.impl;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalResponseDTO;
import com.WilsonQdop.Chamadas.models.Technical;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public interface TechnicalInterface {
    TechnicalResponseDTO save(TechnicalRequestDTO dto);
    Technical find(UUID id);
    TechnicalResponseDTO update(TechnicalRequestDTO dto, UUID id);
    void delete(UUID id);
    CalledResponseDTO assignToCalled(JwtAuthenticationToken auth, Long calledId);
}
