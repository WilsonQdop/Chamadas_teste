# Chamadas Técnicas API

Projeto backend desenvolvido em **Java com Spring Boot** para gerenciamento de chamados técnicos, simulando um modelo **pay-per-service**. O sistema é voltado ao aprendizado prático de boas práticas como **Clean Code** e **princípios SOLID**, com foco em uma arquitetura escalável.

---

## 🔧 Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- MapStruct
- Maven
- Postman (para testes de endpoints)

---

## 🔄 Fluxo do sistema

1. **Cadastro de clientes e técnicos**

   - Clientes e técnicos são registrados com dados básicos (nome, email, telefone).

2. **Criação de chamado técnico**

   - Cliente solicita um chamado com: título, descrição e categoria (ALTA, MEDIA, BAIXA).
   - O valor é definido automaticamente: R\$200, R\$150, R\$100 respectivamente.
   - O chamado é salvo com `pagamento = false` e `status = OPEN`.

3. **Simulação de pagamento**

   - Endpoint simula pagamento via PIX ou cartão.
   - Ao confirmar pagamento, `pagamento = true` e o chamado fica disponível para atribuição.

4. **Atribuição de chamado a um técnico**

   - Técnico pode se atribuir a um chamado com `status = OPEN` e `pagamento = true`.
   - O `status` do chamado muda para `ASSIGNED`.

---

## Endpoints principais

| Método | Caminho                           | Descrição                   |
| ------ | --------------------------------- | --------------------------- |
| POST   | `/customer`                       | Criar cliente               |
| POST   | `/technical`                      | Criar técnico               |
| POST   | `/called/{customerId}`            | Criar chamado para cliente  |
| PUT    | `/called/pay/{calledId}`          | Simular pagamento           |
| PUT    | `/technical/{techId}/assign/{id}` | Atribuir chamado ao técnico |
| GET    | `/called/open`                    | Buscar chamados abertos     |

---

##  Como rodar o projeto

### Requisitos

- Java 17+
- Maven
- MySQL rodando localmente (ou container Docker)

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
```

2. Configure o `application.properties` ou `application.yml` com suas credenciais do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chamadas
spring.datasource.username=root
spring.datasource.password=senha
```

3. Execute o projeto:

```bash
./mvnw spring-boot:run
```

4. Acesse os endpoints no Postman ou Insomnia: `http://localhost:8080`

---

## Futuros passos

- Implementação de autenticação com JWT
- Integração real com API do Mercado Pago
- Listagem personalizada para cada técnico
- Notificações por e-mail
- Painel administrativo com dashboard de chamados

---

##  Contribuições

Sinta-se à vontade para enviar PRs, abrir issues ou dar feedbacks.

---

**Desenvolvido por [Wilson Francisco do Nascimento]**

[LinkedIn](https://www.linkedin.com/in/wilson-ads) | [GitHub](https://github.com/WilsonQdop)

---

