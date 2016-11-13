package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.UserProfile;

/**
 * Created by victor on 13.11.16.
 */
public class GameUser {
    @NotNull
    private UserProfile userProfile;

    @NotNull
    private Integer inGameId;

    public GameUser(@NotNull UserProfile userProfile, @NotNull Integer inGameId) {
        this.userProfile = userProfile;
        this.inGameId = inGameId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Integer getInGameId() {
        return inGameId;
    }
}
