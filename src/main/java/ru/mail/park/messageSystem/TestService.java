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
public class TestService implements Runnable{

    private MessageSystem ms;

    private static final long STEP_TIME = 10000;

    private Executor tickExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    public TestService(MessageSystem ms) {
        this.ms = ms;
    }

    @PostConstruct
    public void initAfterStartup() {
        tickExecutor.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Я сервис, который живет сам по себе и я ничего не делаю!");
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
