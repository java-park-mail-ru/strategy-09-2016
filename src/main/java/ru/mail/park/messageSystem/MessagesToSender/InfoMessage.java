package ru.mail.park.messageSystem.MessagesToSender;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.internal.SenderMessageToFront;
import ru.mail.park.messageSystem.Address;

/**
 * Created by victor on 13.12.16.
 */
public class InfoMessage extends MessageToSender{
    private String messageContent;

    @NotNull
    private Long playerId;

    public InfoMessage(Address from, Address to, String messageContent, @NotNull Long playerId) {
        super(from, to);
        this.messageContent = messageContent;
        this.playerId = playerId;
    }

    @Override
    public void exec(SenderMessageToFront senderMessageToFront){
        senderMessageToFront.infoMessage(messageContent, playerId);
    }
}
