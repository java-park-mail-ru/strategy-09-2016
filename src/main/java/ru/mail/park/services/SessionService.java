package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * ДЗ: реализовать класс, который по соответствующему id сессии будет отдавать пользователей(т.е. реализовать авторизацию по сессии)
 *
 */
@Service
public class SessionService {
    private Map<String, UserProfile> sessionIdTologin = new HashMap<>();

    public UserProfile addSession(String sessionId, UserProfile user) {
        return sessionIdTologin.put(sessionId, user);
    }

    public UserProfile getUser(String sessionId) {
        return sessionIdTologin.get(sessionId);
    }

    public UserProfile removeUser(String sessionId) { return sessionIdTologin.remove(sessionId); }
    //To be done
}
