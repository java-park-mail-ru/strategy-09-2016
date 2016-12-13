package ru.mail.park.websocket;

public class MessageToClient { // отладочное сообщение для юзера
    public static final class Request {
        private String myMessage;

        public String getMyMessage() {
            return myMessage;
        }

        public void setMyMessage(String myMessage) {
            this.myMessage = myMessage;
        }
    }
}
