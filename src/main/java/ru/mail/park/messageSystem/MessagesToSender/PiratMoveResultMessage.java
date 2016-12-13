package ru.mail.park.messageSystem.MessagesToSender;

import ru.mail.park.mechanics.internal.SenderMessageToFront;
import ru.mail.park.mechanics.utils.MovementResult;
import ru.mail.park.messageSystem.Address;

import java.util.List;

/**
 * Created by victor on 13.12.16.
 */
public class PiratMoveResultMessage extends MessageToSender {
    private List<MovementResult> movementResults;

    private Long activePlayerId;

    private Long passivePLayerId;

    public PiratMoveResultMessage(Address from, Address to, List<MovementResult> movementResults, Long activePlayerId, Long passivePLayerId) {
        super(from, to);
        this.movementResults = movementResults;
        this.activePlayerId = activePlayerId;
        this.passivePLayerId = passivePLayerId;
    }

    @Override
    public void exec(SenderMessageToFront senderMessageToFront){
        senderMessageToFront.piratMove(movementResults, activePlayerId, passivePLayerId);
    }
}
