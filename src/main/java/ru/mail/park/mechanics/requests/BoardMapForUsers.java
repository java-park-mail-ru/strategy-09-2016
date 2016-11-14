package ru.mail.park.mechanics.requests;

/**
 * Created by victor on 14.11.16.
 */
public class BoardMapForUsers {
    public static final class Request {
        private String gameBoard;

        private String enemyNick;

        private Boolean isActive; //звучит странно, но ладно

        public String getGameBoard() {
            return gameBoard;
        }

        public void setGameBoard(String gameBoard) {
            this.gameBoard = gameBoard;
        }

        public String getEnemyNick() {
            return enemyNick;
        }

        public void setEnemyNick(String enemyNick) {
            this.enemyNick = enemyNick;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }
    }
}
