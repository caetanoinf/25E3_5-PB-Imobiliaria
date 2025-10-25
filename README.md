# Sistema de Imobiliária - Microsserviços

Sistema de imobiliária com arquitetura de microsserviços usando Spring Cloud.

## Serviços

| Serviço      | Porta | Descrição                                                   |
| ------------ | ----- | ----------------------------------------------------------- |
| **Eureka**   | 8761  | Service Discovery - Registro e descoberta de microsserviços |
| **Gateway**  | 8080  | API Gateway - Roteamento centralizado                       |
| **User**     | 8081  | Gerenciamento de usuários e autenticação                    |
| **Imovel**   | 8082  | Gerenciamento de imóveis e favoritos                        |
| **Keycloak** | 8180  | Servidor de autenticação OAuth2                             |
| **Postgres** | 5432  | Banco de dados                                              |

### User Service

Responsável pela gestão de usuários:

- CRUD de usuários
- Autenticação e autorização com Keycloak
- Perfis de usuário (cliente, corretor, administrador)

### Imovel Service

Responsável pela gestão de imóveis:

- CRUD de imóveis
- Sistema de favoritos por usuário
- Auditoria de alterações com Hibernate Envers
- Filtros e buscas avançadas

## Executar

```bash
docker-compose up -d
```

Dashboard Eureka: http://localhost:8761

## Testar

Verificar se os serviços estão funcionando:

```bash
# Eureka
curl http://localhost:8761

# Gateway + User Service
curl http://localhost:8080/api/users

# Gateway + Imovel Service
curl http://localhost:8080/api/imoveis
```

## Endpoints

### Usuários (via Gateway - porta 8080)

- `GET /api/users` - Listar usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `POST /api/users` - Criar usuário
- `PUT /api/users/{id}` - Atualizar usuário
- `DELETE /api/users/{id}` - Deletar usuário

### Imóveis (via Gateway - porta 8080)

- `GET /api/imoveis` - Listar imóveis
- `GET /api/imoveis/{id}` - Buscar imóvel por ID
- `POST /api/imoveis` - Criar imóvel
- `PUT /api/imoveis/{id}` - Atualizar imóvel
- `DELETE /api/imoveis/{id}` - Deletar imóvel
- `GET /api/imoveis/status/{status}` - Buscar por status
- `POST /api/imoveis/buscar` - Busca com filtros
- `GET /api/imoveis/favoritados` - Listar favoritos
- `POST /api/imoveis/{id}/favoritar` - Favoritar imóvel
