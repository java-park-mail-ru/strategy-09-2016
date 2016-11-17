package ru.mail.park.mechanics.requests;

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
