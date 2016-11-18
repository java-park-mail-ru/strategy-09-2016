package ru.mail.park.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;

import java.io.IOException;

@Component
public abstract class MessageHandler<T> {

    private final @NotNull Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, @NotNull Long userId) throws HandleException {
        try {
            final Object data = new ObjectMapper().readValue(message.getContent(), clazz);
            handle(clazz.cast(data), userId);
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " + message.getContent(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Long forUser) throws HandleException;
}
