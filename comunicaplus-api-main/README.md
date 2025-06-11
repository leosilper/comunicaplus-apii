
# 📋 Sobre o Projeto

A **ComunicaPlus** API é uma aplicação backend desenvolvida com **Spring Boot 3**, criada para viabilizar a comunicação entre celulares em situações de emergência, mesmo sem internet, sinal de operadora ou energia elétrica.

O sistema apoia um aplicativo móvel leve, que utiliza Bluetooth e Wi-Fi Direct para enviar e encaminhar mensagens entre dispositivos próximos, garantindo que informações e pedidos de socorro possam circular mesmo em cenários de desastre.

As mensagens possuem informações como:

- 📬 **Conteúdo**
- 📱 **Dispositivo Remetente**
- 📥 **Dispositivo Destinatário**
- ⏰ **Timestamp**
- 📩 **Status de entrega e encaminhamento**

Com suporte a **filtros dinâmicos via Specification** e **cache de resultados** para otimizar a performance.

---

# 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security (JWT)
- Lombok
- Spring Cache
- H2 Database (em memória)
- Specification API (para filtros dinâmicos)
- Swagger / OpenAPI 3
- Spring DevTools (hot reload)

---

# ⚙️ Como Rodar o Projeto

## ✅ Pré-requisitos:
- Java 17+
- Maven 3.8+

## 🚀 Executar localmente:

```bash
# Clone o repositório
git clone https://github.com/leosilper/comunicaplus-api.git

# Acesse o diretório
cd comunicaplus-api-main

# Execute o projeto
./mvnw spring-boot:run
```

---

# 🔗 Principais Endpoints

| Método | Endpoint | Descrição |
|-------- |--------- |---------- |
| GET | /api/messages | Listar mensagens (com filtros) |
| POST | /api/messages | Criar uma nova mensagem |
| GET | /api/messages/{id} | Buscar mensagem por ID |
| PUT | /api/messages/{id} | Atualizar mensagem existente |
| DELETE | /api/messages/{id} | Deletar mensagem |
| POST | /api/devices | Cadastrar novo dispositivo |
| GET | /api/devices/summary | Resumo dos dispositivos e mensagens |
| POST | /users | Cadastrar novo usuário |
| POST | /auth/login | Autenticar e obter Token JWT |

---

# 🔍 Exemplos de Uso no Postman

## 🔐 1. Autenticar e obter Token JWT

```http
POST http://localhost:8080/auth/login
```

**Body (JSON):**  
```json
{
  "email": "alice@email.com",
  "password": "123456"
}
```

**Resposta esperada:**  
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

➡️ Use o token nos headers de todas as requisições protegidas:  

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 🚀 2. Cadastrar um novo usuário

```http
POST http://localhost:8080/users
```

**Body (JSON):**  
```json
{
  "name": "Novo Usuário",
  "email": "novo@email.com",
  "password": "123456",
  "deviceId": "device-novo-001",
  "role": "USER"
}
```

**Resposta esperada:**  
`201 Created` com os dados do usuário criado (sem mostrar a senha).

---

## 🚀 3. Cadastrar um novo dispositivo

```http
POST http://localhost:8080/api/devices
```

**Body (JSON):**  
```json
{
  "deviceName": "Dispositivo Teste",
  "bluetoothAddress": "AA:BB:CC:DD:EE:FF",
  "wifiDirectAddress": "192.168.49.10",
  "status": "ONLINE"
}
```

**Resposta esperada:**  
`201 Created` com os dados do dispositivo.

---

## 🚀 4. Listar resumo dos dispositivos

```http
GET http://localhost:8080/api/devices/summary
```

➡️ Resposta: Lista de dispositivos com quantidade de mensagens enviadas e recebidas, e outros dados.

---

## 🚀 5. Listar todas as mensagens

```http
GET http://localhost:8080/api/messages
```

➡️ Resposta paginada com todas as mensagens registradas.

---

## 🚀 6. Filtrar mensagens

- Por conteúdo:  
```http
GET /api/messages?content=socorro
```

- Por ID do dispositivo remetente:  
```http
GET /api/messages?deviceId=1
```

- Combinar filtros:  
```http
GET /api/messages?content=hello&deviceId=2
```

---

## 🚀 7. Criar uma nova mensagem

```http
POST http://localhost:8080/api/messages/
```

**Body (JSON):**  
```json
{
  "content": "Olá, mundo!",
  "timestamp": "2025-05-29T10:00:00",
  "sender": { "id": 1 },
  "recipient": { "id": 2 },
  "delivered": false,
  "forwarded": false,
  "messageType": "INFO"
}
```

---

## 🚀 8. Atualizar uma mensagem

```http
PUT http://localhost:8080/api/messages/{id}
```

**Body (JSON):**  
```json
{
  "content": "Mensagem atualizada",
  "timestamp": "2025-05-29T12:00:00",
  "sender": { "id": 1 },
  "recipient": { "id": 2 },
  "delivered": true,
  "forwarded": true,
  "messageType": "ALERT"
}
```

---

## 🚀 9. Deletar uma mensagem

```http
DELETE /api/messages/1
```

➡️ Resposta: `204 No Content`

---

# 📚 Documentação Swagger

Após iniciar a aplicação, acesse:  

```http
http://localhost:8080/swagger-ui.html
```

✅ Interface interativa para testar todos os endpoints!

---

# 🛃 Banco de Dados

O projeto oferece **flexibilidade** para ser executado tanto com um **banco de dados em memória (H2)**, ideal para testes e desenvolvimento, quanto com um **banco de dados Oracle**, ideal para ambientes de homologação e produção.

## ✅ Configurações disponíveis:

### ▶️ **H2 Database (padrão)**

- JDBC URL: `jdbc:h2:mem:comunicaplusdb`
- Driver: `org.h2.Driver`
- Console Web: `http://localhost:8080/h2-console`
- Dialeto: `org.hibernate.dialect.H2Dialect`

### ▶️ **Oracle Database**

- JDBC URL: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`
- Driver: `oracle.jdbc.OracleDriver`
- Usuário: `rm557598`
- Dialeto: `org.hibernate.dialect.OracleDialect`

## ⚙️ Como escolher qual banco utilizar?

O projeto está configurado com **Spring Profiles**, permitindo a alternância entre os ambientes de forma **simples e segura**:

```yaml
spring:
  profiles:
    active: h2   # ou 'oracle' para trocar
```

➡️ Para usar o **H2**:  
```yaml
spring.profiles.active=h2
```

➡️ Para usar o **Oracle**:  
```yaml
spring.profiles.active=oracle
```

✅ Basta alterar o perfil ativo no `application.yml` ou passar como **parâmetro na execução**:

```bash
# Para usar Oracle
./mvnw spring-boot:run -Dspring-boot.run.profiles=oracle

# Para usar H2
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

## 🔗 Configurações comuns (para ambos os bancos):

```yaml
springdoc:
  api-docs.path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method

logging:
  level:
    org.springframework.security: DEBUG
    br.com.fiap.comunicaplus_api_main.config: DEBUG
```

---

# 👥 Intregantes do Grupo

| Nome | RM |
|-------|----|
| Leonardo da Silva Pereira | 557598 |
| Bruno da Silva Souza | 94346 |
| Julio Samuel de Oliveira | 557453 |
