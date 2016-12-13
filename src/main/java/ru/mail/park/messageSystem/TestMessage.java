package ru.mail.park.messageSystem;

/**
 * Created by victor on 13.12.16.
 */
public class TestMessage extends  Message{
    public TestMessage(Address from, Address to) {
        super(from, to);
    }
    public void exec(Abonent abonent) {
        System.out.println("Меня получили!");
    }
}
