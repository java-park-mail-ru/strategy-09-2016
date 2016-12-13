package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.controller.response.SuccessResponse;
import ru.mail.park.exeption.ExceptionWithErrorCode;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import javax.servlet.http.HttpSession;

@RestController
public class RegistrationController{
    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/user/", method = RequestMethod.POST)
    public SuccessResponse registration(@RequestBody RegistrationRequest body,
                                HttpSession httpSession) throws ExceptionWithErrorCode {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            throw new ExceptionWithErrorCode("R01","null_field");
        }
        final UserProfile existingUserWithLogin = accountService.getUserByLogin(login);
        final UserProfile existingUserWithEmail = accountService.getUserByEmail(email);
        if (existingUserWithEmail != null||existingUserWithLogin!=null) {
            throw new ExceptionWithErrorCode("R02","user_already_exist");
        }
        if(httpSession.getAttribute("userLogin")!=null) {
            throw new ExceptionWithErrorCode("R03","already_authorised");
        }
        accountService.addUser(new UserProfile(email, login, password));
        return new SuccessResponse("user_created");
    }

    private static final class RegistrationRequest {
        private final String login;
        private final String password;
        private final String email;
        @JsonCreator
        private RegistrationRequest(@JsonProperty("login") String login,
                                    @JsonProperty("password") String password,
                                    @JsonProperty("email") String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

}
