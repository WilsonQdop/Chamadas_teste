package com.WilsonQdop.Chamadas.models.dtos.customedto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Esse Records/DTO vai solicitar os dados dos clientes")
public record CustomerRequestDTO(
        @Schema(
                description = "Nome completo do cliente",
                example = "Wilson Francisco do Nascimento"
        )
        String name,
        @Schema(
                description = "E-mail de contato do cliente",
                example = "WilsonQdop@email.com"
        )
        String email,
        @Schema(
                description = "Senha do cliente",
                example = "123456ABC"
        )
        String password) {
}
