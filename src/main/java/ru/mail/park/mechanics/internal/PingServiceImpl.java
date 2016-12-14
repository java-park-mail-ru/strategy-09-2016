package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.requests.ReplyPingMessage;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.RemotePointService;

/**
 * Created by victor on 03.12.16.
 */
@Service
public class PingServiceImpl implements  PingService{ //просто отвечалка на пинги от фронта, ничего содержательного

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PingServiceImpl.class);

    @NotNull
    private RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PingServiceImpl(@NotNull RemotePointService remotePointService){
        this.remotePointService = remotePointService;
    }

    @Override
    public void sendPingResponse(Long forUser){
        final ReplyPingMessage.Request replyPingMessage = new ReplyPingMessage.Request();
        replyPingMessage.setPingMessage("Up. Got ur message");
        try {
            final Message pingMessage = new Message(ReplyPingMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(replyPingMessage));
            remotePointService.sendMessageToUser(forUser, pingMessage);
        } catch( Exception e){
            LOGGER.error("Can't send ping message to user",e);
        }
    }

}
