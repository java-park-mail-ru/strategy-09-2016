package ru.mail.park.messageSystem.MessagesToGameMechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.internal.GameMechanicsInNewThread;
import ru.mail.park.messageSystem.Address;

/**
 * Created by victor on 13.12.16.
 */
public class MoveShipMessage extends MessageToGameMechanics {
    @NotNull
    private CoordPair direction;

    @NotNull
    private Long playerId;

    public MoveShipMessage(Address from, Address to, @NotNull CoordPair direction, @NotNull Long playerId) {
        super(from, to);
        this.direction = direction;
        this.playerId = playerId;
    }

    @Override
    public void exec(GameMechanicsInNewThread gameMechanicsInNewThread){
        gameMechanicsInNewThread.moveShip(direction, playerId);
    }
}
