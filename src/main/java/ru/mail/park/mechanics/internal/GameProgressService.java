package ru.mail.park.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.game.CoordPair;
import ru.mail.park.mechanics.GameContent;
import ru.mail.park.mechanics.requests.BoardMapAfterTurn;
import ru.mail.park.websocket.Message;
import ru.mail.park.websocket.MessageToClient;
import ru.mail.park.websocket.RemotePointService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 14.11.16.
 */
@Service
public class GameProgressService {
    @NotNull
    private final Map<Long, GameContent> usersToGamesMap = new HashMap<>(); //связь айдишников пользователей
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final GameSessionService gameSessionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameProgressService(@NotNull RemotePointService remotePointService,
                               @NotNull GameSessionService gameSessionService){
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
    }

    public void createNewGame(@NotNull Long firstPlayerId, @NotNull Long secondPlayerId){
        GameContent game = new GameContent(firstPlayerId, secondPlayerId);
        usersToGamesMap.put(firstPlayerId, game);
        usersToGamesMap.put(secondPlayerId, game); // создали игру, запомнили ее связь с пользователями
    }

    public String getBoardMap(Long playerId){
        if(usersToGamesMap.containsKey(playerId)) {
            return usersToGamesMap.get(playerId).getMap();
        } else {
            return null;
        }
    }

    public void movePirat(Integer piratId, CoordPair targetCell, Long playerId){
        MessageToClient.Request testMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            Boolean result = usersToGamesMap.get(playerId).movePirat(piratId, targetCell, playerId);
            if(!result){
                testMessage.setMyMessage("Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки");
            } else {
                sendUserNewBoard(playerId);
                return;
            }
        } else {
            testMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(testMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( Exception e){
            e.printStackTrace();
        }
    }

    public void moveShip(CoordPair direction, Long playerId){
        MessageToClient.Request testMessage = new MessageToClient.Request();
        if(usersToGamesMap.containsKey(playerId)){
            if(usersToGamesMap.get(playerId).moveShip(direction, playerId)){
                sendUserNewBoard(playerId);
                return;
            } else {
                testMessage.setMyMessage("Капитан, корабль не может туда плыть");
            }
        } else {
            testMessage.setMyMessage("Этот игрок вообще не участвует в играх");
        }
        try {
            final Message responseMessage = new Message(MessageToClient.Request.class.getName(),
                    objectMapper.writeValueAsString(testMessage));
            remotePointService.sendMessageToUser(playerId,responseMessage);
        } catch( Exception e){
            e.printStackTrace();
        }

    }

    private void sendUserNewBoard(Long playerId){
        BoardMapAfterTurn.Request boardChanged = new BoardMapAfterTurn.Request();
        boardChanged.setGameBoard(usersToGamesMap.get(playerId).getMap());
        boardChanged.setActive(false);
        try {
            final Message responseMessageToActivePLayer = new Message(BoardMapAfterTurn.Request.class.getName(),
                    objectMapper.writeValueAsString(boardChanged));
            remotePointService.sendMessageToUser(playerId,responseMessageToActivePLayer);
        } catch( Exception e){
            e.printStackTrace();
        }
        try {
            boardChanged.setActive(true);
            final Message responseMessageToPassivePlayer = new Message(BoardMapAfterTurn.Request.class.getName(),
                    objectMapper.writeValueAsString(boardChanged));
            remotePointService.sendMessageToUser(
                    gameSessionService.getSessionForUser(playerId).getEnemy(playerId).getUserProfile().getId(),
                    responseMessageToPassivePlayer);
        } catch( Exception e){
            e.printStackTrace();
        }
    }


}
