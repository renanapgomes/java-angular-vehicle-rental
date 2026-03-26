# desafio-tecnico-solides

Sistema de **aluguel de veículos** (teste técnico): API REST em **Spring Boot** (arquitetura em camadas inspirada em e app ** Angular** : módulos, rotas lazy, serviços, `shared/models`).

## Documentação por módulo

- **[backend/README.md](backend/README.md)** — arquitetura, pacotes, endpoints, testes e JaCoCo (estilo **Coupon API**).
- **[frontend-angular/README.md](frontend-angular/README.md)** — (Angular puro) rotas e execução do frontend.

## Requisitos

- **Backend:** JDK 21+  
- **Frontend:** Node.js 18+ e npm  
- **Docker (opcional):** Docker Compose

## Como executar

### Backend

```bash
cd backend
./gradlew bootRun
```

- API: `http://localhost:8080`  
- Swagger UI: `http://localhost:8080/swagger-ui.html`  
- Console H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:rentaldb`, usuário `sa`, senha vazia)

Perfil padrão `dev` carrega veículos e um cliente de exemplo.

### Frontend

```bash
cd frontend-angular
npm install
npm start
```

Abra normalmente `http://localhost:4200` (servidor dev do Angular). A API é chamada em `http://localhost:8080/api` (veja `src/environments/environment.ts`).

### Docker Compose (API + Nginx com build estático do Angular)

Na raiz do projeto:

```bash
cd desafio-tecnico-solides
docker compose up --build
```

- Frontend: **`http://localhost:8100`** (proxy `/api` → API)  
- API direta: `http://localhost:8080`

Rodar em segundo plano:

```bash
docker compose up --build -d
```

Parar os containers:

```bash
docker compose down
```

## Diferenciais implementados (PDF)

- Testes automatizados (JUnit + Jacoco no backend)  
- Documentação OpenAPI / Swagger  
- Cache (Caffeine) na consulta de veículos disponíveis por período  
- Docker / Docker Compose  
- Arquitetura em camadas (domínio, casos de uso, portas, adaptadores JPA e web)  
- UX: abas, modais, refresher, feedback com toast, pré-cálculo do total no front  

## Exemplos de requisições (cURL)

Substitua `VEHICLE_ID` e `CLIENT_ID` pelos UUIDs retornados pelos POSTs.

### Cadastrar veículo

```bash
curl -s -X POST http://localhost:8080/api/vehicles \
  -H "Content-Type: application/json" \
  -d "{\"plate\":\"RIO2A19\",\"brand\":\"Hyundai\",\"model\":\"HB20\",\"year\":2024,\"dailyRate\":120.00}"
```

### Listar veículos

```bash
curl -s http://localhost:8080/api/vehicles
```

### Veículos disponíveis entre datas (sem aluguel ativo conflitante)

```bash
curl -s "http://localhost:8080/api/vehicles/available?from=2025-04-01&to=2025-04-05"
```

### Cadastrar cliente

```bash
curl -s -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d "{\"fullName\":\"João Teste\",\"document\":\"52998224725\",\"email\":\"joao@email.com\",\"phone\":\"11988887777\"}"
```

### Registrar aluguel

```bash
curl -s -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d "{\"vehicleId\":\"VEHICLE_ID\",\"clientId\":\"CLIENT_ID\",\"startDate\":\"2025-04-01\",\"endDate\":\"2025-04-03\"}"
```

### Histórico de aluguéis

```bash
curl -s http://localhost:8080/api/rentals
```

### Cancelar aluguel

```bash
curl -s -X POST http://localhost:8080/api/rentals/RENTAL_ID/cancel
```

## Testes (backend)

```bash
cd backend
./gradlew test
```

Relatório de cobertura: `backend/build/reports/jacoco/test/html/index.html`.

## Estrutura de pastas

```
desafio-tecnico-solides/
├── backend/          # Spring Boot (Gradle) + README.md
├── frontend-angular/ # Angular puro + README.md
├── docker-compose.yml
└── README.md
```
