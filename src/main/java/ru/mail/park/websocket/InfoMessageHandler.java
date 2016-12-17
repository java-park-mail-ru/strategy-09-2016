package ru.mail.park.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;

import javax.annotation.PostConstruct;

@Component
public class InfoMessageHandler extends MessageHandler<InfoMessageFromUser.Request> {

    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public InfoMessageHandler(@NotNull MessageHandlerContainer messageHandlerContainer) {
        super(InfoMessageFromUser.Request.class);
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(InfoMessageFromUser.Request.class, this);
    }

    @Override
    public void handle(@NotNull InfoMessageFromUser.Request message, @NotNull Long userId) throws HandleException { }
}
