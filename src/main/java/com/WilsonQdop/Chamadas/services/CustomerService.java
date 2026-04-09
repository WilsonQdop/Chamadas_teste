package com.WilsonQdop.Chamadas.services;

import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerResponseDTO;
import com.WilsonQdop.Chamadas.enums.RolesType;
import com.WilsonQdop.Chamadas.exceptions.CustomerNotFoundException;
import com.WilsonQdop.Chamadas.services.impl.CustomerInterface;
import com.WilsonQdop.Chamadas.mappers.CustomerMapper;
import com.WilsonQdop.Chamadas.models.Customer;
import com.WilsonQdop.Chamadas.models.Role;
import com.WilsonQdop.Chamadas.repositories.CustomerRepository;
import com.WilsonQdop.Chamadas.repositories.PersonRepository;
import com.WilsonQdop.Chamadas.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomerService implements CustomerInterface {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper mapper, RoleRepository roleRepository,
                           PersonRepository personRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @Override
    public CustomerResponseDTO save(CustomerRequestDTO dto) {

        Role basicRole = roleRepository.findByName(RolesType.USER.name());
        var userFromDb = personRepository.findByEmail(dto.email());

        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Customer customer = mapper.toDto(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customer.setRoles(Set.of(basicRole));

        return this.mapper.toEntity(this.customerRepository.save(customer));
    }

    @Override
    public Customer findById(UUID id) {
       return this.customerRepository.findById(id).
                orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public CustomerResponseDTO update(CustomerRequestDTO dto, UUID id) {
        Customer customer = this.findById(id);

           this.mapper.updateEntityFromDto(dto, customer);
           Customer update = this.customerRepository.save(customer);

        return this.mapper.toEntity(update);
    }

    @Override
    public void delete(UUID id) {
        Customer customer = this.findById(id);

        this.customerRepository.delete(customer);
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        List<Customer> findAll = customerRepository.findAll();
        return this.mapper.toEntityList(findAll);
    }
}
