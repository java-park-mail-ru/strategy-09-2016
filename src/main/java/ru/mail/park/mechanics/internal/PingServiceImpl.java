package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.MessageSystem;
import ru.mail.park.messageSystem.MessagesToSender.PingReplyMessage;
import ru.mail.park.websocket.RemotePointService;

/**
 * Created by victor on 03.12.16.
 */
@Service
public class PingServiceImpl implements  PingService{ //просто отвечалка на пинги от фронта, ничего содержательного

    private final Address myAddress = new Address();

    private Address senderAddress;

    private MessageSystem ms;

    @NotNull
    private RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PingServiceImpl(@NotNull RemotePointService remotePointService,
                           MessageSystem messageSystem, SenderMessageToFront sender){
        this.remotePointService = remotePointService;
        this.senderAddress = sender.getAddress();
        this.ms = messageSystem;
    }

    @Override
    public void sendPingResponse(Long forUser){
        ms.sendMessage(new PingReplyMessage(myAddress, senderAddress,forUser));
        /*
        final ReplyPingMessage.Request replyPingMessage = new ReplyPingMessage.Request();
        replyPingMessage.setPingMessage("Up. Got ur message");
        try {
            final Message pingMessage = new Message(ReplyPingMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(replyPingMessage));
            remotePointService.sendMessageToUser(forUser, pingMessage);
        } catch( Exception e){
            e.printStackTrace();
        }*/
    }

}
