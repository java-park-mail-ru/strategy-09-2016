package ru.mail.park.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.mail.park.exeption.HandleException;

import javax.annotation.PostConstruct;

@Component
public class TestMessageHandler extends MessageHandler<TestMessage.Request> {

    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public TestMessageHandler(@NotNull MessageHandlerContainer messageHandlerContainer) {
        super(TestMessage.Request.class);
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(TestMessage.Request.class, this);
    }

    @Override
    public void handle(@NotNull TestMessage.Request message, @NotNull Long userId) throws HandleException {
        //System.out.println("I handle your message. What's next?");

    }
}
