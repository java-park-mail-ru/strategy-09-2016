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

	public UserProfile getUserById(Long userId){
		List<UserProfileEntity> resultList = em.createQuery(
				"select g from UserProfileEntity g where id = " + userId, UserProfileEntity.class)
				.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0).toDto();
		}
	}

    public UserProfile getUserByEmail(String email){
        List<UserProfileEntity> resultList = em.createQuery(
                "select g from UserProfileEntity g where email = \'" + email + "\'", UserProfileEntity.class)
                .getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0).toDto();
        }
    }

	public UserProfile getUserByLogin(String login){
		List<UserProfileEntity> resultList = em.createQuery(
				"select g from UserProfileEntity g where login = \'" + login + "\'", UserProfileEntity.class)
				.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0).toDto();
		}
	}

	public List<UserProfile> getBests() {
		return em.createQuery("select g from UserProfileEntity g order by rating desc", UserProfileEntity.class)
				.setMaxResults(10)
				.getResultList() // даже если я знаю, что получу одного юзера
				.stream() // то с точки зрения синтаксиса выборка возвращает
							// список
				.map(UserProfileEntity::toDto) // из одного элемента
				.collect(Collectors.toList());
	}
}
