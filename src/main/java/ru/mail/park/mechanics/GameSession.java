package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.model.UserProfile;

import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private final Long sessionId;

    @NotNull
    private final GameUser first; //есть сомнения, что нам нужен сам GameUser в его нынешнем виде

    @NotNull
    private final GameUser second; //скорее всего в него стоило бы вытащить только логин и айдишник



    public GameSession(@NotNull UserProfile user1, @NotNull UserProfile user2) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.first = new GameUser(user1,0);
        this.second =  new GameUser(user2,1);
    }


    public @NotNull GameUser getEnemy(@NotNull Long playerId) {
        if (playerId.equals(first.getUserProfile().getId())) {
            return second;
        }
        if (playerId.equals(second.getUserProfile().getId())) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for game but user not participant");
    }


    public @NotNull GameUser getSelf(@NotNull Long userId) {
        if (userId.equals(first.getUserProfile().getId())) {
            return first;
        }
        if (userId.equals(second.getUserProfile().getId())) {
            return second;
        }
        throw new IllegalArgumentException("Request self for game but user not participate it");
    }

    public @NotNull GameUser getFirst() {
        return first;
    }

    public @NotNull GameUser getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession that = (GameSession) o;

        return sessionId.equals(that.sessionId);

    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
