package ru.mail.park.mechanics.requests;

//это уже сообщение для юзера с окрестностями клекти, о которой он спрашивал
public class NeighborsMessage { // не уверен в названии
    public static class Request{ //так-то это скорее response, чем request
        private String neighbors;

        public String getNeighbors() {
            return neighbors;
        }

        public void setNeighbors(String neighbors) {
            this.neighbors = neighbors;
        }
    }
}
