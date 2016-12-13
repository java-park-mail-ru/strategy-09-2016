package ru.mail.park.messageSystem.MessagesToSender;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.internal.SenderMessageToFront;
import ru.mail.park.messageSystem.Address;

import java.util.List;

/**
 * Created by victor on 13.12.16.
 */
public class NeighborsMessage extends MessageToSender {
    private List<Integer> neighborsList;

    @NotNull
    private Long playerId;

    public NeighborsMessage(Address from, Address to, List<Integer> neighborsList, @NotNull Long playerId) {
        super(from, to);
        this.neighborsList = neighborsList;
        this.playerId = playerId;
    }

    @Override
    public void exec(SenderMessageToFront senderMessageToFront){
        senderMessageToFront.sendNeighbors(neighborsList, playerId);
    }
}
