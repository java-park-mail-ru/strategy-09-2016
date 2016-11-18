package ru.mail.park.websocket;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.exeption.HandleException;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Long userId) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
