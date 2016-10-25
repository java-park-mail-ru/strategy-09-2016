package ru.mail.park.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.entities.UserProfileEntity;
import ru.mail.park.model.UserProfile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    @PersistenceContext
    private EntityManager em;


    public void addUser(UserProfile userProfile) {
        UserProfileEntity user = new UserProfileEntity(userProfile);
        em.persist(user);
    }

    public List<UserProfile> getUser(String login) throws SQLException {
        return em.createQuery("select g from UserProfileEntity g where login = \'" + login + "\'", UserProfileEntity.class)
                .getResultList() //даже если я знаю, что получу одного юзера
                .stream() //то с точки зрения синтаксиса выборка возвращает список
                .map(UserProfileEntity::toDto) //из одного элемента
                .collect(Collectors.toList());
    }

    public List<UserProfile> getBests() throws SQLException {
        return em.createQuery("select g from UserProfileEntity g order by rating desc", UserProfileEntity.class)
                .setMaxResults(10)
                .getResultList() //даже если я знаю, что получу одного юзера
                .stream() //то с точки зрения синтаксиса выборка возвращает список
                .map(UserProfileEntity::toDto) //из одного элемента
                .collect(Collectors.toList());
    }
}
