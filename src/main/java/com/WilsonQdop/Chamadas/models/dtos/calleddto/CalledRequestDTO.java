package com.WilsonQdop.Chamadas.models.dtos.calleddto;

import com.WilsonQdop.Chamadas.enums.CategoryEnum;
import com.WilsonQdop.Chamadas.enums.StatusEnum;

import java.util.UUID;

public record CalledRequestDTO(String title, String description, CategoryEnum category) {
}
