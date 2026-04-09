package com.WilsonQdop.Chamadas.services.impl;

import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryResponseDTO;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface CalledHistoryInterface {
    CalledHistoryResponseDTO registerObservation(CalledHistoryRequestDTO dto, JwtAuthenticationToken auth);
}
