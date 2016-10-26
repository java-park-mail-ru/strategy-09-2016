package ru.mail.park.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.exeption.CustomExeption;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Solovyev on 06/09/16.
 */

@RestController
public class RegistrationController{
    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/user/", method = RequestMethod.POST)
    public ResponseEntity registration(@RequestBody RegistrationRequest body,
                                HttpSession httpSession) throws CustomExeption {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            throw new CustomExeption("R01","null_field");
        }
        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null) {
            throw new CustomExeption("R02","user_already_exist");
        }
        if(httpSession.getAttribute("userLogin")!=null) {
            throw new CustomExeption("R03","already_authorised");
        }
        accountService.addUser(new UserProfile(email, login, password));
        return ResponseEntity.ok(new SuccessResponse("user_created"));
    }

    @RequestMapping(path = "/rating/", method = RequestMethod.GET)
    public SuccessResponseRating bestRating() {
        final List<UserProfile> users = accountService.getBests();
        if(users.size()!=0) {
            return new SuccessResponseRating(users);//ResponseEntity.ok(users);
        } else {
        return new SuccessResponseRating("no_one_in_database");//ResponseEntity.ok("no_users_in_database");
        }
    }

    @RequestMapping(path = "/isAuthorised/", method = RequestMethod.GET)
    public ResponseEntity isAuth(HttpSession httpSession) {
        if(httpSession.getAttribute("userLogin")!=null) {
            final UserProfile user = accountService.getUser(httpSession.getAttribute("userLogin").toString());
            System.out.println(user.getId());
            return ResponseEntity.ok(new AuthorizationResponce(true, user.getLogin()));
        }
        return ResponseEntity.ok(new AuthorizationResponce(false, null));
    }

    @RequestMapping(path = "/exit/", method = RequestMethod.GET)
    public ResponseEntity exit(HttpSession httpSession) {
        httpSession.setAttribute("userLogin",null);
        return ResponseEntity.ok(new SuccessResponse("session_set_null"));
    }

    @RequestMapping(path = "/session/", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthorisationRequest body,
                               HttpSession httpSession) throws CustomExeption {
        final String login = body.getLogin();
        final String password = body.getPassword();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            throw new CustomExeption("S01","null_field");
        }
        final UserProfile user = accountService.getUser(login);
        if (user == null) {
            throw new CustomExeption("S02","wrong_login_password");
        }
        if (user.getPassword().equals(password)) {
            httpSession.setAttribute("userLogin",user.getLogin()); //такое чувство, что вывод врет и по факту айдишники нумеруются с единицы
            return ResponseEntity.ok(new SuccessResponse("successfully_authorized"));
        }
        throw new CustomExeption("S02","wrong_login_password");
    }

    @ExceptionHandler(CustomExeption.class)
    private ResponseEntity handleCustomExeption(CustomExeption customExeption){
        if(customExeption.getErrorCode().equals("R03")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BadResponse(customExeption.getErrorMessage()));

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse(customExeption.getErrorMessage()));
        }
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity handleAllExeption(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse("ooops, something go wrong way"));
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

    private static final class SuccessResponseRating{
        private List<UserProfile> body;
        private String errorCode;

        private SuccessResponseRating(List<UserProfile> body) { this.body = body; }

        private SuccessResponseRating(String errorMessage) { this.errorCode = errorMessage; }

        @JsonProperty("body")
        public List<UserProfile> getBody() {
            return body;
        }

        @JsonProperty("errorCode")
        public String getErrorMessage() {
            return errorCode;
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
