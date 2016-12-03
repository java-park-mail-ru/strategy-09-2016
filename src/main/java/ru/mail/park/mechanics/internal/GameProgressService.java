package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameContent;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.requests.NeighborsMessage;
import ru.mail.park.mechanics.requests.PiratMoveMessage;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.MessageToClient;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 14.11.16.
 */
@Service
public class GameProgressService {

    private final @NotNull Map<Long, GameContent> usersToGamesMap = new HashMap<>(); //связь юзеров и игр

    private final @NotNull RemotePointService remotePointService; // сервис, рассылающие сообщения юзерам

    private final @NotNull GameSessionService gameSessionService; //сервис, который помнит сессий и юзеров

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameProgressService(@NotNull RemotePointService remotePointService,
                               @NotNull GameSessionService gameSessionService){
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
    }

    public void createNewGame(@NotNull Long firstPlayerId, @NotNull Long secondPlayerId){
        final GameContent game = new GameContent(firstPlayerId, secondPlayerId);
        usersToGamesMap.put(firstPlayerId, game);
        usersToGamesMap.put(secondPlayerId, game); // создали игру, запомнили ее связь с пользователями
    }

    public @Nullable String getBoardMap(Long playerId){
        if(usersToGamesMap.containsKey(playerId)) {
            return usersToGamesMap.get(playerId).getMap();
        } else {
            return null;
        }
    }

    public void movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        final MessageToClient.Request testMessage = new MessageToClient.Request(); //вещь для отладки
        // если возник какой-то рассинхрон между фронтом и беком
        if(usersToGamesMap.containsKey(playerId)){
            final Boolean result = usersToGamesMap.get(playerId).movePirat(piratId, targetCell, playerId);
            if(!result){
                testMessage.setMyMessage("Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки");
            } else {
                sendUserNewBoard(playerId, piratId, 13*targetCell.getY()+targetCell.getX());
                return;
            }
        } else {
            testMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(testMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    public void sendNeighbord(Integer cellIndex, Long playerId){

        final Integer x = cellIndex%13;
        final Integer y = cellIndex/13;
        //System.out.println("Мы получаем соседей клетки с координатами" + x + ' ' + y);
        //мы не успели разобраться, как JSON-ить массив целых чисел
        CoordPair piratCord = new CoordPair(x,y);
        final CoordPair[] neighbors = usersToGamesMap.get(playerId).getNeighbors(piratCord, playerId);
        final StringBuilder builder = new StringBuilder();
        for(CoordPair cell:neighbors){
            builder.append(13*cell.getY()+cell.getX()); //пересчет (х,у) координат, в которых работает сервер
            builder.append(','); // в одномерный индекс, в котором работает фронт
        }

        if(CoordPair.equals(piratCord,usersToGamesMap.get(playerId).getShipCord(playerId))){
            final CoordPair[] shipNeighbors = usersToGamesMap.get(playerId).getShipAvailableDirection(playerId);
            System.out.println(shipNeighbors.length);
            for(CoordPair cell:shipNeighbors){
                builder.append(13*(cell.getY()+piratCord.getY())+(cell.getX()+piratCord.getX())); //пересчет (х,у) координат, в которых работает сервер
                builder.append(','); // в одномерный индекс, в котором работает фронт
            }
        }
        builder.setLength(builder.length()-1);

        final NeighborsMessage.Request messageWithNeighbors = new NeighborsMessage.Request();
        messageWithNeighbors.setNeighbors(builder.toString());
        try{
            final Message responseMessage = new Message(NeighborsMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(messageWithNeighbors));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    public void moveShip(CoordPair direction, Long playerId){
        final MessageToClient.Request testMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            if(usersToGamesMap.get(playerId).moveShip(direction, playerId)){
                //sendUserNewBoard(playerId);то рассылаем игрока сообщение о движении корабля
                return;
            } else {
                testMessage.setMyMessage("Капитан, корабль не может туда плыть. Сейчас его координаты: ");
                //System.out.println(usersToGamesMap.get(playerId).getShipCord(playerId));
            }
        } else {
            testMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(testMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        }
    }

    private void sendUserNewBoard(Long playerId, Integer piratId, Integer indexOfTargetCell){
        final PiratMoveMessage.Request newTurnMessage = new PiratMoveMessage.Request();
        newTurnMessage.setActive(false);
        newTurnMessage.setPlayerId(usersToGamesMap.get(playerId).gameUserIdToGameUserId(playerId));
        newTurnMessage.setPiratId(piratId);
        newTurnMessage.setNewCellIndexOfPirat(indexOfTargetCell);
        try {
            //System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто ходил");
            final Message responseMessageToActivePLayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(playerId,responseMessageToActivePLayer);
        } catch( IOException e){
            e.printStackTrace();
        }
        try {
            //System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто будет ходить");
            newTurnMessage.setActive(true);
            final Message responseMessageToPassivePlayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(
                    gameSessionService.getSessionForUser(playerId).getEnemy(playerId).getUserProfile().getId(),
                    responseMessageToPassivePlayer);//надо переделать sessionService, чтобы не было таких цепочек
        } catch( IOException e){
            e.printStackTrace();
        }
    }


}
