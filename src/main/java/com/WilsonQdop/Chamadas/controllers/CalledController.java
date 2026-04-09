package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedCalledDTO;
import com.WilsonQdop.Chamadas.models.dtos.calleddto.FinalizedRequestDTO;
import com.WilsonQdop.Chamadas.services.CalledService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("called")
public class CalledController {
    private final CalledService calledService;

    public CalledController(CalledService calledService) {
        this.calledService = calledService;
    }

    @PostMapping("create")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @SecurityRequirement(name="bearerAuth")
    @Operation(
            summary = "Registra um novo chamado",
            description = "Cria um novo registro de chamado e registra ao usuário logado no sistema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do chamado a ser cadastrado"

            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "chamado criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Algum dado foi inserido de forma errada"),
            }
    )
    public ResponseEntity<CalledResponseDTO> create (@RequestBody CalledRequestDTO dto, JwtAuthenticationToken auth) {
        CalledResponseDTO called = this.calledService.create(dto,  auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(called);
    }
    @Operation(
            summary = "Listar chamados com status 'ABERTO'",
            description = """
        Retorna todos os chamados que estão com o status **ABERTO**.
        
        **Acesso restrito:** apenas usuários com perfil de **técnico** (`SCOPE_TECH`).
        
        Cada chamado retornado contém as informações principais do cliente e do técnico (se atribuído).
    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de chamados abertos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado ou token JWT inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas técnicos podem acessar este recurso")
    })
    @GetMapping("status/open")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    public ResponseEntity<List<CalledResponseDTO>> findStausOpen () {
        List<CalledResponseDTO> calledStatus = this.calledService.findByStatusOpen();
        return ResponseEntity.status(HttpStatus.OK).body(calledStatus);
    }

    @Operation(
            summary = "Confirmar pagamento de um chamado",
            description = """
        Confirma o pagamento de um chamado existente.
        
        **Acesso restrito:** apenas usuários autenticados com perfil de **cliente** (`SCOPE_USER`).
        
        Após a confirmação do pagamento:
        - O chamado muda de status para **AGUARDANDO ATRIBUIÇÃO**.
        - O técnico poderá ser designado através do endpoint **PUT /technical/assign/{calledId}**.
    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento confirmado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou pagamento já confirmado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas clientes podem confirmar pagamento"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    @PutMapping("payment/{calledId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Void> paymentConfirmed (@PathVariable  Long calledId, JwtAuthenticationToken auth) {
        this.calledService.paymentConfirmed(calledId, auth);
        return ResponseEntity.ok().build();
    }

    @PostMapping("finalized/{calledId}")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    @Operation(
            summary = "Finalizar um chamado técnico",
            description = """
        Finaliza um chamado previamente atribuído ao técnico autenticado.

         **Acesso restrito:** apenas usuários com perfil de **técnico** (`SCOPE_TECH`).

        Este endpoint deve ser utilizado **após o chamado ter sido pago e atribuído**.
        O técnico deve informar os detalhes da finalização (ex: observações, tempo de serviço, status final, etc.).
    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chamado finalizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou chamado já finalizado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado — token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado — apenas técnicos podem finalizar chamados"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado ou não atribuído ao técnico autenticado")
    })
    public ResponseEntity<FinalizedCalledDTO> finalizedCalled (@PathVariable Long calledId, JwtAuthenticationToken auth,
                                                               @RequestBody FinalizedRequestDTO dto ) {

        FinalizedCalledDTO response = this.calledService.finalizedCalled(calledId, auth, dto);
        return ResponseEntity.ok(response);
    }

}
