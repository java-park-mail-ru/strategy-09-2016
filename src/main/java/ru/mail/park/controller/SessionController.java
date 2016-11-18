package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.controller.response.AuthorisationResponse;
import ru.mail.park.controller.response.SuccessResponse;
import ru.mail.park.exeption.ExceptionWithErrorCode;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import javax.servlet.http.HttpSession;

@RestController
public class SessionController {
    private final AccountService accountService;

    @Autowired
    public SessionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/exit/", method = RequestMethod.GET)
    public SuccessResponse exit(HttpSession httpSession) {
        httpSession.setAttribute("userLogin",null);
        return new SuccessResponse("session_set_null");
    }

    @RequestMapping(path = "/session/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public SuccessResponse auth(@RequestBody AuthorisationRequest body,
                               HttpSession httpSession) throws ExceptionWithErrorCode {
        final String login = body.getLogin();
        final String password = body.getPassword();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            throw new ExceptionWithErrorCode("S01","null_field");
        }
        final UserProfile user = accountService.getUserByLogin(login);
        if (user == null) {
            throw new ExceptionWithErrorCode("S02","wrong_login_password");
        }
        if (user.getPassword().equals(password)) {
            UserProfile testUser = accountService.getUserById(user.getId());
            if(testUser!=null)
            httpSession.setAttribute("userId",user.getId()); //такое чувство, что вывод врет и по факту айдишники нумеруются с единицы
            return new SuccessResponse("successfully_authorized");
        }
        throw new ExceptionWithErrorCode("S02","wrong_login_password");
    }

    @RequestMapping(path = "/isAuthorised/", method = RequestMethod.GET)
    public AuthorisationResponse isAuth(HttpSession httpSession) {
        if(httpSession.getAttribute("userId")!=null) {
            final UserProfile user = accountService.getUserById((Long)httpSession.getAttribute("userId"));
            return new AuthorisationResponse(true, user.getLogin());
        }
        return new AuthorisationResponse(false, null);
    }

    private static final class AuthorisationRequest {
        private final String login;
        private final String password;
        @JsonCreator
        private AuthorisationRequest(@JsonProperty("login") String login,
                                     @JsonProperty("password") String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}
