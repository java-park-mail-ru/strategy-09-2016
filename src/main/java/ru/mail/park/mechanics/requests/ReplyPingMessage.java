package ru.mail.park.mechanics.requests;

public class ReplyPingMessage { //ответ на пинг от юзера
    public static final class Request {
        private String pingMessage;

        public String getPingMessage() {
            return pingMessage;
        }

        public void setPingMessage(String pingMessage) {
            this.pingMessage = pingMessage;
        }
    }
}
