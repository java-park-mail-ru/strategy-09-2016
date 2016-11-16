package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.requests.BoardMapForUsers;
import ru.mail.park.mechanics.requests.InitGame;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Solovyev on 03/11/2016.
 */
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

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<UserProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst().getUserProfile());
        players.add(gameSession.getSecond().getUserProfile());
        gameProgressService.createNewGame(gameSession.getFirst().getUserProfile().getId(),
                gameSession.getSecond().getUserProfile().getId());
        for (UserProfile player : players) {
            final BoardMapForUsers.Request initMessage = createGameStartMessage(gameSession, player);
            //noinspection OverlyBroadCatchBlock
            try {
                final Message message = new Message(BoardMapForUsers.Request.class.getName(),
                        objectMapper.writeValueAsString(initMessage));
                remotePointService.sendMessageToUser(player.getId(), message);
            } catch (IOException e) {
                //TODO: Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a game", e);
            }
        }
    }

    private BoardMapForUsers.Request createGameStartMessage(@NotNull GameSession gameSession,
                                                            @NotNull UserProfile player){
        BoardMapForUsers.Request request = new BoardMapForUsers.Request();
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

    @SuppressWarnings("TooBroadScope")
    private InitGame.Request createInitMessageFor(@NotNull GameSession gameSession, @NotNull Long userId) {
        final UserProfile self = gameSession.getSelf(userId).getUserProfile();
        final InitGame.Request initGameMessage = new InitGame.Request();
        final Map<Long, String> names = new HashMap<>();

        final Collection<UserProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst().getUserProfile());
        players.add(gameSession.getSecond().getUserProfile());
        for (UserProfile player : players) {
            names.put(player.getId(), player.getLogin());
        }

        initGameMessage.setSelf(userId);
        initGameMessage.setNames(names);
        return initGameMessage;
    }
}
