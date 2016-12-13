package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameContent;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.MessageSystem;
import ru.mail.park.messageSystem.MessagesToGameMechanics.GetNeighborsMessage;
import ru.mail.park.messageSystem.MessagesToGameMechanics.MovePiratMessage;
import ru.mail.park.messageSystem.MessagesToGameMechanics.MoveShipMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 14.11.16.
 */
@Service
public class GameProgressService {

    @NotNull
    private final Map<Long, GameContent> usersToGamesMap = new HashMap<>(); //связь юзеров и игр

    @NotNull
    private final GameSessionService gameSessionService; //сервис, который помнит сессий и юзеров

    private Address myAddress = new Address();

    private Address gameMechanincsAddress;

    private MessageSystem ms;

    public GameProgressService(@NotNull GameSessionService gameSessionService,
                               @NotNull GameMechanicsInNewThread gameMechanicsInNewThread,
                               @NotNull MessageSystem ms){
        this.gameSessionService = gameSessionService;
        this.gameMechanincsAddress = gameMechanicsInNewThread.getAddress();
        this.ms = ms;
    }

    public void createNewGame(@NotNull Long firstPlayerId, @NotNull Long secondPlayerId){
        final GameContent game = new GameContent(firstPlayerId, secondPlayerId); //Сообщение для игровой механики
        usersToGamesMap.put(firstPlayerId, game);
        usersToGamesMap.put(secondPlayerId, game); // создали игру, запомнили ее связь с пользователями
    }

    @Nullable
    public String getBoardMap(Long playerId){
        if(usersToGamesMap.containsKey(playerId)) {
            return usersToGamesMap.get(playerId).getMap(); //Сообщение для игровой механики
        } else {
            return null;
        }
    }

    public void movePirat(Integer piratId, CoordPair targetCell, Long playerId){

        ms.sendMessage(new MovePiratMessage(myAddress, gameMechanincsAddress,piratId,targetCell, playerId,
                gameSessionService.getSessionForUser(playerId).getEnemy(playerId).getUserProfile().getId()));
        /*
        final MessageToClient.Request testMessage = new MessageToClient.Request(); //вещь для отладки
        // если возник какой-то рассинхрон между фронтом и беком
        System.out.println("получили сообщение от фронта, готовимся двигать пирата");
        if(usersToGamesMap.containsKey(playerId)){
            final List<MovementResult> result = usersToGamesMap.get(playerId).movePirat(piratId, targetCell, playerId); //Сообщение для игровой механики
            if(result==null){
                testMessage.setMyMessage("Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки");
            } else {
                sendUserNewBoard(result, playerId);
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
        } */
    }

    public void sendNeighbord(Integer cellIndex, Long playerId){

        ms.sendMessage(new GetNeighborsMessage(myAddress, gameMechanincsAddress,cellIndex, playerId));
        /*
        final Integer x = cellIndex%13;
        final Integer y = cellIndex/13;
        //System.out.println("Мы получаем соседей клетки с координатами" + x + ' ' + y);
        //мы не успели разобраться, как JSON-ить массив целых чисел
        final CoordPair piratCord = new CoordPair(x,y);
        final CoordPair[] neighbors = usersToGamesMap.get(playerId).getNeighbors(piratCord, playerId); //Сообщение для игровой механики
        //final StringBuilder builder = new StringBuilder();
        final List<Integer> neighborsList = new ArrayList<>();
        for(CoordPair cell:neighbors){
            neighborsList.add(13*cell.getY()+cell.getX());
        //    builder.append(13*cell.getY()+cell.getX()); //пересчет (х,у) координат, в которых работает сервер
        //    builder.append(','); // в одномерный индекс, в котором работает фронт
        }

        if(CoordPair.equals(piratCord,usersToGamesMap.get(playerId).getShipCord(playerId))){
            final CoordPair[] shipNeighbors = usersToGamesMap.get(playerId).getShipAvailableDirection(playerId); //Сообщение для игровой механики
            for(CoordPair cell:shipNeighbors){
                neighborsList.add(13*(cell.getY()+piratCord.getY())+(cell.getX()+piratCord.getX()));
         //       builder.append(13*(cell.getY()+piratCord.getY())+(cell.getX()+piratCord.getX())); //пересчет (х,у) координат, в которых работает сервер
         //       builder.append(','); // в одномерный индекс, в котором работает фронт
            }
        }
        //builder.setLength(builder.length()-1);

        final NeighborsMessage.Request messageWithNeighbors = new NeighborsMessage.Request();
        messageWithNeighbors.setNeighbors(new Gson().toJson(neighborsList));
        try{
            final Message responseMessage = new Message(NeighborsMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(messageWithNeighbors));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( IOException e){
            e.printStackTrace();
        } */
    }

    public void moveShip(CoordPair direction, Long playerId){
        ms.sendMessage(new MoveShipMessage(myAddress, gameMechanincsAddress,direction,playerId));
        /*
        final MessageToClient.Request testMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            if(usersToGamesMap.get(playerId).moveShip(direction, playerId)){ //Сообщение для игровой механики
                //sendUserNewBoard(playerId);//то рассылаем игрока сообщение о движении корабля
                //sendUserShipMovementResult(playerId);
                testMessage.setMyMessage("корабль передвинулся, но мы этого пока не увидим");
                //return;
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
        } */
    }
/*
    private void sendUserNewBoard(List<MovementResult> movementResults, Long playerId){
        final PiratMoveMessage.Request newTurnMessage = new PiratMoveMessage.Request();
        newTurnMessage.setActive(false);
        newTurnMessage.setMovement(new Gson().toJson(movementResults));
//        newTurnMessage.setPlayerId(usersToGamesMap.get(playerId).gameUserIdToGameUserId(playerId));
//        newTurnMessage.setPiratId(piratId);
 //       newTurnMessage.setNewCellIndexOfPirat(indexOfTargetCell);
        try {
            //System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто ходил");
            final Message responseMessageToActivePLayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(playerId,responseMessageToActivePLayer);
        } catch( IOException e){
            e.printStackTrace();
        }
        try {
            System.out.println("Пират передвинут. Эй, фронт, лови сообщение для того, кто будет ходить");
            newTurnMessage.setActive(true);
            final Message responseMessageToPassivePlayer = new Message(PiratMoveMessage.Request.class.getName(),
                    objectMapper.writeValueAsString(newTurnMessage));
            remotePointService.sendMessageToUser(
                    gameSessionService.getSessionForUser(playerId).getEnemy(playerId).getUserProfile().getId(),
                    responseMessageToPassivePlayer);//надо переделать sessionService, чтобы не было таких цепочек
        } catch( IOException e){
            e.printStackTrace();
        }
    } */


}
