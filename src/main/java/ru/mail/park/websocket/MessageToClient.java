package ru.mail.park.websocket;

/**
 * Created by victor on 13.11.16.
 */
public class MessageToClient {
    public static final class Request {
        private String MyMessage;

        public String getMyMessage() {
            return MyMessage;
        }

        public void setMyMessage(String myMessage) {
            MyMessage = myMessage;
        }
    }
}
