package com.github.andreytondo.restfulmailer.adapter.mailer;

import com.github.andreytondo.restfulmailer.application.MailService;
import com.github.andreytondo.restfulmailer.domain.Mail;
import com.github.andreytondo.restfulmailer.domain.MailServerConnection;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import jakarta.enterprise.context.RequestScoped;
import org.jboss.logging.Logger;

@RequestScoped
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class);

    private final Vertx vertx;
    private final VertxMailAdapter vertxMailAdapter = new VertxMailAdapter();

    public MailServiceImpl(Vertx vertx) {
        this.vertx = vertx;
    }

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

    MailClient createMailClient(MailServerConnection mailServerConnection) {
        return MailClient.create(vertx, vertxMailAdapter.createMailConfig(mailServerConnection));
    }

}