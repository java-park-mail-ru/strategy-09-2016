package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.UserProfile;

public class GameUser {

    private @NotNull UserProfile userProfile;

    private @NotNull Integer inGameId;

    public GameUser(@NotNull UserProfile userProfile, @NotNull Integer inGameId) {
        this.userProfile = userProfile;
        this.inGameId = inGameId;
    }

    public @NotNull UserProfile getUserProfile() {
        return userProfile;
    }

    public Integer getInGameId() {
        return inGameId;
    }
}
