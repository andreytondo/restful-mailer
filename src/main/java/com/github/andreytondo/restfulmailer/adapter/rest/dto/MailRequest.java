package com.github.andreytondo.restfulmailer.adapter.rest.dto;

import com.github.andreytondo.restfulmailer.domain.Mail;
import com.github.andreytondo.restfulmailer.domain.MailServerConnection;

import java.util.List;

public class MailRequest {

    private MailServerConnection mailServerConnection;
    private List<Mail> mails;

    public MailServerConnection getMailServer() {
        return mailServerConnection;
    }

    public void setMailServer(MailServerConnection mailServerConnection) {
        this.mailServerConnection = mailServerConnection;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }
}