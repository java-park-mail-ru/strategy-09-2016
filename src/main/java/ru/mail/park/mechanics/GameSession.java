package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.model.UserProfile;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author k.solovyev
 */
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Long sessionId;
    @NotNull
    private final GameUser first;
    @NotNull
    private final GameUser second;

    public GameSession(@NotNull UserProfile user1, @NotNull UserProfile user2) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.first = new GameUser(user1,0);
        this.second =  new GameUser(user2,1);
    }

    @NotNull
    public GameUser getEnemy(@NotNull UserProfile user) {
        if (user == first.getUserProfile()) {
            return second;
        }
        if (user == second.getUserProfile()) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for game but user not participant");
    }

    @NotNull
    public GameUser getSelf(@NotNull Long userId) {
        if (first.getUserProfile().getId()==userId) {
            return first;
        }
        if (second.getUserProfile().getId()==userId) {
            return second;
        }
        throw new IllegalArgumentException("Request self for game but user not participate it");
    }

    @NotNull
    public GameUser getFirst() {
        return first;
    }

    @NotNull
    public GameUser getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSession that = (GameSession) o;

        return sessionId.equals(that.sessionId);

    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}