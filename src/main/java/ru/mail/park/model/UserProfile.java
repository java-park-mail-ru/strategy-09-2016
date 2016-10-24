package ru.mail.park.model;

import ru.mail.park.entities.UserProfileEntity;

import java.util.Random;

/**
 * Created by Solovyev on 17/09/16.
 */
public class UserProfile {
    private String email;
    private String login;
    private String password;
    private Integer rating;

    public UserProfile(String email, String login, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.rating = new Random().nextInt(100);
    }

    public UserProfile(String email, String login, String password, Integer rating) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.rating = rating;
    }

    public UserProfile(UserProfileEntity user){
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.rating = user.getRating();
    }


    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRating() {
        return rating;
    }
}
