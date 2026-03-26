# Aluguel de Veículos — Frontend (Angular puro)

Este projeto é o frontend do desafio `desafio-tecnico-solides`, desenvolvido com **Angular 16** (sem Ionic) e consumo da API do backend em **Spring Boot**.

## Rotas

- `/veiculos` — lista de veículos e seção de disponibilidade
- `/veiculos/novo` — cadastro de veículo
- `/clientes` — lista de clientes
- `/clientes/novo` — cadastro de cliente
- `/alugueis` — registro de aluguel, histórico e cancelamento

## Como executar localmente

```bash
cd frontend-angular
npm install
npm start
```

O servidor dev do Angular normalmente sobe em `http://localhost:4200`.

## Build e Docker

O build gera arquivos estáticos em `dist/frontend-angular-puro/`.
No Docker, o projeto usa Nginx e proxy reverso para encaminhar chamadas de `/api` para a API em `api:8080`.

# Aluguel de Veículos

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.12.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
