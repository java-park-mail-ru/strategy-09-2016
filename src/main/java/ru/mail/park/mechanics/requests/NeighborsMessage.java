package ru.mail.park.mechanics.requests;

/**
 * Created by victor on 17.11.16.
 */
public class NeighborsMessage {
    public static class Request{
        private String neighbors;

        public String getNeighbors() {
            return neighbors;
        }

        public void setNeighbors(String neighbors) {
            this.neighbors = neighbors;
        }
    }
}
