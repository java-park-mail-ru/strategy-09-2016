package ru.mail.park.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.model.UserProfile;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Created by victor on 25.10.16.
 */
@Transactional
public class AccountServiceTest {

    private AccountService accountService;


    @Before
    @Autowired
    public void setup(){
        accountService = new AccountService();
    }


    @Test
    public void singleUsertest(){

        UserProfile foo = new UserProfile("example@mail.ru","login","123");
        accountService.addUser(foo);
        try {
            List<UserProfile> userList = accountService.getUser(foo.getLogin());
            assertSame(1, userList.size());
            assertSame(foo, userList.get(0));
        } catch (SQLException e) {

        }
    }
    @Test
    public void ratingListTest() {
        UserProfile foo = new UserProfile("example@mail.ru","login","123");
        accountService.addUser(foo);
        UserProfile anotherFoo = new UserProfile("example1@mail.ru", "login1", "123");
        accountService.addUser(anotherFoo);
        try {
            List<UserProfile> userList = accountService.getBests();
            assertSame(userList.size(),2);
            assertFalse(userList.get(0).getRating()<userList.get(1).getRating());
        } catch (SQLException e) {

        }
    }
}
