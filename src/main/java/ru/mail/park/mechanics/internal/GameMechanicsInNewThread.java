package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameContent;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.utils.MovementResult;
import ru.mail.park.messageSystem.Abonent;
import ru.mail.park.messageSystem.Address;
import ru.mail.park.messageSystem.MessageSystem;
import ru.mail.park.messageSystem.MessagesToSender.InfoMessage;
import ru.mail.park.messageSystem.MessagesToSender.InitGameMessageToFront;
import ru.mail.park.messageSystem.MessagesToSender.NeighborsMessage;
import ru.mail.park.messageSystem.MessagesToSender.PiratMoveResultMessage;
import ru.mail.park.model.UserProfile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by victor on 13.12.16.
 */
@Service
public class GameMechanicsInNewThread implements Runnable, Abonent { //Новая игровая механика, которая будет жить в отдельном треде

    @NotNull
    private final Map<Long, GameContent> usersToGamesMap = new HashMap<>(); //связь юзеров и игр

    private MessageSystem ms;

    private static final long STEP_TIME = 100;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    private final Address myAddress = new Address();

    private final Address senderAddress;

    @Autowired
    public GameMechanicsInNewThread(MessageSystem ms, SenderMessageToFront senderMessageToFront) {
        this.ms = ms;
        ms.addAbonent(myAddress);
        this.senderAddress = senderMessageToFront.getAddress(); //А если двум сервисам надо будет послать сообщения друг другу?
    }

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try { //Все остальные методы вызываются при выполнении сообщений в этом цикле
                ms.execForAbonent(this);
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void createNewGame(@NotNull UserProfile firstPlayer, @NotNull UserProfile secondPlayer){
        final GameContent game = new GameContent(firstPlayer.getId(), secondPlayer.getId()); //Сообщение для игровой механики
        usersToGamesMap.put(firstPlayer.getId(), game);
        usersToGamesMap.put(secondPlayer.getId(), game); // создали игру, запомнили ее связь с пользователями
        System.out.print("игру создали. пытаемся разослать поле игрокам");
        ms.sendMessage(new InitGameMessageToFront(myAddress,senderAddress,game.getMap(),firstPlayer,secondPlayer));
    }

    public void movePirat(Integer piratId, CoordPair targetCell, Long firstPlayerId, Long secondPlayerId) {
        if (usersToGamesMap.containsKey(firstPlayerId)) {
            final List<MovementResult> result = usersToGamesMap.get(firstPlayerId).movePirat(piratId, targetCell, firstPlayerId); //Сообщение для игровой механики
            if(result==null){
                ms.sendMessage(new InfoMessage(myAddress, senderAddress, "Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки", firstPlayerId));
                // то отправить одно сообщение //testMessage.setMyMessage("Такой ход невозможен. Скорее всего, вы ошиблись в выборе клетки");
            } else {
                ms.sendMessage(new PiratMoveResultMessage(myAddress, senderAddress,result,firstPlayerId, secondPlayerId));
            }
        }
    }

    public void getNeighbor(Integer cellIndex, Long playerId){

        final Integer x = cellIndex%13;
        final Integer y = cellIndex/13;
        final CoordPair piratCord = new CoordPair(x,y);
        final CoordPair[] neighbors = usersToGamesMap.get(playerId).getNeighbors(piratCord, playerId); //Сообщение для игровой механики

        final List<Integer> neighborsList = new ArrayList<>();
        for(CoordPair cell:neighbors){
            neighborsList.add(13*cell.getY()+cell.getX());
        }

        if(CoordPair.equals(piratCord,usersToGamesMap.get(playerId).getShipCord(playerId))){
            final CoordPair[] shipNeighbors = usersToGamesMap.get(playerId).getShipAvailableDirection(playerId); //Сообщение для игровой механики
            for(CoordPair cell:shipNeighbors){
                neighborsList.add(13*(cell.getY()+piratCord.getY())+(cell.getX()+piratCord.getX()));
            }
        }

        ms.sendMessage(new NeighborsMessage(myAddress,senderAddress,neighborsList, playerId));

    }

    public void moveShip(CoordPair direction, Long playerId){

        if(usersToGamesMap.containsKey(playerId)){
            if(usersToGamesMap.get(playerId).moveShip(direction, playerId)){ //Сообщение для игровой механики
                ms.sendMessage(new InfoMessage(myAddress, senderAddress,"корабль передвинулся, но мы этого пока не увидим",playerId));
                //testMessage.setMyMessage("корабль передвинулся, но мы этого пока не увидим");
            } else {
                ms.sendMessage(new InfoMessage(myAddress, senderAddress,"Капитан, корабль не может туда плыть.",playerId));
                //testMessage.setMyMessage("Капитан, корабль не может туда плыть. Сейчас его координаты: ");
            }
        } else {
            ms.sendMessage(new InfoMessage(myAddress, senderAddress,"Этот игрок вообще не участвует в играх",playerId));
        }
    }

    @Override
    public Address getAddress(){
        return this.myAddress;
    }

}
