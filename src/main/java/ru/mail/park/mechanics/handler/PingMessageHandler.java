package ru.mail.park.mechanics.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.mechanics.requests.GetPingMessage;
import ru.mail.park.mechanics.internal.PingService;
import ru.mail.park.websocket.MessageHandler;
import ru.mail.park.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

@Component
public class PingMessageHandler extends MessageHandler<GetPingMessage.Request> {


    private @NotNull MessageHandlerContainer messageHandlerContainer;

    private @NotNull PingService pingService;

    public PingMessageHandler(@NotNull PingService pingService
            ,
                              @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(GetPingMessage.Request.class);
        this.pingService = pingService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(GetPingMessage.Request.class, this);
    }

    @Override
    public void handle(@NotNull GetPingMessage.Request message, @NotNull Long forUser) throws HandleException {
        pingService.sendPingResponse(forUser);
    }
}
