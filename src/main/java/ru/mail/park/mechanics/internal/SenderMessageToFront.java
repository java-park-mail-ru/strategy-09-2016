package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.requests.BoardMapForUsers;
import ru.mail.park.mechanics.requests.NeighborsMessage;
import ru.mail.park.mechanics.requests.PiratMoveMessage;
import ru.mail.park.mechanics.requests.ReplyPingMessage;
import ru.mail.park.mechanics.utils.MovementResult;
import ru.mail.park.messageSystem.Abonent;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.MessageSystem;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.MessageToClient;
import ru.mail.park.websocket.RemotePointService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by victor on 13.12.16.
 */
@Service
public class SenderMessageToFront implements  Runnable, Abonent{
    private MessageSystem ms; //класс, который будет слать сообщения фронту

    private static final long STEP_TIME = 100;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    private final Address myAddress = new Address();

    @NotNull
    private final RemotePointService remotePointService; // сервис, рассылающие сообщения юзерам

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SenderMessageToFront(MessageSystem ms, RemotePointService remotePointService) {
        this.ms = ms;
        ms.addAbonent(myAddress);
        this.remotePointService = remotePointService;
    }

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while (true) try {
            ms.execForAbonent(this);
            Thread.sleep(STEP_TIME);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void infoMessage(String messageContent, Long playerId) {
        final MessageToClient.Request testMessage = new MessageToClient.Request(); //вещь для отладки
        testMessage.setMyMessage(messageContent);
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(testMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    public void sendNeighbors(List<Integer> neighborsList, Long playerId) {
        final NeighborsMessage.Request messageWithNeighbors = new NeighborsMessage.Request();
        messageWithNeighbors.setNeighbors(new Gson().toJson(neighborsList));
        try{
            final Message responseMessage = new Message(NeighborsMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(messageWithNeighbors));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    public void piratMove(List<MovementResult> movementResults, Long activePlayerId, Long passivePlayerId){
        final PiratMoveMessage.Request newTurnMessage = new PiratMoveMessage.Request();
        newTurnMessage.setActive(false);
        newTurnMessage.setMovement(new Gson().toJson(movementResults));
        try {
            //System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто ходил");
            final Message responseMessageToActivePLayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(activePlayerId,responseMessageToActivePLayer);
        } catch( IOException e){
            e.printStackTrace();
        }
        try {
            //System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто будет ходить");
            newTurnMessage.setActive(true);
            final Message responseMessageToPassivePlayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(passivePlayerId, responseMessageToPassivePlayer);//надо переделать sessionService, чтобы не было таких цепочек
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    public void initGame(String BoardMap, UserProfile firstPLayer, UserProfile secondPlayer){

        final BoardMapForUsers.Request requestToFirstPlayer = new BoardMapForUsers.Request();
        requestToFirstPlayer.setGameBoard(BoardMap);
        requestToFirstPlayer.setEnemyNick(secondPlayer.getLogin());
        requestToFirstPlayer.setActive(true);

        final BoardMapForUsers.Request requestToSecondPlayer = new BoardMapForUsers.Request();
        requestToSecondPlayer.setGameBoard(BoardMap);
        requestToSecondPlayer.setEnemyNick(secondPlayer.getLogin());
        requestToSecondPlayer.setActive(false);
        //noinspection OverlyBroadCatchBlock
        try {
            final Message messageToFirst = new Message(BoardMapForUsers.Request.class.getName(),
                    objectMapper.writeValueAsString(requestToFirstPlayer));
            remotePointService.sendMessageToUser(firstPLayer.getId(), messageToFirst);

            final Message messageToSecond = new Message(BoardMapForUsers.Request.class.getName(),
                    objectMapper.writeValueAsString(requestToSecondPlayer));
            remotePointService.sendMessageToUser(secondPlayer.getId(), messageToSecond);
            System.out.println("разослали игровое поле");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPingResponse(Long forUser){
        final ReplyPingMessage.Request replyPingMessage = new ReplyPingMessage.Request();
        replyPingMessage.setPingMessage("Up. Got ur message");
        try {
            final Message pingMessage = new Message(ReplyPingMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(replyPingMessage));
            remotePointService.sendMessageToUser(forUser, pingMessage);
        } catch( Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Address getAddress(){
        return this.myAddress;
    }
}
