package ru.mail.park.mechanics.requests;

public class PiratMoveMessage { //сообщение для пользователя о том, что какой-то пират был передвинут
    public static final class Request {
        private String movements;

        private Boolean isActive;

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public String getMovement() {
            return movements;
        }

        public void setMovement(String movement) {
            this.movements = movement;
        }
    }
}
