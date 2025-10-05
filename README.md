# Infinity Solutions - Backend API

## 📋 Sobre o Projeto

O **Infinity Solutions Backend** é uma aplicação Spring Boot desenvolvida para gerenciar um sistema de locações, oferecendo funcionalidades completas para usuários, produtos, pedidos e gestão de arquivos. O projeto utiliza uma arquitetura hexagonal (Clean Architecture) e integração com AWS S3 para armazenamento de arquivos.

### 🎯 Principais Funcionalidades

- **Gestão de Usuários**: Cadastro e autenticação de pessoas físicas e jurídicas
- **Gestão de Produtos**: CRUD completo de produtos com upload de imagens
- **Sistema de Pedidos**: Controle de locações e documentos auxiliares
- **Armazenamento de Arquivos**: Integração com AWS S3 para arquivos públicos e privados
- **Autenticação JWT**: Sistema de segurança com tokens JWT
- **Envio de E-mails**: Notificações via SMTP
- **API RESTful**: Documentação com OpenAPI/Swagger

### 🏗️ Arquitetura

```
application-service/
├── src/main/java/com/infinitysolutions/applicationservice/
│   ├── core/                    # Regras de negócio
│   │   ├── domain/             # Entidades de domínio
│   │   ├── gateway/            # Interfaces dos gateways
│   │   ├── usecases/           # Casos de uso
│   │   └── exception/          # Exceções de domínio
│   └── infrastructure/         # Camada de infraestrutura
│       ├── config/             # Configurações
│       ├── controller/         # REST Controllers
│       ├── gateway/            # Implementações dos gateways
│       ├── persistence/        # JPA Entities e Repositories
│       └── service/            # Serviços de infraestrutura
```

### 🛠️ Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **MySQL 8.0** - Banco de dados relacional
- **AWS S3** - Armazenamento de arquivos
- **LocalStack** - Simulação AWS para desenvolvimento
- **Docker & Docker Compose** - Containerização
- **Maven** - Gerenciamento de dependências
- **OpenAPI/Swagger** - Documentação da API

---

# 🚀 Executando o Projeto

### 📋 Pré-requisitos

- **Java 21** ou superior
- **Docker** e **Docker Compose**
- **Maven 3.6+** (ou usar o wrapper incluído)
- **Git**

# 🔧 Ambiente de Desenvolvimento

### 1. Clone o repositório
```bash
git clone https://github.com/Org-InfinitySolutions/Backend.git
cd Backend
```

### 2. Configure as variáveis de ambiente (opcional)

Seguindo por este passo não será necessário a utilização de docker, mas é obrigatório configurar o Mysql e Localstack em sua máquina antes de prosseguir.

Crie um arquivo `.env` na raiz do projeto:
```env
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=novalocacoes
DB_USERNAME=root
DB_PASSWORD=281004

# Email
GMAIL_PASSWORD=sua_senha_do_gmail

# AWS LocalStack (desenvolvimento)
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=test
AWS_SECRET_ACCESS_KEY=test
AWS_S3_ENDPOINT=http://localhost:4566
AWS_USE_LOCALSTACK=true
```

A estrutura do projeto ficará assim:
```
Backend
├── application-service/
├── README.md
├── .env
├── bd_script.sql
└── docker-compose.yaml
```

2.1 Depois de ajustar as credenciais você poderá subir o projeto 

## Docker compose do localstack:

Caso tenha docker instalado, adicione o trecho abaixo no arquivo ```docker-compose.yaml``` localizado na raiz do projeto.

```bash

  localstack:
    image: localstack/localstack:latest
    container_name: localstack-s3
    ports:
      - "4566:4566"
      - "4510-4559:4510-4559"
    environment:
      SERVICES: s3
      DEBUG: 1
      AWS_ACCESS_KEY_ID: test
      AWS_SECRET_ACCESS_KEY: test
      PERSISTENCE: "1"
      DATA_DIR: /var/lib/localstack/data
      CLEAR_TMP_FOLDER: "0"
    volumes:
      - localstack-data:/var/lib/localstack
      - /var/run/docker.sock:/var/run/docker.sock
    networks:
      - novalocacoes-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:4566/_localstack/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 10s
```

### 3. Inicie os serviços com Docker

#### 3.1 Para executar todos os serviços gere o arquivo .JAR do projeto
```
# Iniciar todos os serviços (aplicação, banco)
docker-compose up -d
````

#### 3.2 Caso queira executar a API pela IDE suba apenas a insfraestrutura
```
# Ou iniciar apenas a infraestrutura
docker-compose up -d db_service localstack
```

### 4. Execute a aplicação localmente (alternativa)
```bash
# Usando Maven Wrapper
./mvnw spring-boot:run -Dspring.profiles.active=development

# Ou usando Maven instalado
mvn spring-boot:run -Dspring.profiles.active=development
```

### 5. Acesse a aplicação
- **API**: http://localhost:8082
- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **Banco de dados**: localhost:3307 (via Docker)
- **LocalStack S3**: http://localhost:4566

### 🌐 Ambiente de Produção

#### 1. Configuração AWS

**Opção A: Credenciais IAM User**
```bash
# Configure as variáveis de ambiente
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=sua_access_key
export AWS_SECRET_ACCESS_KEY=sua_secret_key
export AWS_S3_PUBLIC_BUCKET=infinitysolutions-arquivos-publicos
export AWS_S3_PRIVATE_BUCKET=infinitysolutions-arquivos-privados
export AWS_USE_LOCALSTACK=false
```

**Opção B: IAM Role (recomendado para EC2/ECS)**
- Configure uma IAM Role com permissões S3
- Associe a role à instância EC2 ou task ECS
- A aplicação detectará automaticamente as credenciais

#### 2. Banco de Dados
Configure um banco MySQL/RDS na AWS:
```bash
export DB_HOST=seu-rds-endpoint.amazonaws.com
export DB_PORT=3306
export DB_NAME=novalocacoes
export DB_USERNAME=root
export DB_PASSWORD=sua_senha_segura
```

#### 3. Deploy

**Via Docker:**
```bash
# Build da imagem
docker build -t infinity-solutions-backend ./application-service

# Execute com as variáveis de ambiente
docker run -p 8080:8082 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DB_HOST=seu-db-host \
  -e AWS_REGION=us-east-1 \
  infinity-solutions-backend
```

**Via JAR:**
```bash
# Build do projeto
./mvnw clean package -DskipTests

# Execute o JAR
java -jar -Dspring.profiles.active=production \
  target/application-service-0.0.1-SNAPSHOT.jar
```

---

## 📊 Banco de Dados

### Executar Script de Inicialização
```bash
# O script bd_script.sql é executado automaticamente no Docker
# Para execução manual:
mysql -h localhost -P 3307 -u root -p 281004 novalocacoes < bd_script.sql
```

---

## 🔧 Configuração e Variáveis de Ambiente

### Principais Variáveis

| Variável | Descrição | Padrão | Obrigatória |
|----------|-----------|---------|-------------|
| `SERVER_PORT` | Porta da aplicação | `8082` | Não |
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `development` | Não |
| `DB_HOST` | Host do banco de dados | `localhost` | Sim |
| `DB_PORT` | Porta do banco | `3306` | Não |
| `DB_NAME` | Nome do banco | `novalocacoes` | Sim |
| `DB_USERNAME` | Usuário do banco | `root` | Sim |
| `DB_PASSWORD` | Senha do banco | - | Sim |
| `GMAIL_PASSWORD` | Senha do Gmail para SMTP | - | Sim |
| `AWS_REGION` | Região AWS | `us-east-1` | Não |
| `AWS_ACCESS_KEY_ID` | Access Key AWS | - | Prod |
| `AWS_SECRET_ACCESS_KEY` | Secret Key AWS | - | Prod |
| `AWS_USE_LOCALSTACK` | Usar LocalStack | `true` | Não |

---

## 🚨 Troubleshooting

### Problemas Comuns

**1. Erro de conexão com o banco:**
```bash
# Verificar se o MySQL está rodando
docker-compose ps db_service

# Verificar logs
docker-compose logs db_service
```

**2. LocalStack não está funcionando:**
```bash
# Reiniciar o LocalStack
docker-compose restart localstack

# Verificar health
curl http://localhost:4566/_localstack/health
```

**3. Erro de permissão AWS:**
- Verificar se as credenciais estão corretas
- Confirmar se o usuário IAM tem permissões S3
- Verificar se os buckets existem

**4. Aplicação não inicia:**
```bash
# Verificar logs
docker-compose logs application_service

# Verificar variáveis de ambiente
docker-compose exec application_service env | grep AWS
```

---
