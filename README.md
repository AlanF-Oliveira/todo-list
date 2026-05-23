# Todo List - Fullstack Application

Aplicação fullstack de gerenciamento de tarefas desenvolvida com Java + Spring Boot no backend e React + Vite no frontend.

O projeto permite criar, listar, atualizar, concluir e remover tarefas utilizando arquitetura REST, persistência com PostgreSQL, autenticação JWT, validação de dados e documentação automática com Swagger/OpenAPI.

---

## Deploy

Frontend:
https://todo-list-xi-indol.vercel.app

Backend:
https://todo-list-production-d7d9.up.railway.app

Swagger:
https://todo-list-production-d7d9.up.railway.app/swagger-ui/index.html

---

## Tecnologias

**Backend**
- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Hibernate Validator
- SpringDoc OpenAPI (Swagger)
- JUnit 5 + Mockito

**Frontend**
- React 18
- Vite
- CSS puro

## Funcionalidades

- Cadastro e login de usuários com JWT
- Cada usuário gerencia apenas suas próprias tarefas
- Criar tarefa com título e descrição
- Listar todas as tarefas
- Buscar tarefa por ID
- Atualizar tarefa parcialmente (PATCH)
- Deletar tarefa
- Marcar tarefa como concluída/pendente
- Cancelar tarefa
- Ordenação por status (Pendente → Concluído → Cancelado)
- Validação de dados
- Documentação dos endpoints (Swagger)
- Testes unitários (Service e Controller)

## Estrutura do Projeto

todo-list/
├── src/                  # Backend Spring Boot
│   └── main/java/com/alan/todo_list/
│       ├── config/       # Configurações (CORS)
│       ├── controller/   # Endpoints REST
│       ├── dto/          # Request e Response DTOs
│       ├── entity/       # Entidade JPA
│       ├── enums/        # TaskStatus
│       ├── exception/    # Exception Handler global
│       ├── mapper/       # Conversão entre camadas
│       ├── repository/   # Repositório JPA
│       ├── security/     # JWT e Spring Security
│       └── service/      # Regras de negócio
└── frontend/             # React + Vite
└── src/
├── pages/        # TaskPage, LoginPage, CadastroPage
└── services/     # taskService, authService\

## Como rodar localmente

### Pré-requisitos
- Java 21+
- Maven
- PostgreSQL
- Node.js 18+

### Backend

1. Crie o banco de dados:

CREATE DATABASE "db_todo-list";

2. Configure variáveis de ambiente:

PGHOST=localhost
PGPORT=5432
PGDATABASE=db_todo-list
PGUSER=seu_usuario
PGPASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta
JWT_EXPIRATION=86400000

3. Rode a aplicação:

./mvnw spring-boot:run

A API estará disponível em http://localhost:8080

### Frontend

cd frontend
npm install
npm run dev

O frontend estará disponível em http://localhost:5173

## Documentação da API

Com a aplicação rodando, acesse:
http://localhost:8080/swagger-ui/index.html

## Endpoints

| Método | Endpoint           | Descrição            | Auth |
|--------|--------------------|----------------------|------|
| POST   | /auth/cadastrar    | Cadastrar usuário    | Não  |
| POST   | /auth/entrar       | Login                | Não  |
| POST   | /task              | Criar tarefa         | Sim  |
| GET    | /task              | Listar tarefas       | Sim  |
| GET    | /task/{id}         | Buscar por ID        | Sim  |
| PATCH  | /task/{id}         | Atualizar tarefa     | Sim  |
| DELETE | /task/{id}         | Deletar tarefa       | Sim  |

## Testes

./mvnw test 