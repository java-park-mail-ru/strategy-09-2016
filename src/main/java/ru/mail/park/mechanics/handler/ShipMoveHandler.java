package ru.mail.park.mechanics.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.game.CoordPair;
import ru.mail.park.mechanics.internal.GameProgressService;
import ru.mail.park.mechanics.requests.ShipMoveRequest;
import ru.mail.park.websocket.MessageHandler;
import ru.mail.park.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 14.11.16.
 */
@Component
public class ShipMoveHandler extends MessageHandler<ShipMoveRequest> {
    @NotNull
    private GameProgressService gameProgressService;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public ShipMoveHandler(@NotNull GameProgressService gameProgressService,
                           @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ShipMoveRequest.class);
        this.gameProgressService = gameProgressService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ShipMoveRequest.class, this);
    }

    @Override
    public void handle(@NotNull ShipMoveRequest message, @NotNull Long forUser) throws HandleException {
        //тут тоже надо пнуть прогресс-гейм-сервис, чтобы передвинул корабль и отослал игрокам новое поле
        System.out.println("Поднять якорь!");
        System.out.println(forUser); //forUser - id юзера, который отправил сообщение
        gameProgressService.moveShip(new CoordPair(message.getDirectionX(), message.getDirectionY()), forUser);

    }
}
