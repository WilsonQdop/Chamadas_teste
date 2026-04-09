package com.WilsonQdop.Chamadas.services;

import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryResponseDTO;
import com.WilsonQdop.Chamadas.services.impl.CalledHistoryInterface;
import com.WilsonQdop.Chamadas.mappers.CalledHistoryMapper;
import com.WilsonQdop.Chamadas.models.Called;
import com.WilsonQdop.Chamadas.models.CalledHistory;
import com.WilsonQdop.Chamadas.repositories.CalledHistoryRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CalledHistoryService implements CalledHistoryInterface {
    private final CalledService calledService;
    private final CalledHistoryRepository calledHistoryRepository;
    private final CalledHistoryMapper mapper;

    public CalledHistoryService(CalledService calledService, CalledHistoryRepository calledHistoryRepository,
                                CalledHistoryMapper mapper) {
        this.calledService = calledService;
        this.calledHistoryRepository = calledHistoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CalledHistoryResponseDTO registerObservation(CalledHistoryRequestDTO dto, JwtAuthenticationToken auth) {
        Called called = this.calledService.findById(dto.calledId());

        UUID technicalId = UUID.fromString(auth.getName());

        if(!called.getTechnical().getId().equals(technicalId)) {
            throw new AccessDeniedException("Este chamado não pertence a você!");
        }

        CalledHistory history = new CalledHistory();
        history.setObservation(dto.observation());
        history.setCalled(called);
        history.setTechnical(called.getTechnical());

        return this.mapper.toDto(this.calledHistoryRepository.save(history));
    }

}
