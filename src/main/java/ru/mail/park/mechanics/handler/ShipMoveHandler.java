package ru.mail.park.mechanics.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.internal.GameProgressService;
import ru.mail.park.mechanics.requests.ShipMoveRequest;
import ru.mail.park.websocket.MessageHandler;
import ru.mail.park.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

@Component
public class ShipMoveHandler extends MessageHandler<ShipMoveRequest> { //движение кобрабля в игре пока не используется

    private @NotNull GameProgressService gameProgressService;

    private @NotNull MessageHandlerContainer messageHandlerContainer;

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
        gameProgressService.moveShip(new CoordPair(message.getDirectionX(), message.getDirectionY()), forUser);

    }
}
