package ru.mail.park.messageSystem.MessagesToGameMechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.internal.GameMechanicsInNewThread;
import ru.mail.park.messageSystem.Address;

/**
 * Created by victor on 13.12.16.
 */
public class GetNeighborsMessage extends  MessageToGameMechanics {
    @NotNull
    private Integer targetCellIndex;

    @NotNull
    private Long playerId;

    public GetNeighborsMessage(Address from, Address to, @NotNull Integer targetCellIndex, @NotNull Long playerId) {
        super(from, to);
        this.targetCellIndex = targetCellIndex;
        this.playerId = playerId;
    }

    @Override
    public void exec(GameMechanicsInNewThread gameMechanicsInNewThread){
        gameMechanicsInNewThread.getNeighbor(targetCellIndex, playerId);
    }
}
