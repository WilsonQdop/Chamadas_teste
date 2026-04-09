package com.WilsonQdop.Chamadas.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.POST,"/token/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/customer/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/technical/register").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/customer/findAll").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/technical/assign").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/technical/update").authenticated()
                        .requestMatchers(HttpMethod.GET, "/technical/find").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/technical/delete").authenticated()

                        .requestMatchers(HttpMethod.POST, "/called/create").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/called/payment").authenticated()
                        .requestMatchers(HttpMethod.GET, "/called/status/open").authenticated()
                        .requestMatchers(HttpMethod.POST, "/called/finalized").authenticated()

                        .requestMatchers(HttpMethod.POST, "/history/registred").authenticated()


                        .requestMatchers("/backup/**").permitAll()


                        .anyRequest().authenticated())

                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.httpStrictTransportSecurity(
                                        hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requiresChannel(channel -> channel.anyRequest().requiresSecure());

        return http.build();
    }
    @Bean
    public JwtDecoder jwtDecoder () {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
