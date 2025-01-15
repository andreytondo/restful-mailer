package com.github.andreytondo.restfulmailer.adapter.rest;

import com.github.andreytondo.restfulmailer.adapter.rest.dto.MailRequest;
import com.github.andreytondo.restfulmailer.application.MailService;
import com.github.andreytondo.restfulmailer.domain.Mail;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mail")
public class MailerResource {

    @Inject
    MailService mailService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMail(MailRequest request) {
        Mail[] mails = request.getMails().toArray(new Mail[0]);
        mailService.send(request.getMailServer(), mails);
        return Response.ok().build();
    }
}
