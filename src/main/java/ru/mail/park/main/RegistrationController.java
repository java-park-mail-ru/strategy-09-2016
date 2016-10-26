package ru.mail.park.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
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

        try {
            final UserProfile existingUser = accountService.getUser(login);
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("user_exist"));
            }
            final UserProfile existingSession = sessionService.getUser(sessionId);
            if(existingSession!=null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BadResponse("already_authorized"));
            }
            accountService.addUser(new UserProfile(email, login, password));
            return ResponseEntity.ok(new SuccessResponse("user_created"));
        } catch(SQLException e) {
            return ResponseEntity.ok("something_go_wrong_with_database");
        }

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
    public List<UserProfile> bestRating() {
        try {
            final List<UserProfile> users = accountService.getBests();
            if(users.size()!=0) {
                //ObjectMapper mapper = new ObjectMapper();
                //try{
                    //System.out.println(users.get(0).getEmail());
                    return users;//ResponseEntity.ok(users);
                //} catch(com.fasterxml.jackson.core.JsonProcessingException e) {
                   // return ResponseEntity.ok("something_go_wrong");
               // }
            } else {
            return null;//ResponseEntity.ok("no_users_in_database");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return null;//ResponseEntity.ok("something_go_wrong_with_database");
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
        try{
            final UserProfile user = accountService.getUser(login);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("that_user_doesnt_exist"));
            }
            if (user.getPassword().equals(password)) {
                sessionService.addSession(sessionId, user);
                return ResponseEntity.ok(new SuccessResponse("successfully_authorized"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("wrong_login_password"));
        } catch(SQLException e) {
            return ResponseEntity.ok("something_go_wrong_with_database");
        }
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
