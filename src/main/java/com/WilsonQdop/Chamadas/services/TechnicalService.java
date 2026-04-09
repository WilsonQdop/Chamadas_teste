package com.WilsonQdop.Chamadas.services;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalResponseDTO;
import com.WilsonQdop.Chamadas.enums.RolesType;
import com.WilsonQdop.Chamadas.enums.StatusEnum;
import com.WilsonQdop.Chamadas.exceptions.CalledAssignmentException;
import com.WilsonQdop.Chamadas.exceptions.TechnicalNotFoundException;
import com.WilsonQdop.Chamadas.models.Person;
import com.WilsonQdop.Chamadas.models.Role;
import com.WilsonQdop.Chamadas.repositories.PersonRepository;
import com.WilsonQdop.Chamadas.repositories.RoleRepository;
import com.WilsonQdop.Chamadas.services.impl.TechnicalInterface;
import com.WilsonQdop.Chamadas.mappers.CalledMapper;
import com.WilsonQdop.Chamadas.mappers.TechnicalMapper;
import com.WilsonQdop.Chamadas.models.Called;
import com.WilsonQdop.Chamadas.models.Technical;
import com.WilsonQdop.Chamadas.repositories.CalledRepository;
import com.WilsonQdop.Chamadas.repositories.TechnicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TechnicalService implements TechnicalInterface {
    private final TechnicalRepository technicalRepository;
    private final RoleRepository roleRepository;
    private final CalledRepository calledRepository;
    private final PersonRepository personRepository;
    private final CalledService calledService;
    private final TechnicalMapper mapper;
    private final CalledMapper mapperCalled;
    private final BCryptPasswordEncoder passwordEncoder;

    public TechnicalService(TechnicalRepository technicalRepository, RoleRepository roleRepository, CalledRepository calledRepository, PersonRepository personRepository,
                            CalledService calledService, TechnicalMapper mapper,
                            CalledMapper mapperCalled, BCryptPasswordEncoder passwordEncoder) {
        this.technicalRepository = technicalRepository;
        this.roleRepository = roleRepository;
        this.calledRepository = calledRepository;
        this.personRepository = personRepository;
        this.calledService = calledService;
        this.mapper = mapper;
        this.mapperCalled = mapperCalled;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TechnicalResponseDTO save(TechnicalRequestDTO dto) {

        Role basicRole = roleRepository.findByName(RolesType.TECH.name());
        Optional<Person> techFromDb = personRepository.findByEmail(dto.email());

        if(techFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Technical technical = this.mapper.toDto(dto);
        technical.setPassword(passwordEncoder.encode(dto.password()));
        technical.setRoles(Set.of(basicRole));

        Technical saved = this.technicalRepository.save(technical);
        return this.mapper.toEntity(saved);
    }

    @Override
    public Technical find(UUID id) {
        return this.technicalRepository.findById(id)
                .orElseThrow(TechnicalNotFoundException::new);
    }

    @Override
    public TechnicalResponseDTO update(TechnicalRequestDTO dto, UUID id) {
        Technical technical = this.find(id);

        this.mapper.updateToEntityFromDto(dto, technical);
        Technical updated = this.technicalRepository.save(technical);
        return this.mapper.toEntity(updated);
    }

    @Override
    public void delete(UUID id) {
        Technical technical = this.find(id);
        this.technicalRepository.delete(technical);
    }

    @Override
    public CalledResponseDTO assignToCalled(JwtAuthenticationToken auth, Long calledId) {
        Called called = this.calledService.findById(calledId);
        Technical technical = this.find(UUID.fromString(auth.getName()));

        if (!called.isPaymentConfirmed() || !called.getStatus().equals(StatusEnum.OPEN) || called.getTechnical() != null) {
            throw new CalledAssignmentException();
        }

        called.setTechnical(technical);
        called.setStatus(StatusEnum.ASSIGNED);

        Called assignCalled = this.calledRepository.save(called);
        return this.mapperCalled.toEntity(assignCalled);
    }
}
