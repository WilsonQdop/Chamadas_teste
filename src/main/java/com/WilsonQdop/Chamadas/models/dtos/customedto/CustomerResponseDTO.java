package com.WilsonQdop.Chamadas.models.dtos.customedto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Esse Records/DTO vai retornar os dados dos clientes")
public record CustomerResponseDTO(
        @Schema(
                description = "Identificador único do cliente",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id,
        @Schema(
                description = "Nome completo do cliente",
                example = "Wilson Francisco do Nascimento"
        )
        String name,
        @Schema(
                description = "E-mail do cliente",
                example = "WilsonQdop@email.com"
        )
        String email,
        @Schema(
                description = "Senha do cliente",
                example = "123456ABC"
        )
        String password) {
}
