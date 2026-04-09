package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calledhistorydto.CalledHistoryResponseDTO;
import com.WilsonQdop.Chamadas.services.CalledHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("history")
public class CalledHistoryController {
    private final CalledHistoryService calledHistoryService;

    public CalledHistoryController(CalledHistoryService calledHistoryService) {
        this.calledHistoryService = calledHistoryService;
    }

    @PostMapping("registred")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    @Operation(
            summary = "Registrar observação no histórico do chamado",
            description = """
        Adiciona uma nova observação ao histórico de um chamado técnico.

         **Acesso restrito:** apenas usuários com perfil de **técnico**.

        Este endpoint deve ser utilizado **após o chamado ter sido criado e atribuído a um técnico**.
        Cada registro representa uma atualização de status, comentário técnico ou observação de progresso.
    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação no corpo da requisição"),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas técnicos podem registrar histórico"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado para o registro informado")
    })
    public ResponseEntity<CalledHistoryResponseDTO> registredHistory (@RequestBody CalledHistoryRequestDTO dto, JwtAuthenticationToken auth) {
        CalledHistoryResponseDTO history = this.calledHistoryService.registerObservation(dto, auth);
        return ResponseEntity.ok().body(history);
    }

}
