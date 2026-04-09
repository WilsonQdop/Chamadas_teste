package com.WilsonQdop.Chamadas.services;

import com.WilsonQdop.Chamadas.models.dtos.tokendto.LoginRequestDTO;
import com.WilsonQdop.Chamadas.models.dtos.tokendto.LoginResponseDTO;
import com.WilsonQdop.Chamadas.models.Role;
import com.WilsonQdop.Chamadas.repositories.PersonRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public TokenService(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder,
                        PersonRepository personRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    public LoginResponseDTO login (LoginRequestDTO login) {
        var person = this.personRepository.findByEmail(login.email());

        if (person.isEmpty() || !person.get().isLoginCorrect(login, passwordEncoder)) {
            throw new BadCredentialsException("Email ou senha é inválido");
        }

        String scopes = person.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        int expiresIn = 300;
         JwtClaimsSet claims = JwtClaimsSet.builder()
                 .issuer("chamadas")
                 .subject(person.get().getId().toString())
                 .issuedAt(Instant.now())
                 .expiresAt(Instant.now().plusSeconds(expiresIn))
                 .claim("scope", scopes)
                .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue,  expiresIn);
    }

}
