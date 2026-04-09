package com.WilsonQdop.Chamadas.mappers;


import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.roledto.RoleDTO;
import com.WilsonQdop.Chamadas.models.Customer;
import com.WilsonQdop.Chamadas.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toDto(CustomerRequestDTO dto);
    CustomerResponseDTO toEntity (Customer customer);
    List<CustomerResponseDTO> toEntityList(List<Customer> dto);
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CustomerRequestDTO dto, @MappingTarget Customer customer);

    @Mapping(source = "roleId", target = "id")
    RoleDTO toDto(Role role);
    
    default Set<RoleDTO> mapRoles(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
