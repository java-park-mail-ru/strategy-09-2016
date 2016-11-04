package ru.mail.park.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.controller.response.RatingSuccessResponse;
import ru.mail.park.exeption.ExceptionWithErrorCode;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import java.util.List;

@RestController
public class RatingController {
    private final AccountService accountService;

    @Autowired
    public RatingController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/rating/", method = RequestMethod.GET)
    public RatingSuccessResponse bestRating() throws ExceptionWithErrorCode {
        final List<UserProfile> users = accountService.getBests();
            return new RatingSuccessResponse(users);
    }

}
