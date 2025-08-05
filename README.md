# 🍽️ ServeFast – API REST para Gerenciamento de Restaurante

O **ServeFast** é um sistema completo para gerenciamento de restaurantes, desenvolvido com foco em performance, organização e separação de responsabilidades. O projeto segue a arquitetura MVC e integra **back-end**, **front-end** e **aplicativo mobile**, garantindo uma experiência robusta e moderna tanto para administradores quanto para usuários.

---

## 🔧 Tecnologias Utilizadas

### 📌 Back-end
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- MySQL
- Maven

### 🎨 Front-end Web
- Thymeleaf
- HTML5 / CSS3
- Bootstrap

### 📱 Aplicativo Mobile
- Flutter

---

## 📦 Estrutura do Projeto

```
sistema/
├── src/
│   ├── main/
│   │   ├── java/           # Código Java (controllers, services, entities, repos)
│   │   ├── resources/
│   │   │   ├── templates/  # Arquivos HTML do Thymeleaf
│   │   │   └── static/     # CSS, JS, imagens
│   │   │   └── application.properties
├── README.md
└── pom.xml
```

---

## 🚀 Funcionalidades

- Cadastro e gerenciamento de mesas, pedidos e usuários
- Controle de ingredientes, itens do cardápio e promoções
- Gerenciamento de pagamentos e formas de pagamento
- Criação automática de usuários padrão (admin, caixa, cozinha)
- Integração com front-end via Thymeleaf
- Comunicação REST com aplicativo Flutter
- Autenticação e controle de acesso com Spring Security

---

## 💡 Como Rodar o Projeto Localmente

### 1. Clonar o repositório

```bash
git clone https://github.com/belinDEV/TCC-SERVEFAST.git
cd TCC-SERVEFAST
```

### 2. Criar o banco de dados no MySQL

```sql
CREATE DATABASE restaurante_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

> O banco será atualizado automaticamente pelo Hibernate na primeira execução.

### 3. Configurar o `application.properties`

```properties
spring.datasource.username=root
spring.datasource.password=suaSenhaAqui
```

### 4. Executar o projeto

Via terminal:

```bash
./mvnw spring-boot:run
```

Ou pela IDE (IntelliJ ou VS Code)

---

## 🔐 Acesso Padrão

Usuários criados automaticamente ao iniciar o sistema:

| Usuário   | Senha padrão | Função    |
|-----------|--------------|-----------|
| admin     | admin        | ADMIN     |
| caixa     | caixa        | CAIXA     |
| cozinha   | cozinha      | COZINHA   |

---

## 📱 Integração com Flutter

O aplicativo Flutter se comunica com a API via HTTP REST, consumindo os endpoints expostos pela aplicação Spring Boot.

> Exemplo de endpoint:
> ```
> GET http://localhost:8080/api/pedidos
> ```

---

## 📄 .gitignore (recomendado)

```gitignore
# Build
/target/
*.class
*.jar
*.war
*.log

# IDEs
.idea/
*.iml
.gradle/

# Configs sensíveis
application.properties
.env

# Sistema
.DS_Store
Thumbs.db
```

---

## 📝 LICENSE

Este projeto está licenciado sob os termos da **MIT License**.

```text
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell      
copies of the Software, and to permit persons to whom the Software is          
furnished to do so, subject to the following conditions:                       

The above copyright notice and this permission notice shall be included in     
all copies or substantial portions of the Software.                            

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR     
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,       
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE    
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      
THE SOFTWARE.
```

---


## ✍️ Autor

Projeto de TCC – ServeFast – Sistema de Gerenciamento de Restaurantes
