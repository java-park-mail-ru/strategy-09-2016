package ru.mail.park.messageSystem.MessagesToGameMechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.internal.GameMechanicsInNewThread;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.model.UserProfile;

/**
 * Created by victor on 13.12.16.
 */
public class InitGameMessage extends MessageToGameMechanics{
    @NotNull
    private UserProfile firstPlayer;

    @NotNull
    private UserProfile secondPlayer;

    public InitGameMessage(Address from, Address to, @NotNull UserProfile firstPlayerId, @NotNull UserProfile secondPlayerId) {
        super(from, to);
        this.firstPlayer = firstPlayerId;
        this.secondPlayer = secondPlayerId;
    }

    public void exec(GameMechanicsInNewThread gameMechanicsInNewThread){
        gameMechanicsInNewThread.createNewGame(firstPlayer, secondPlayer);
    }
}
