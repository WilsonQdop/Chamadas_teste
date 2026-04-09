package com.WilsonQdop.Chamadas.services.impl;

import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerResponseDTO;
import com.WilsonQdop.Chamadas.models.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerInterface {
    CustomerResponseDTO save (CustomerRequestDTO dto);
    Customer findById(UUID id);
    CustomerResponseDTO update (CustomerRequestDTO dto, UUID id);
    void delete (UUID id);
    List<CustomerResponseDTO> findAll();
}
