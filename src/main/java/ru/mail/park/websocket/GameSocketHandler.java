package ru.mail.park.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.mail.park.exeption.HandleException;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import javax.naming.AuthenticationException;
import java.io.IOException;


public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);

    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;

    @NotNull
    private final AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public GameSocketHandler(@NotNull AccountService accountService,
                             @NotNull MessageHandlerContainer messageHandlerContainer) {
        this.accountService = accountService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        System.out.println(userId);
        System.out.println("Connected!!");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        final Long userId = (Long) session.getAttributes().get("userId");
        System.out.println("some message recieved");
        if(userId!=null) {
            final UserProfile user = accountService.getUserById(userId);
            handleMessage(user, message);
            MessageToClient.Request testMessage = new MessageToClient.Request();
            testMessage.setMyMessage("Hello!!!");
            try {
                final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                        objectMapper.writeValueAsString(testMessage));
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseMessage)));
            } catch( Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(UserProfile userProfile, TextMessage text) {

        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at ping response", ex);
            return;
        }
        try {
            //noinspection ConstantConditions
            messageHandlerContainer.handle(message, userProfile.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getType() + " with content: " + message.getContent(), e);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("diconnected!!");
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if (userId == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}