package ru.mail.park.gameMechanics;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.park.mechanics.game.CoordPair;
import ru.mail.park.mechanics.game.GameBoard;
import ru.mail.park.mechanics.game.Movement;

/**
 * Created by victor on 16.12.16.
 */
public class GameBoardTest {

    @Test
    public void piratAndShipInteractionTest(){
        final GameBoard testGameBoard = new GameBoard();

        final CoordPair starterLocation = new CoordPair(0,6);

        final CoordPair targetSeaCell = new CoordPair(0,7);

        Movement move = new Movement(0, starterLocation, targetSeaCell);
        testGameBoard.movePirat(move,0);
        Assert.assertEquals(targetSeaCell, testGameBoard.getPiratCord(0,0));

        move = new Movement(0,targetSeaCell,starterLocation);
        testGameBoard.movePirat(move,0);
        Assert.assertEquals(starterLocation, testGameBoard.getPiratCord(0,0));



    }

    @Test
    public void gameWithPlayersInteractionTest(){
        final GameBoard testGameBoard = new GameBoard();
        final CoordPair[] firstPlayerPiratsCord = new CoordPair[3];
        final CoordPair[] secondPlayerPiratsCord = new CoordPair[3];

        final CoordPair firstStarterLocation = new CoordPair(0,6);
        final CoordPair secondStarterLocation = new CoordPair(12,6);
        for(Integer playerId = 0; playerId < 2; playerId++){
            for(Integer piratId = 0; piratId < 3; piratId++){
                if(playerId.equals(0)) {
                    firstPlayerPiratsCord[piratId] = firstStarterLocation;
                    Assert.assertEquals(firstPlayerPiratsCord[piratId], testGameBoard.getPiratCord(3*playerId + piratId,playerId));
                } else {
                    secondPlayerPiratsCord[piratId] = secondStarterLocation;
                    Assert.assertEquals(secondPlayerPiratsCord[piratId], testGameBoard.getPiratCord(3*playerId + piratId, playerId));
                }
            }
        }//стартовые локации генерируются правильно

        final CoordPair firstPlayerDirection = new CoordPair(1,0);
        final CoordPair secondPlayerDirection = new CoordPair(-1,0);

        Integer activePLayerId = 0;

        CoordPair targetCell = CoordPair.sum(firstPlayerPiratsCord[0],firstPlayerDirection);
        Movement move = new Movement(3*activePLayerId, firstPlayerPiratsCord[0], targetCell);
        testGameBoard.movePirat(move,activePLayerId);
        Assert.assertEquals(targetCell, testGameBoard.getPiratCord(3*activePLayerId,activePLayerId));
        activePLayerId = (activePLayerId+1)%2;
        firstPlayerPiratsCord[0] = testGameBoard.getPiratCord(0,0);


        targetCell = CoordPair.sum(secondPlayerPiratsCord[0],secondPlayerDirection);
        move = new Movement(3*activePLayerId
                , secondPlayerPiratsCord[0], targetCell);
        testGameBoard.movePirat(move,activePLayerId);
        Assert.assertEquals(targetCell, testGameBoard.getPiratCord(3*activePLayerId,activePLayerId));
        activePLayerId = (activePLayerId+1)%2;
        secondPlayerPiratsCord[0] = testGameBoard.getPiratCord(3,1);

        //по шесть ходов
        for(Integer turns = 0; turns < 9;turns++ ){
            if(activePLayerId==1) {
                targetCell = CoordPair.sum(secondPlayerPiratsCord[0], secondPlayerDirection);
                move = new Movement(3 * activePLayerId, secondPlayerPiratsCord[0], targetCell);
            } else {
                targetCell = CoordPair.sum(firstPlayerPiratsCord[0],firstPlayerDirection);
                move = new Movement(3*activePLayerId, firstPlayerPiratsCord[0], targetCell);
            }
            testGameBoard.movePirat(move,activePLayerId);
            Assert.assertEquals(targetCell, testGameBoard.getPiratCord(3*activePLayerId,activePLayerId));
            activePLayerId = (activePLayerId+1)%2;
            if(activePLayerId==0){
                firstPlayerPiratsCord[0] = testGameBoard.getPiratCord(0,0);
            } else {
                secondPlayerPiratsCord[0] = testGameBoard.getPiratCord(3,1);
            }
        }

        targetCell = CoordPair.sum(secondPlayerPiratsCord[0],secondPlayerDirection);
        move = new Movement(3*activePLayerId
                , secondPlayerPiratsCord[0], targetCell);
        testGameBoard.movePirat(move,activePLayerId);
        Assert.assertEquals(targetCell, testGameBoard.getPiratCord(3*activePLayerId,activePLayerId));
        //А выкинуло ли пирата вражеской команды?
        Assert.assertEquals(firstStarterLocation,testGameBoard.getPiratCord(0,0));
        secondPlayerPiratsCord[0] = testGameBoard.getPiratCord(3,1);
        firstPlayerPiratsCord[0] = testGameBoard.getPiratCord(0,0);
    }

}
