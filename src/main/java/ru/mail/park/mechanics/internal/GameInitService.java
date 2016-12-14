package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.requests.BoardMapForUsers;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameInitService.class);

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final ObjectMapper objectMapper = new ObjectMapper();

    @NotNull
    private final GameProgressService gameProgressService;

    public GameInitService(@NotNull RemotePointService remotePointService,
                           @NotNull GameProgressService gameProgressService) {
        this.remotePointService = remotePointService;
        this.gameProgressService = gameProgressService;
    }

    public void initGameFor(UserProfile firstPlayer, UserProfile secondPLayer) {
        final Collection<UserProfile> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPLayer);
        gameProgressService.createNewGame(firstPlayer.getId(),
                secondPLayer.getId());
        GameSession gameSession = new GameSession(firstPlayer,secondPLayer);
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
        }
    }

    //рассылка стартового состояния доски игрокам
    private BoardMapForUsers.Request createGameStartMessage(@NotNull GameSession session,
                                                            @NotNull UserProfile player){
        final BoardMapForUsers.Request request = new BoardMapForUsers.Request();
        request.setGameBoard(gameProgressService.getBoardMap(player.getId()));
        request.setEnemyNick(session.getEnemy(player.getId()).getUserProfile().getLogin()); //проблема в том, что надо в общем виде различать игрока, ход которого первый
        if(session.getFirst().getUserProfile().equals(player)) {
            request.setActive(true);
        } else {
            request.setActive(false);
        }
        request.setEnemyNick(session.getEnemy(player.getId()).getUserProfile().getLogin());
        return  request;
    }

}
