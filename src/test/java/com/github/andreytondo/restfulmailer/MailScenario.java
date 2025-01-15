package com.github.andreytondo.restfulmailer;

import com.github.andreytondo.restfulmailer.adapter.rest.dto.MailRequest;
import com.github.andreytondo.restfulmailer.domain.Mail;
import com.github.andreytondo.restfulmailer.domain.MailServerConnection;

import java.util.List;

public class MailScenario {

    private static final String FROM = "no-reply@andreytondo.github.com";
    private static final String TO = "non-existing@andreytondo.github.com";

    public static MailRequest createDefaultRequest() {
        return createMailRequest(createDefaultConnection(), List.of(createDefaultMail("Test", "Test")));
    }

    public static MailRequest createMailRequest(MailServerConnection conn, List<Mail> mails) {
        MailRequest mailRequest = new MailRequest();
        mailRequest.setMailServer(conn);
        mailRequest.setMails(mails);
        return mailRequest;
    }

    public static Mail createDefaultMail(String subject, String content) {
        return createMail(List.of(TO), FROM, subject, content);
    }

    public static Mail createMail(List<String> to, String from, String subject, String content) {
        Mail mail = new Mail();
        mail.setTo(to);
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.setContent(content);
        return mail;
    }

    public static MailServerConnection createDefaultConnection() {
        return createConnection(MailServer.HOST, MailServer.PORT, MailServer.username, MailServer.password);
    }

    public static MailServerConnection createConnection(String host, int port, String username, String password) {
        MailServerConnection mailServerConnection = new MailServerConnection();
        mailServerConnection.setHostname(host);
        mailServerConnection.setPort(port);
        mailServerConnection.setUsername(username);
        mailServerConnection.setPassword(password);
        return mailServerConnection;
    }
}
