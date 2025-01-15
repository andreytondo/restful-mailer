package com.github.andreytondo.restfulmailer.adapter.mailer;

import com.github.andreytondo.restfulmailer.domain.Attachment;
import com.github.andreytondo.restfulmailer.domain.Mail;
import com.github.andreytondo.restfulmailer.domain.MailServerConnection;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.mail.MailAttachment;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

import java.util.ArrayList;

public class VertxMailAdapter {

    MailConfig createMailConfig(MailServerConnection mailServerConnection) {
        return new MailConfig()
                .setHostname(mailServerConnection.getHostname())
                .setPort(mailServerConnection.getPort())
                .setUsername(mailServerConnection.getUsername())
                .setPassword(mailServerConnection.getPassword());
    }

    MailMessage createMailMessage(Mail mail) {
        MailMessage mailMessage = new MailMessage()
                .setFrom(mail.getFrom())
                .setTo(mail.getTo())
                .setSubject(mail.getSubject())
                .setText(mail.getContent());

        if (mail.getAttachments() != null) {
            for (Attachment attachment : mail.getAttachments()) {
                mailMessage.setAttachment(new ArrayList<>());
                mailMessage.getAttachment().add(createMailAttachment(attachment));
            }
        }
        return mailMessage;
    }

    MailAttachment createMailAttachment(Attachment attachment) {
        return MailAttachment.create()
                .setName(attachment.getName())
                .setDescription(attachment.getDescription())
                .setData(Buffer.buffer(toByteArray(attachment.getData())))
                .setContentType(attachment.getContentType())
                .setContentId(attachment.getContentId());
    }

    byte[] toByteArray(Byte[] bytes) {
        byte[] byteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byteArray[i] = bytes[i];
        }
        return byteArray;
    }

}