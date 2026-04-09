package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.models.dtos.tokendto.LoginRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.tokendto.LoginResponseDTO;
import com.WilsonQdop.Chamadas.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
@Tag(name = "Autenticação", description = "Endpoints para login e autenticação de usuários")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("login")

    @Operation(
            summary = "Login do usuário",
            description = "Autentica um usuário com email e senha, retornando um token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO contendo email e senha do usuário",
                    content = @Content(
                            schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de login",
                                    value = "{ \"email\": \"usuario@email.com\", \"senha\": \"123456\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = LoginResponseDTO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de token",
                                            value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            }
    )
    public ResponseEntity<LoginResponseDTO> login (@RequestBody LoginRequestDTO login) {
        LoginResponseDTO token = this.tokenService.login(login);
        return ResponseEntity.ok(token);
    }
}
