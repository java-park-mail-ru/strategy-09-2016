package ru.mail.park.messageSystem;

/**
 * Created by victor on 13.12.16.
 */
public abstract class Message {//какое-то письмо
    private final Address from; //знает, от кого оно
    private final Address to; //и куда оно идет

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

    public abstract void exec(Abonent abonent); //а еще оно знает, что при его получении надо будет что-то сделать

}
