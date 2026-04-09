package com.WilsonQdop.Chamadas.mappers;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedCalledDTO;
import com.WilsonQdop.Chamadas.models.Called;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalledMapper {

    Called toDto(CalledRequestDTO dto);

    @Mapping(source = "paymentConfirmed", target = "payment")
    CalledResponseDTO toEntity (Called called);

    @Mapping(source = "status", target = "status")
    FinalizedCalledDTO mapToFinalizedResponse(Called entity);
}
