package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by victor on 13.11.16.
 */
public class GameMeshanicsImpl implements GameMechanics {
    @NotNull
    private AccountService accountService;
    @NotNull
    private RemotePointService remotePointService;
    @NotNull
    private Set<Long> playingUsers = new HashSet<>();
    @NotNull
    private ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();

    @Override
    public void addUser(@NotNull Long userId) {
        waiters.add(userId);
        tryStartGame();
    }

    public void tryStartGame(){
        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();
        while(waiters.size()>=1){ //пока в списке желающих сыграть больше 1 человека
            Long candidateId = waiters.poll(); //достаем желающего из очереди
            if (!insureCandidate(candidateId)) { //если он еще может игрыть
                continue;
            }
            matchedPlayers.add(accountService.getUserById(candidateId)); //добавляем его в набор игроков на следующую игру
            if(matchedPlayers.size() == 2) { //если таких набралось двое, то у них начинается игра
                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
                //gameSessionService.startGame(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add); //если у нас остался один
        //неприкаянный желающий сыграть, возвращаем его обратно в очередь
    }

    private boolean insureCandidate(@NotNull Long candidate) { //проверяем, может ли
        return remotePointService.isConnected(candidate) && //этот игрок участвовать в игре
                accountService.getUserById(candidate) != null;
    }


    @Override
    public void reset() {

    }
}
