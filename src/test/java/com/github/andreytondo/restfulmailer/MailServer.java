package com.github.andreytondo.restfulmailer;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

public class MailServer {

    public static int PORT = 1025;
    public static String HOST = "localhost";
    public static String username = "user";
    public static String password = "password";

    private static final GreenMail greenMail;

    static {
        greenMail = new GreenMail(new ServerSetup(PORT, HOST, ServerSetup.PROTOCOL_SMTP));
        addUser(username, password);
    }

    public static void start() {
        greenMail.start();
    }

    public static void clearInboxes() {
        try {
            greenMail.purgeEmailFromAllMailboxes();
        } catch (FolderException ignored) {}
    }

    public static void stop() {
        greenMail.stop();
    }

    public static void addUser(String username, String password) {
        greenMail.withConfiguration(GreenMailConfiguration.aConfig().withUser(username, password));
    }

    public static boolean awaitIncomingEmail(int emailCount) {
        return awaitIncomingEmail(25000, emailCount);
    }

    public static boolean awaitIncomingEmail(long timeout, int emailCount) {
        return greenMail.waitForIncomingEmail(timeout, emailCount);
    }
}
