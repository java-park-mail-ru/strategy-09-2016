package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.MessageSystem;
import ru.mail.park.messageSystem.MessagesToGameMechanics.InitGameMessage;
import ru.mail.park.model.UserProfile;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameInitService.class);

    private Address myAddress = new Address();

    private Address gameMechanincsAddress;

    private MessageSystem ms;

    public GameInitService(@NotNull GameMechanicsInNewThread gameMechanicsInNewThread,
                           @NotNull MessageSystem messageSystem) {
        this.ms = messageSystem;
        this.gameMechanincsAddress = gameMechanicsInNewThread.getAddress();
    }

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<UserProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst().getUserProfile());
        players.add(gameSession.getSecond().getUserProfile());
        System.out.println("пытаемся начать игру");
        ms.sendMessage(new InitGameMessage(myAddress,gameMechanincsAddress,gameSession.getFirst().getUserProfile(), gameSession.getSecond().getUserProfile()));
       /* gameProgressService.createNewGame(gameSession.getFirst().getUserProfile().getId(),
                gameSession.getSecond().getUserProfile().getId());
        for (UserProfile player : players) {
            final BoardMapForUsers.Request initMessage = createGameStartMessage(gameSession, player);
            //noinspection OverlyBroadCatchBlock
            try {
                final Message message = new Message(BoardMapForUsers.Request.class.getName(),
                        objectMapper.writeValueAsString(initMessage));
                remotePointService.sendMessageToUser(player.getId(), message);
            } catch (IOException e) {
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a game", e);
            }
        } */
    }

    /*
    //рассылка стартового состояния доски игрокам
    private BoardMapForUsers.Request createGameStartMessage(@NotNull GameSession gameSession,
                                                            @NotNull UserProfile player){
        final BoardMapForUsers.Request request = new BoardMapForUsers.Request();
        request.setGameBoard(gameProgressService.getBoardMap(player.getId()));
        request.setEnemyNick(gameSession.getEnemy(player.getId()).getUserProfile().getLogin());
        if(gameSession.getFirst().getUserProfile().equals(player)) {
            request.setActive(true);
        } else {
            request.setActive(false);
        }
        request.setEnemyNick(gameSession.getEnemy(player.getId()).getUserProfile().getLogin());
        return  request;
    }
*/
}
