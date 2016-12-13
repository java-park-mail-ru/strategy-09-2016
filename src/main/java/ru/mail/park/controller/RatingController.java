package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.exeption.ExceptionWithErrorCode;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import java.util.LinkedList;
import java.util.List;

@RestController
public class RatingController {
    private final AccountService accountService;

    @Autowired
    public RatingController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/rating/", method = RequestMethod.GET)
    public List<BestUser> bestRating() throws ExceptionWithErrorCode {
        final List<UserProfile> users = accountService.getBests();
        final List<BestUser> bestUsers = new LinkedList<>();
        for(UserProfile userProfile:users){
            bestUsers.add(new BestUser(userProfile.getLogin(), userProfile.getRating()));
        }
        return bestUsers;
    }

    private class BestUser{
        private final String login;
        private final Integer rating;

        private BestUser(String login, Integer rating) {
            this.login = login;
            this.rating = rating;
        }

        @JsonProperty("login")
        private String getLogin() {
            return login;
        }

        @JsonProperty("rating")
        private Integer getRating() {
            return rating;
        }
    }

}
