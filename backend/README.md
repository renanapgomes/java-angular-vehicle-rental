# Aluguel de Veículos API

API REST em **Java 21** e **Spring Boot** para gestão de **veículos**, **clientes** e **aluguéis**, com cálculo automático do valor total, validação de **disponibilidade por período** (sem sobreposição de aluguéis ativos) e cancelamento de reservas. O projeto segue **arquitetura em camadas / hexagonal**, com regras de negócio no domínio e camada de aplicação independente de JPA e Spring Web.

> Monorepo: o repositório raiz (`desafio-tecnico-solides`) contém também o **frontend Ionic/Angular**, **Docker Compose** e um `README.md` com visão geral e exemplos cURL.

## Tecnologias

### Core & framework

- **Java 21 (LTS)**
- **Spring Boot 3.2.x**
- **Gradle** (build)

### Banco de dados

- **H2** (em memória, perfil local/dev)
- **Spring Data JPA** (somente na camada de infraestrutura)

### Qualidade e produtividade

- **Lombok** — `@Getter`, `@RequiredArgsConstructor`, `@Data`, `@AllArgsConstructor` em domínio, casos de uso, DTOs e entidades JPA.
- **JaCoCo** — relatório de cobertura (`build/reports/jacoco/`).
- **SpringDoc OpenAPI (Swagger)**
- **Spring Cache + Caffeine** — cache da consulta de veículos disponíveis por intervalo de datas (invalidado ao criar/cancelar aluguel).
- **Docker** (Dockerfile no módulo)

### Testes

- **JUnit 5**, **Mockito**, **Spring Boot Test**

---

## Arquitetura

Organização em **domain**, **application** e **infrastructure**, com inversão de dependência: a aplicação depende de **portas** (interfaces); JPA e HTTP são adaptadores.

### Estrutura de pacotes

```
com.rental.solides/
├── RentalApplication.java
│
├── domain/                              # Núcleo (sem Spring, sem JPA)
│   ├── Vehicle.java                     # create(), reconstitute()
│   ├── Client.java
│   ├── Rental.java                      # cálculo de dias, total, periodsOverlap()
│   ├── RentalStatus.java                # ACTIVE, CANCELLED
│   └── BusinessException.java
│
├── application/
│   ├── port/                            # Contratos (Save/Load/RentalOverlap/AvailableVehicles…)
│   ├── usecase/                         # CreateVehicle, CreateRental, CancelRental, …
│   └── exception/
│       └── ResourceNotFoundException.java
│
├── infrastructure/
│   ├── config/
│   │   ├── UseCaseConfig.java           # Beans dos use cases
│   │   ├── WebConfig.java               # CORS
│   │   └── SampleDataLoader.java        # Dados de exemplo (@Profile dev)
│   ├── persistence/                     # Entidades JPA, repositórios, adapters
│   ├── cache/                           # Cache + invalidação em mutações de aluguel
│   └── web/
│       ├── VehicleController.java
│       ├── ClientController.java
│       ├── RentalController.java
│       ├── *WebMapper.java
│       └── GlobalExceptionHandler.java
│
└── dto/
    ├── request/
    └── response/
```

### Princípios aplicados

| Princípio | Como |
|-----------|------|
| **Um use case, uma intenção** | Classes em `application.usecase` com método público `execute(...)`. |
| **Inversão de dependência** | Use cases dependem de `SaveVehiclePort`, `LoadRentalPort`, `RentalOverlapPort`, etc. Implementações em `infrastructure.persistence` e `infrastructure.cache`. |
| **Regras no domínio** | Cálculo de total, dias inclusivos e consistência de período em `Rental`; validações de veículo/cliente nos agregados correspondentes. |
| **Application agnóstica** | Em `domain` e `application` não há imports de `org.springframework.*` nem `jakarta.persistence.*` nos use cases e entidades de domínio. |

### Regras de negócio (resumo)

- **Valor total** = diária do veículo × número de dias (**inclusivo** entre data inicial e final).
- **Conflito de período**: não é permitido novo aluguel **ACTIVE** para o mesmo veículo se o intervalo intersectar um aluguel já ativo.
- **Cancelamento** (`Rental.cancel()`): status → `CANCELLED`; período volta a ficar disponível para novas reservas.
- **Disponibilidade** (`GET /api/vehicles/available`): veículos sem aluguel ativo que cubra o intervalo informado.

### Fluxo (exemplo: registrar aluguel)

1. **RentalController** recebe o DTO, valida (`@Valid`) e chama `CreateRentalUseCase.execute(...)`.
2. **CreateRentalUseCase** carrega veículo e cliente, consulta `RentalOverlapPort`; se ok, `Rental.create(...)` calcula o total e persiste via `SaveRentalPort`.
3. **RentalMutationNotifier** invalida o cache de disponibilidade.
4. Resposta montada com `RentalWebMapper` (inclui placa e nome do cliente quando possível).

---

## Como rodar

### Pré-requisitos

- **JDK 21** (Gradle usa toolchain Java 21)

### Com Gradle

```powershell
./gradlew bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html` (ou `/swagger-ui/index.html`, conforme versão do SpringDoc)
- Console H2: `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:rentaldb`, usuário `sa`, senha vazia)

Perfil padrão **dev** inclui dados de exemplo (veículos e cliente).

### Com Docker (imagem deste módulo)

Na raiz do monorepo:

```bash
docker compose up --build
```

---

## API

### Endpoints

| Verbo | Endpoint | Descrição |
|-------|----------|-----------|
| POST | `/api/vehicles` | Cadastra veículo (placa, marca, modelo, ano opcional, diária). |
| GET | `/api/vehicles` | Lista todos os veículos. |
| GET | `/api/vehicles/{id}` | Busca veículo por UUID. |
| GET | `/api/vehicles/available?from=yyyy-MM-dd&to=yyyy-MM-dd` | Lista veículos **sem** aluguel ativo conflitante no período. |
| POST | `/api/clients` | Cadastra cliente. |
| GET | `/api/clients` | Lista clientes. |
| GET | `/api/clients/{id}` | Busca cliente por UUID. |
| POST | `/api/rentals` | Registra aluguel (body: `vehicleId`, `clientId`, `startDate`, `endDate`). |
| GET | `/api/rentals` | Histórico de aluguéis (ordenado por data de início). |
| GET | `/api/rentals/{id}` | Detalhe do aluguel. |
| POST | `/api/rentals/{id}/cancel` | Cancela aluguel (status `CANCELLED`). |

### Exemplo de payload (POST `/api/rentals`)

```json
{
  "vehicleId": "550e8400-e29b-41d4-a716-446655440000",
  "clientId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
  "startDate": "2025-04-01",
  "endDate": "2025-04-03"
}
```

### Resposta de erro

Erros de validação, regra de negócio e recurso não encontrado retornam **JSON** com `timestamp`, `status`, `error` e `message` (e `errors` com campo/mensagem quando aplicável). Conflitos de integridade (ex.: placa duplicada) retornam **409 Conflict**.

O `GlobalExceptionHandler` (`@RestControllerAdvice`, `@Order(HIGHEST_PRECEDENCE)`) padroniza o corpo em JSON.

---

## Testes e cobertura (JaCoCo)

- **Domain:** `RentalTest` — dias inclusivos, total, sobreposição de períodos.
- **Application:** `CreateRentalUseCaseTest` — overlap e veículo inexistente.
- **Contexto:** `RentalApplicationTests` — sobe o contexto Spring com perfil `test`.

### Executar testes

```powershell
./gradlew test
```

### Relatório JaCoCo

```powershell
./gradlew test jacocoTestReport
```

HTML: **`build/reports/jacoco/index.html`**

---

## Referência rápida

| Item | Valor |
|------|--------|
| Base URL padrão | `http://localhost:8080` |
| Prefixo REST | `/api` |
| Prefixo OpenAPI | `/v3/api-docs` (SpringDoc) |
