package ru.mail.park.messageSystem.MessagesToGameMechanics;

import ru.mail.park.mechanics.internal.GameMechanicsInNewThread;
import ru.mail.park.messageSystem.Abonent;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.Message;

/**
 * Created by victor on 13.12.16.
 */
public abstract class MessageToGameMechanics extends Message{
    public MessageToGameMechanics(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if(abonent instanceof GameMechanicsInNewThread) {
            exec((GameMechanicsInNewThread) abonent);
        }
    }

    abstract void exec(GameMechanicsInNewThread gameMechanicsInNewThread);

}
