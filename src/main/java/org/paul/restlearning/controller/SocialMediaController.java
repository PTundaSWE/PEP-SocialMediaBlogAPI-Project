package org.paul.restlearning.controller;

import org.paul.restlearning.dao.*;
import org.paul.restlearning.model.Account;
import org.paul.restlearning.model.Message;
import org.paul.restlearning.service.AccountService;
import org.paul.restlearning.service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        // DAO wiring
        IAccountDao accountDao = new AccountDaoImpl();
        IMessageDao messageDao = new MessageDaoImpl();

        // Service wiring
        this.accountService = new AccountService(accountDao);
        this.messageService = new MessageService(messageDao, accountService);
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Account endpoints
        app.post("/register", this::register);
        app.post("/login", this::login);

        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessageText);

        // Messages by user
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);

        return app;
    }

    // Account Handlers
    private void register(Context ctx) {
        Account incoming = ctx.bodyAsClass(Account.class);
        Account created = accountService.register(incoming);

        if (created == null) {
            ctx.status(400);
        } else {
            ctx.json(created);
        }
    }

    private void login(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account account = accountService.login(credentials);

        if (account == null) {
            ctx.status(401);
        } else {
            ctx.json(account);
        }
    }

    // Message Handlers
    private void createMessage(Context ctx) {
        Message incoming = ctx.bodyAsClass(Message.class);
        Message created = messageService.createMessage(incoming);

        if (created == null) {
            ctx.status(400);
        } else {
            ctx.json(created);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages); // always 200
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(message);
        }
    }

    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(messageId);

        if (deleted != null) {
            ctx.json(deleted);
        }
    }

    private void updateMessageText(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message body = ctx.bodyAsClass(Message.class);

        Message updated = messageService.updateMessageText(
                messageId,
                body.getMessage_text()
        );

        if (updated == null) {
            ctx.status(400);
        } else {
            ctx.json(updated);
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.json(messages);
    }

}