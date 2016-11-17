package ru.mail.park.mechanics.requests;

/**
 * Created by victor on 17.11.16.
 */
public class PiratMoveMessage {
    public static final class Request {
        private Integer playerId;

        private Integer piratId;

        private Integer newCellIndexOfPirat;

        private Boolean isActive; //звучит странно, но ладно

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
    }
}
