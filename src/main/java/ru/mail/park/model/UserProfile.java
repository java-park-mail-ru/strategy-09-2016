package ru.mail.park.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Solovyev on 17/09/16.
 */
public class UserProfile {
    private String login;
    private String email;
    private String password;
    private long id;
    private static final AtomicLong IDGENERATOR = new AtomicLong(0);

    public UserProfile(String login, String email, String password) {
        this.id = IDGENERATOR.getAndIncrement();
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getId() { return id; }
}
