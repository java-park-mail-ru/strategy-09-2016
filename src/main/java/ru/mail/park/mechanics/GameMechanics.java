package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;

public interface GameMechanics {
    //void addClientSnapshot(@NotNull Long userId, @NotNull ClientSnap clientSnap);
    void addUser(@NotNull Long userId);

//    void gmStep(long frameTime);

    void reset();
}
