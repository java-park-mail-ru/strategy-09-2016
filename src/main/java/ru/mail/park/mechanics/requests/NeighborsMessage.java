package ru.mail.park.mechanics.requests;

import java.util.List;

//это уже сообщение для юзера с окрестностями клекти, о которой он спрашивал
public class NeighborsMessage {
    public static class Request{
        private List<Integer> neighbors;

        public List<Integer> getNeighbors() {
            return neighbors;
        }

        public void setNeighbors(List<Integer> neighbors) {
            this.neighbors = neighbors;
        }
    }
}
