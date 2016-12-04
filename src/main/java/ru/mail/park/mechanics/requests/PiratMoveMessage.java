package ru.mail.park.mechanics.requests;

public class PiratMoveMessage { //сообщение для пользователя о том, что какой-то пират был передвинут
    public static final class Request {
        private String movements;

        private Integer playerId;

        private Integer piratId;

        private Integer newCellIndexOfPirat;

        private Boolean isActive;

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public Integer getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Integer playerId) {
            this.playerId = playerId;
        }

        public Integer getPiratId() {
            return piratId;
        }

        public void setPiratId(Integer piratId) {
            this.piratId = piratId;
        }

        public Integer getNewCellIndexOfPirat() {
            return newCellIndexOfPirat;
        }

        public void setNewCellIndexOfPirat(Integer newCellIndexOfPirat) {
            this.newCellIndexOfPirat = newCellIndexOfPirat;
        }

        public String getMovement() {
            return movements;
        }

        public void setMovement(String movement) {
            this.movements = movement;
        }
    }
}
