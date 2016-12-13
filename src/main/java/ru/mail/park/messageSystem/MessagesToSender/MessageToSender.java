package ru.mail.park.messageSystem.MessagesToSender;

import ru.mail.park.mechanics.internal.SenderMessageToFront;
import ru.mail.park.messageSystem.Abonent;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.Message;

/**
 * Created by victor on 13.12.16.
 */
public abstract class MessageToSender extends Message{
    public MessageToSender(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if(abonent instanceof SenderMessageToFront) {
            exec((SenderMessageToFront) abonent);
        }
    }

    abstract void exec(SenderMessageToFront senderMessageToFront);
}
