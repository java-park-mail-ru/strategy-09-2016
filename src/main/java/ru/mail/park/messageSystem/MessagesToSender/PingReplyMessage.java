package ru.mail.park.messageSystem.MessagesToSender;

import ru.mail.park.mechanics.internal.SenderMessageToFront;
import ru.mail.park.messageSystem.Address;

/**
 * Created by victor on 13.12.16.
 */
public class PingReplyMessage extends  MessageToSender {
    private Long playerId;

    public PingReplyMessage(Address from, Address to, Long playerId) {
        super(from, to);
        this.playerId = playerId;
    }

    @Override
    public void exec(SenderMessageToFront senderMessageToFront){
        senderMessageToFront.sendPingResponse(playerId);
    }
}
