package ru.mail.park.mechanics.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.game.CoordPair;
import ru.mail.park.mechanics.internal.GameProgressService;
import ru.mail.park.mechanics.requests.PiratMoveRequest;
import ru.mail.park.websocket.MessageHandler;
import ru.mail.park.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by victor on 14.11.16.
 */

@Component
public class PiratMoveHandler extends MessageHandler<PiratMoveRequest> {
    @NotNull
    private GameProgressService gameProgressService;
    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public PiratMoveHandler(@NotNull GameProgressService gameProgressService,
                            @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(PiratMoveRequest.class);
        this.gameProgressService = gameProgressService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        System.out.println(PiratMoveRequest.class);
        messageHandlerContainer.registerHandler(PiratMoveRequest.class, this);
    }

    @Override
    public void handle(@NotNull PiratMoveRequest message, @NotNull Long forUser) throws HandleException {
        System.out.println("Хо-хо, прират собрался в путешествие");
        System.out.println(forUser); //forUser - id юзера, который отправил сообщение
        gameProgressService.movePirat(message.getPiratId(),
                new CoordPair(message.getTargetCellX(), message.getTargetCellY()), forUser);
        //типо к нам пришел ход от юзера, пора работать
        //надо вытащить параметры реквеста и скормить их gameProgressService
        //а тот уже, в своб очередь отпишется игрокам об изменениях на поле
    }
}
