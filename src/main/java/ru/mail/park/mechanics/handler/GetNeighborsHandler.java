package ru.mail.park.mechanics.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.mechanics.internal.GameProgressService;
import ru.mail.park.mechanics.requests.GetNeighbors;
import ru.mail.park.websocket.MessageHandler;
import ru.mail.park.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

@Component
public class GetNeighborsHandler extends MessageHandler<GetNeighbors> {
    @NotNull
    private GameProgressService gameProgressService;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public GetNeighborsHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                               @NotNull GameProgressService gameProgressService) {
        super(GetNeighbors.class);
        this.gameProgressService = gameProgressService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        System.out.println(GetNeighbors.class);
        messageHandlerContainer.registerHandler(GetNeighbors.class, this);
    }

    @Override
    public void handle(@NotNull GetNeighbors message, @NotNull Long forUser) throws HandleException {
        System.out.println("Пытаемся получить соседей");
        System.out.println(forUser); //forUser - id юзера, который отправил сообщение
        gameProgressService.sendNeighbord(message.getCellIndex(), forUser);
    }

}
