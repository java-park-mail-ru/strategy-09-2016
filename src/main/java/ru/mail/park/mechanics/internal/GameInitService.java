package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.GameSession;
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

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull GameSession gameSession) {
        final Collection<UserProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst().getUserProfile());
        players.add(gameSession.getSecond().getUserProfile());
        for (UserProfile player : players) {
            final InitGame.Request initMessage = createInitMessageFor(gameSession, player.getId());
            //noinspection OverlyBroadCatchBlock
            try {
                final Message message = new Message(InitGame.Request.class.getName(),
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
