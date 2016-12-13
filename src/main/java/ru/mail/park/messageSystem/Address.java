package ru.mail.park.messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by victor on 13.12.16.
 */
public class Address {
    private static AtomicInteger abonentIdCreator = new
            AtomicInteger();
    private final int abonentId;

    public Address() {
        this.abonentId =
                abonentIdCreator.incrementAndGet();
    }

    @Override
    public int hashCode() {
        return abonentId;
    }
}