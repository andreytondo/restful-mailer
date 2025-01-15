# RESTful Mailer - Arquitetura e Design

Esta é a implementação de um serviço RESTful para envio de e-mails, baseado em Quarkus. Ela utiliza o `Vertx MailerClient` para envio de e-mails e o `GreenMail` para testes automatizados.

---

## 1. Arquitetura Hexagonal e Limpa

### Organização de Pacotes
A organização do código segue uma estrutura modular e separada, promovendo independência entre camadas:
- **`domain`**: Contém as entidades centrais (`Mail`, `Attachment`, `MailServerConnection`), responsáveis por encapsular as regras de negócio.
- **`application`**: Define as interfaces de serviço (`MailService`) para a lógica de aplicação.
- **`adapter`**: Implementa as interfaces e interage com sistemas externos.
    - **`mailer`**: Adapta as funções de envio de e-mails utilizando o `Vertx MailerClient`.
    - **`rest`**: Exponibiliza uma API REST com endpoints como `/mail` para envio de e-mails.

### Interações Entre Camadas
- A camada **`rest`** injeta a interface **`MailService`**, que é implementada pelo adaptador na camada **`mailer`**.
- As entidades no pacote **`domain`** são usadas diretamente em todas as camadas, evitando dependências desnecessárias entre implementações específicas.

#### Exemplo: Fluxo de Envio de E-mail
1. O **`MailerResource`** recebe um POST com um `MailRequest`.
2. O **`MailService`** transforma os dados e delega o envio à implementação do **`MailServiceImpl`**.
3. O adaptador **`VertxMailAdapter`** cria a configuração e os objetos necessários para o envio via `Vertx MailerClient`.

---

## 2. Utilização do `Vertx MailerClient`

O `Vertx MailerClient` é integrado para gerenciar a comunicação com servidores SMTP. Ele é configurado dinamicamente baseado em dados fornecidos em runtime.

### Configuração
No adaptador **`VertxMailAdapter`**, é criado um objeto `MailConfig`:
```java
MailConfig createMailConfig(MailServerConnection mailServerConnection) {
    return new MailConfig()
            .setHostname(mailServerConnection.getHostname())
            .setPort(mailServerConnection.getPort())
            .setUsername(mailServerConnection.getUsername())
            .setPassword(mailServerConnection.getPassword());
}
```

### Envio de E-mails
A classe MailServiceImpl utiliza o MailClient para enviar e-mails:
```java
@Override
public void send(MailServerConnection mailServerConnection, Mail... mail) {
    MailClient mailClient = createMailClient(mailServerConnection);
    for (Mail m : mail) {
        mailClient.sendMail(vertxMailAdapter.createMailMessage(m))
                .onFailure(e -> {
                    LOGGER.error("Failed to send mail", e);
                    mailClient.close();
                    throw new RuntimeException(e);
                });
    }
}
```

## 2. Utilização do `GreenMail` para Testes

O `GreenMail` é usado para simular um servidor SMTP em testes automatizados, permitindo verificar o envio de e-mails sem interagir com um servidor externo.

### Configuração do Servidor
A classe `MailServer` gerencia a configuração e execução:
```java
static {
    greenMail = new GreenMail(new ServerSetup(PORT, HOST, ServerSetup.PROTOCOL_SMTP));
    addUser(username, password);
}
```

- **Métodos principais:**
    - `start()`: Inicia o servidor.
    - `stop()`: Para o servidor.
    - `clearInboxes()`: Remove e-mails armazenados.

### Exemplo de Teste Automatizado

Na classe MailerResourceTest, o envio é testado verificando a chegada de e-mails simulados:
```java
@Test
void testSendMail() {
    given()
        .contentType(ContentType.JSON)
        .body(MailScenario.createDefaultRequest())
        .when()
        .post("/mail")
        .then()
        .statusCode(200);

    Assertions.assertTrue(MailServer.awaitIncomingEmail(1));
}
```