package ru.mail.park.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.UserProfile;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountServiceTest {

	@Autowired
    private AccountService accountService;

    @Test
    public void singleUsertest() throws Exception{
        UserProfile foo = new UserProfile("example@mail.ru","login","123");
        accountService.addUser(foo);
        UserProfile fromDb = accountService.getUserByLogin(foo.getLogin());
        assertNotNull(fromDb);
        assertEquals(foo.getEmail(), fromDb.getEmail());
        assertEquals(foo.getLogin(), fromDb.getLogin());
        assertEquals(foo.getPassword(), fromDb.getPassword());
        assertEquals(foo.getRating(), fromDb.getRating());
    }

    @Test
    public void ratingListTest() throws Exception {
        UserProfile foo = new UserProfile("example@mail.ru","login","123");
        accountService.addUser(foo);
        UserProfile anotherFoo = new UserProfile("example1@mail.ru", "login1", "123");
        accountService.addUser(anotherFoo);
        List<UserProfile> userList = accountService.getBests();
        assertEquals(userList.size(),2);
        assertFalse(userList.get(0).getRating()<userList.get(1).getRating());
    }
}
