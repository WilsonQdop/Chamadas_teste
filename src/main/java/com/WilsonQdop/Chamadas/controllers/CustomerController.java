package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.customedto.CustomerResponseDTO;
import com.WilsonQdop.Chamadas.models.Customer;
import com.WilsonQdop.Chamadas.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("customer")
@Tag(name= "Customer", description = "Controlador para todo o fluxo relacionado aos cliente")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("register")
    @Operation(
            summary = "Cadastrar um novo cliente",
            description = "Cria um novo registro de cliente no sistema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do cliente a ser cadastrado"

            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Algum dado foi inserido de forma errada"),
            }
    )
    public ResponseEntity<CustomerResponseDTO> save(@RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO customer = this.customerService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }


@GetMapping("find/{id}")
@SecurityRequirement(name= "bearerAuth")
@Operation(
        summary = "Buscar através do ID",
        description = "Buscar clientes no sistema através do ID com autenticação",
    parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                    name = "id",
                    description = "UUID do cliente a ser buscado",
                    required = true
            )
    },
    responses = {
            @ApiResponse(responseCode = "200", description = "Cliente buscado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
    }
)

    public ResponseEntity<Customer> find (@PathVariable UUID id) {
        Customer customer = this.customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @Operation (
        summary = "Atualizar cliente com ID",
        description = "Endpoint para atualizar os dados de um usuário",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "UUID do cliente a ser atualizado",
                            required = true
                    )
            },

            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do cliente a ser atualizado"

            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente foi atualizado com sucesso com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
            }
    )

    @PutMapping("update/{id}")
    public ResponseEntity<CustomerResponseDTO> update (@PathVariable UUID id, @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO customer = this.customerService.update(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @DeleteMapping("delete/{id}")
    @Operation (
            summary = "Deletar usuários",
            description = "Endpoint para deletar um usuário através do ID",
            parameters = {
                @io.swagger.v3.oas.annotations.Parameter (
                        name = "id",
                        description = "UUID do cliente a ser deletado",
                        required = true

                )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Esse usuário não existe na base de dados"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão"),
            }
    )
    public ResponseEntity<Void> delete   (@PathVariable UUID id) {
        this.customerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("findAll")
    @PreAuthorize("hasAuthority('SCOPE_ADM')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna uma lista de todos os clientes cadastrados no sistema. "
                    + "Apenas administradores autenticados (SCOPE_ADM) podem acessar este endpoint.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado — token ausente ou inválido"),
                    @ApiResponse(responseCode = "403", description = "Proibido — usuário não tem permissão de administrador"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    public ResponseEntity<List<CustomerResponseDTO>> findAll   () {
       List<CustomerResponseDTO> customers = this.customerService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }
}
