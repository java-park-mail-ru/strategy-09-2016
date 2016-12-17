package ru.mail.park.mechanics.requests;

import java.util.List;

public class BoardMapForUsers { //сообщение юзеру о начале игры со всей необходимой информацией для начала этой игры
    public static final class Request {
        private List<Integer> gameBoard;

        private String enemyNick;

        private Boolean isActive;

        public List<Integer> getGameBoard() {
            return gameBoard;
        } //вроде и светятся неактивными, но все равно используются

        public void setGameBoard(List<Integer> gameBoard) {
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
