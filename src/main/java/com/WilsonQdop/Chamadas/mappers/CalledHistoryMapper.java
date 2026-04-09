package com.WilsonQdop.Chamadas.mappers;

import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryResponseDTO;
import com.WilsonQdop.Chamadas.models.CalledHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalledHistoryMapper {

     @Mapping(source = "technical.name", target = "technicalName")
     CalledHistoryResponseDTO toDto(CalledHistory history);

}
