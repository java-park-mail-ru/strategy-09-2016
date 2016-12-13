package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.RemotePointService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class GameSessionService {
    @NotNull
    private final Map<Long, GameSession> usersMap = new HashMap<>();
    @NotNull
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();
    @NotNull
    private final RemotePointService remotePointService;
    public GameSessionService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    @Nullable
    public GameSession getSessionForUser(@NotNull Long userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(@NotNull Long userId) {
        return usersMap.containsKey(userId);
    }

    public void notifyGameIsOver(@NotNull GameSession gameSession) {
        final boolean exists = gameSessions.remove(gameSession);
        usersMap.remove(gameSession.getFirst().getUserProfile().getId());
        usersMap.remove(gameSession.getSecond().getUserProfile().getId());
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getFirst().getUserProfile().getId(), CloseStatus.SERVER_ERROR);
            remotePointService.cutDownConnection(gameSession.getSecond().getUserProfile().getId(), CloseStatus.SERVER_ERROR);
        }
    }

    public GameSession startGame(@NotNull UserProfile first, @NotNull UserProfile second) {
        final GameSession gameSession = new GameSession(first, second);
        gameSessions.add(gameSession);
        usersMap.put(gameSession.getFirst().getUserProfile().getId(), gameSession);
        usersMap.put(gameSession.getSecond().getUserProfile().getId(), gameSession);
        //начинаеми игру
        return gameSession;
    }

}
