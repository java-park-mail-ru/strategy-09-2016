package ru.mail.park.messageSystem;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by victor on 13.12.16.
 */
@Service
public class MessageSystem {
    //крч,класс - почта России
    //по логике, кстати, быть бы ему синглтоном
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new ConcurrentHashMap<Address, ConcurrentLinkedQueue<Message>>();
    //карта из очередей - почтовых ящиков
    public void sendMessage(Message message){ //положили письмо в очереди/почтовый ящик
        messages.get(message.getTo()).add(message);
    }

    public void addAbonent(Address abonentAddress){
        messages.put(abonentAddress, new ConcurrentLinkedQueue<>());
    }

    public void execForAbonent(Abonent abonent) { //приходит такой аббенент, а у него целый ящик спама и начинает он его, значит, разгребать
        Queue<Message> messageQueue = messages.get(abonent.getAddress());
        while(!messageQueue.isEmpty()){
            Message message = messageQueue.poll();
            message.exec(abonent);
        }
    }

}
