package com.github.andreytondo.restfulmailer.application;

import com.github.andreytondo.restfulmailer.domain.Mail;
import com.github.andreytondo.restfulmailer.domain.MailServerConnection;

public interface MailService {

    void send(MailServerConnection mailServerConnection, Mail...mail);
}
