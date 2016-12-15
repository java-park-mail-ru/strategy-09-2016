package ru.mail.park.mechanics.requests;

import ru.mail.park.mechanics.utils.results.Result;

import java.util.List;

public class PiratMoveMessage { //сообщение для пользователя о том, что какой-то пират был передвинут
    public static final class Request {
        private List<Result> movements;

        private Boolean isActive;

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public List<Result> getMovement() {
            return movements;
        }

        public void setMovement(List<Result> movement) {
            this.movements = movement;
        }
    }
}
