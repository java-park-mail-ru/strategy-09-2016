package ru.mail.park.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Solovyev on 06/09/16.
 */

@RestController
public class RegistrationController{
    private final AccountService accountService;
    private final SessionService sessionService;

    @Autowired
    public RegistrationController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity registration(@RequestBody RegistrationRequest body,
                                HttpSession httpSession) {
        final String sessionId = httpSession.getId();

        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("null_field"));
        }
        final List<UserProfile> existingUsers = accountService.getUser(login);
        if (existingUsers.size() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("user_exist"));
        }
        final UserProfile existingSession = sessionService.getUser(sessionId);
        if(existingSession!=null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BadResponse("already_authorized"));
        }
        accountService.addUser(new UserProfile(login, password, email));
        return ResponseEntity.ok(new SuccessResponse("user_created"));
    }

    @RequestMapping(path = "/isAuthorized", method = RequestMethod.GET)
    public ResponseEntity isAuthorized(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final UserProfile user = sessionService.getUser(sessionId);
        if(user!=null) {
            return ResponseEntity.ok(new AuthorizationResponce(true, user.getLogin()));
        }
        return ResponseEntity.ok(new AuthorizationResponce(false, "null"));
    }

    @RequestMapping(path = "/rating", method = RequestMethod.GET)
    public ResponseEntity bestRating() {
        final List<UserProfile> users = accountService.getBests();
        if(users.size()!=0) {
            ObjectMapper mapper = new ObjectMapper();
            try{
                return ResponseEntity.ok(new AuthorizationResponce(true, mapper.writeValueAsString(users)));
            } catch(com.fasterxml.jackson.core.JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.ok(new AuthorizationResponce(false, "something_go_wrong"));
            }
        } else {
        return ResponseEntity.ok(new AuthorizationResponce(false, "no_users_in_database"));
        }
    }

    @RequestMapping(path = "/exit", method = RequestMethod.GET)
    public ResponseEntity exit(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final UserProfile user = sessionService.removeUser(sessionId);
        if(user != null) {
            return ResponseEntity.ok(new SuccessResponse("Вы больше не авторизованы"));
        }
        return ResponseEntity.ok(new SuccessResponse("Вы не были авторизованы"));
    }

    @RequestMapping(path = "/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthorisationRequest body,
                               HttpSession httpSession) {

        final String sessionId = httpSession.getId();

        final String login = body.getLogin();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("null_field"));
        }
        final List<UserProfile> users = accountService.getUser(login);
        if (users.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("that_user_doesnt_exist"));
        }
        if(users.size() == 1) {
            UserProfile user = users.get(0);
            if (user.getPassword().equals(password)) {
                sessionService.addSession(sessionId, user);
                return ResponseEntity.ok(new SuccessResponse("successfully_authorized"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("database_error"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("wrong_login_password"));
    }

    private static final class RegistrationRequest {
        private String login;
        private String password;
        private String email;

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

    private static final class AuthorisationRequest {
        private String login;
        private String password;

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

    private static final class SuccessResponse {
        private String responce;

        private SuccessResponse(String responce) {
            this.responce = responce;
        }

        @SuppressWarnings("unused")
        public String getResponce() {
            return responce;
        }
    }

    private static final class BadResponse {
        private String errorCode;

        private BadResponse(String responce) {
            this.errorCode = responce;
        }

        @SuppressWarnings("unused")
        public String getErrorCode() {
            return errorCode;
        }
    }

    private static final class AuthorizationResponce {
        private Boolean isAuthorized;
        private String login;

        public AuthorizationResponce(Boolean status, String login) {
            this.isAuthorized = status;
            this.login = login;
        }
        @JsonProperty("isAuthorized")
        public Boolean getIsAuthorized() {
            return this.isAuthorized;
        }
        @JsonProperty("login")
        public String getLogin() {
            return this.login;
        }
    }

}
