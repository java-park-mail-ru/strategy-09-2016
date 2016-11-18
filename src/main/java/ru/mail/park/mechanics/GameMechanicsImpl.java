package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.internal.GameInitService;
import ru.mail.park.mechanics.internal.GameSessionService;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanicsImpl implements GameMechanics {

    private @NotNull AccountService accountService;

    private @NotNull RemotePointService remotePointService;

    private @NotNull GameSessionService gameSessionService;

    private final @NotNull GameInitService gameInitService;

    private @NotNull ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();

    @SuppressWarnings("LongLine")
    public GameMechanicsImpl(@NotNull AccountService accountService,
                             @NotNull RemotePointService remotePointService,
                             @NotNull GameSessionService gameSessionService,
                             @NotNull GameInitService gameInitService) {
        this.accountService = accountService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.gameInitService = gameInitService;
    }

    @Override
    public void addUser(@NotNull Long userId) {
        if(waiters.contains(userId)){
            //System.out.println("Такой пользователь уже есть в очереди. Второго нам не надо");
            return;
        }
        waiters.add(userId);
        tryStartGame();
    }

    public void tryStartGame(){
        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();
        while(waiters.size()>=1){ //пока в списке желающих сыграть больше 1 человека
            final Long candidateId = waiters.poll(); //достаем желающего из очереди
            //System.out.println("Проверяем, можно ли создать игру ");
            if (!insureCandidate(candidateId)) { //если он еще может игрыть
                continue;
            }
            matchedPlayers.add(accountService.getUserById(candidateId)); //добавляем его в набор игроков на следующую игру
            if(matchedPlayers.size() == 2) { //если таких набралось двое, то у них начинается игра
                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
                //System.out.println("У нас есть два игрока. Хммм. ");
                final GameSession session = gameSessionService.startGame(iterator.next(), iterator.next());
                gameInitService.initGameFor(session);
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
