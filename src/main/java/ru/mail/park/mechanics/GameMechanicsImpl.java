package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.internal.GameInitService;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameMechanicsImpl implements GameMechanics {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanics.class);

    private @NotNull AccountService accountService;

    private @NotNull RemotePointService remotePointService;

    private final @NotNull GameInitService gameInitService;

    private @NotNull ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();

    @SuppressWarnings("LongLine")
    public GameMechanicsImpl(@NotNull AccountService accountService,
                             @NotNull RemotePointService remotePointService,
                             @NotNull GameInitService gameInitService) {
        this.accountService = accountService;
        this.remotePointService = remotePointService;
        this.gameInitService = gameInitService;
    }

    @Override
    public void addUser(@NotNull Long userId) {
        if(waiters.contains(userId)){
            LOGGER.debug("Player, who already state in queue, try to up again");
            return;
        }
        LOGGER.debug("Player added in queue");
        waiters.add(userId);
        tryStartGame();
    }

    public void tryStartGame(){
        LOGGER.debug("Try to start game");
        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();
        while(waiters.size()>=1){ //пока в списке желающих сыграть больше 1 человека
            final Long candidateId = waiters.poll(); //достаем желающего из очереди
            LOGGER.debug("Is it possible to start game?");
            if (!insureCandidate(candidateId)) { //если он еще может игрыть
                continue;
            }
            matchedPlayers.add(accountService.getUserById(candidateId)); //добавляем его в набор игроков на следующую игру
            if(matchedPlayers.size() == 2) { //если таких набралось двое, то у них начинается игра
                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
                LOGGER.debug("We have to people, who want to play.");
                gameInitService.initGameFor(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add);
    }

    private boolean insureCandidate(@NotNull Long candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUserById(candidate) != null;
    }


    @Override
    public void reset() {

    }
}
