package ru.mail.park.entities;

import ru.mail.park.model.UserProfile;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "rating")
    private Integer rating;

    public UserProfileEntity(UserProfile user) {
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.rating = user.getRating();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public UserProfile toDto() {
        return new UserProfile(email, login, password, rating);
    }

}
