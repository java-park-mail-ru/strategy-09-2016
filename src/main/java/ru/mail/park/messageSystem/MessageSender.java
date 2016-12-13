package ru.mail.park.messageSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by victor on 13.12.16.
 */
@Service
public class MessageSender implements Runnable, Abonent{

    private MessageSystem ms;

    private static final long STEP_TIME = 5000;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    private final Address myAddress = new Address();

    private final Address testServiceAddress; //ссылка на весь сервис мне не нужна, а вот адрес добыть надо

    @Autowired
    public MessageSender(MessageSystem ms, TestService testService) {
        this.ms = ms;
        this.testServiceAddress = testService.getAddress();
        ms.addAbonent(myAddress);
    }

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Я отправляю сообщение.");
                ms.sendMessage(new TestMessage(this.myAddress, testServiceAddress));
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Address getAddress(){
        return this.myAddress;
    }
}
