package com.WilsonQdop.Chamadas.services.impl;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedCalledDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedRequestDTO;
import com.WilsonQdop.Chamadas.models.Called;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


import java.util.List;

public interface CalledInterface {
    CalledResponseDTO create (CalledRequestDTO dto, JwtAuthenticationToken auth);
    List<CalledResponseDTO> findByStatusOpen();
    void paymentConfirmed(Long calledId, JwtAuthenticationToken auth);
    Called findById (Long id);
    FinalizedCalledDTO finalizedCalled(Long calledId, JwtAuthenticationToken auth, FinalizedRequestDTO dto);

}
