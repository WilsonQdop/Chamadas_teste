package com.WilsonQdop.Chamadas.services;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedCalledDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedRequestDTO;
import com.WilsonQdop.Chamadas.enums.StatusEnum;
import com.WilsonQdop.Chamadas.exceptions.BusinessException;
import com.WilsonQdop.Chamadas.exceptions.CalledIsPaidException;
import com.WilsonQdop.Chamadas.exceptions.CalledNotFoundException;
import com.WilsonQdop.Chamadas.services.impl.CalledInterface;
import com.WilsonQdop.Chamadas.mappers.CalledMapper;
import com.WilsonQdop.Chamadas.models.Called;
import com.WilsonQdop.Chamadas.models.Customer;
import com.WilsonQdop.Chamadas.repositories.CalledRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CalledService implements CalledInterface {
    private final CalledRepository calledRepository;
    private final CustomerService customerService;
    private final CalledMapper mapper;

    public CalledService(CalledRepository calledRepository, CustomerService customerService,
                         CalledMapper mapper) {
        this.calledRepository = calledRepository;
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    public CalledResponseDTO create(CalledRequestDTO dto, JwtAuthenticationToken auth) {
        Customer customer = this.customerService.findById(UUID.fromString(auth.getName()));

        Called called = mapper.toDto(dto);
        switch (dto.category()) {
            case HIGH -> called.setValue(new BigDecimal(200));
            case MEDIA -> called.setValue(new BigDecimal(150));
            case LOW -> called.setValue(new BigDecimal(100));
        }
        called.setCustomer(customer);

        Called saved = this.calledRepository.save(called);

        return this.mapper.toEntity(saved);
    }

    @Override
    public List<CalledResponseDTO> findByStatusOpen() {
        List<Called> CalledOpen = this.calledRepository.findByStatusIn(List.of(StatusEnum.OPEN));

        return CalledOpen.stream().map(c -> new CalledResponseDTO(
                c.getTitle(),c.getDescription(), c.getValue(),c.isPaymentConfirmed(),
                c.getCategory(), c.getStatus(), c.getCreatedAt())).toList();
    }

    @Override
    public void paymentConfirmed(Long calledId, JwtAuthenticationToken auth) {
        Called called = this.findById(calledId);

        UUID customerId = UUID.fromString(auth.getName());

        if (!called.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Pagamento falho! Este chamado não é seu!");
        }

        if (called.isPaymentConfirmed()) {
            throw new CalledIsPaidException();
        }
        called.setPaymentConfirmed(true);
        called.setStatus(StatusEnum.OPEN);

        this.calledRepository.save(called);
    }

    @Override
    public Called findById(Long id) {
        return this.calledRepository.findById(id)
                .orElseThrow(CalledNotFoundException::new);
    }

    @Override
    public FinalizedCalledDTO finalizedCalled(Long calledId, JwtAuthenticationToken auth, FinalizedRequestDTO dto) {
        Called called = this.findById(calledId);

        UUID technicalId = UUID.fromString(auth.getName());

        if(called.getTechnical().getId().equals(technicalId)) {
            throw new RuntimeException("Este chamado não pertence a você!");
        }

        this.isClosed(called);

        called.setStatus(dto.status());
        called.setCreatedAt(LocalDateTime.now());

        return this.mapper.mapToFinalizedResponse(this.calledRepository.save(called));
    }

//    public void validateTechnicalOwnership(Called called, UUID technicalId) {
//        if (called.getTechnical() != null && !technicalId.equals(called.getTechnical().getId())) {
//            throw new TechnicalIsNotOwnerException();
//        }
//    }
    public void isClosed(Called called) {
       if (called.getStatus().equals(StatusEnum.COMPLETED ) || called.getStatus().equals(StatusEnum.CANCELED)) {
           throw new BusinessException();
        }
    }

}
