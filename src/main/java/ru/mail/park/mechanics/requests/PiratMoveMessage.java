package ru.mail.park.mechanics.requests;

import ru.mail.park.mechanics.utils.MovementResult;

import java.util.List;

public class PiratMoveMessage { //сообщение для пользователя о том, что какой-то пират был передвинут
    public static final class Request {
        private List<MovementResult> movements;

        private Boolean isActive;

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public List<MovementResult> getMovement() {
            return movements;
        }

        public void setMovement(List<MovementResult> movement) {
            this.movements = movement;
        }
    }
}
