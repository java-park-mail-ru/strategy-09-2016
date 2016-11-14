package ru.mail.park.websocket;

/**
 * Created by victor on 13.11.16.
 */
public class MessageToClient {
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
