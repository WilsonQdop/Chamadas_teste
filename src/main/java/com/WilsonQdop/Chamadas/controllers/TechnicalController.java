package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.models.dtos.calleddto.CalledResponseDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.technicaldto.TechnicalResponseDTO;
import com.WilsonQdop.Chamadas.models.Technical;
import com.WilsonQdop.Chamadas.services.TechnicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("technical")
public class TechnicalController {
    private final TechnicalService technicalService;

    public TechnicalController(TechnicalService technicalService) {
        this.technicalService = technicalService;
    }

    @Operation(
            summary = "Cadastrar um novo técnico",
            description = "Cria um novo registro de técnico no sistema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do técnico a ser cadastrado"

            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "técnico criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Algum dado foi inserido de forma errada"),
            }
    )
    @PostMapping("register")
    public ResponseEntity<TechnicalResponseDTO> save (@RequestBody TechnicalRequestDTO dto) {
        TechnicalResponseDTO technical = this.technicalService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(technical);
    }

    @Operation (
            summary = "Atualizar técnico com ID",
            description = "Endpoint para atualizar os dados de um técnico",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "UUID do técnico a ser atualizado",
                            required = true
                    )
            },

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do técnico a ser atualizado"

            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "técnico foi atualizado com sucesso com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
            }
    )
    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    public ResponseEntity<TechnicalResponseDTO> update (@RequestBody TechnicalRequestDTO dto, @PathVariable UUID id) {
        TechnicalResponseDTO technical = this.technicalService.update(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(technical);
    }


    @SecurityRequirement(name= "bearerAuth")
    @Operation(
            summary = "Buscar técnico através do ID",
            description = "Buscar técnico no sistema através do ID com autenticação",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "UUID do técnico a ser buscado",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "técnico buscado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
            }
    )
    @GetMapping("find/{id}")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    public ResponseEntity<Technical> find (@PathVariable UUID id) {
        Technical technical = this.technicalService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(technical);
    }

    @Operation (
            summary = "Deletar um técnico",
            description = "Endpoint para deletar um técnico através do ID",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter (
                            name = "id",
                            description = "UUID do técnico a ser deletado",
                            required = true

                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "técnico deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
            }
    )

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    public ResponseEntity<TechnicalResponseDTO> delete (@PathVariable UUID id) {
         this.technicalService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("assign/{calledId}")
    @PreAuthorize("hasAuthority('SCOPE_TECH')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Atribuir técnico a um chamado",
            description = "Este endpoint atribui o técnico autenticado ao chamado informado.\n" +
                    "        \n" +
                    "        **Importante:** só pode ser utilizado **após o pagamento ser confirmado** \n" +
                    "        através do endpoint `PUT /called/payment/{calledId}` payment = true.\n" +
                    "        \n" +
                    "        Caso o pagamento ainda não tenha sido confirmado, a requisição retornará um erro 400 (Bad Request).",

            parameters = {
                    @Parameter(
                            name = "calledId",
                            description = "ID do chamado que será atribuído ao técnico autenticado",
                            required = true,
                            example = "42"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Técnico atribuído ao chamado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida ou chamado já atribuído"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não possui permissão SCOPE_TECH"),
                    @ApiResponse(responseCode = "404", description = "Chamado não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    public ResponseEntity<CalledResponseDTO> assignToCalled (JwtAuthenticationToken auth, @PathVariable Long calledId) {
        CalledResponseDTO called = this.technicalService.assignToCalled(auth, calledId);
        return ResponseEntity.status(HttpStatus.OK).body(called);
    }

}
