package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;

public interface GameMechanics {

    void addUser(@NotNull Long userId);

    void reset();
}
