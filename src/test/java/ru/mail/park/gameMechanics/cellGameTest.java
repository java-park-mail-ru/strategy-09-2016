package ru.mail.park.gameMechanics;

import org.junit.Test;
import ru.mail.park.mechanics.game.*;
import ru.mail.park.mechanics.utils.results.Result;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Created by victor on 15.12.16.
 */
public class cellGameTest {
    @Test
    public void isCellRightCreated(){
        final BoardCell testCell = new BoardCell(0);
        testCell.setNeighbors(new CoordPair(0,0));
        assert(testCell.getNeighbors().length == 8);
        assert(testCell.isNeighbors(new CoordPair(1,0)));
        assert(!testCell.isNeighbors(new CoordPair(2,0)));
        assert(testCell.getPiratIds().length == 0);
    }

    @Test
    public void coastCellTest(){
        final Integer mediumBoardSize = 3;
        final AbstractCell[][] boardMap = new AbstractCell[mediumBoardSize][mediumBoardSize];
        for(int i = 0; i < mediumBoardSize; ++i) {
            for (int j = 0; j < mediumBoardSize; ++j) {
                if(i==0&&j==0){
                    boardMap[i][j] = new MockCell(0, new CoordPair(0,0));
                    boardMap[i][j].setNeighbors(new CoordPair(0,0));
                } else if((i==0||j==0)||(i==1&&j==1)) {
                    boardMap[i][j] = new CoastCell(mediumBoardSize,new CoordPair(i,j),null);
                } else {
                    boardMap[i][j] = new BoardCell(mediumBoardSize * i + j);
                    boardMap[i][j].setNeighbors(new CoordPair(i, j));
                }
            }
        }//создали кусочек карты
        final Integer testPiratId = 0;

        boardMap[1][1].setPiratId(testPiratId);

        assert(boardMap[1][1].getNeighbors().length==2);

        final List<Result> resultsOfWrongTurn = new ArrayList<>();
        final CoordPair wrongTargerCell = new CoordPair(2,2);

        assert(!boardMap[1][1].beforeMoveOut(testPiratId, resultsOfWrongTurn, wrongTargerCell));
        assert(resultsOfWrongTurn.get(0).getStatus()==-2);


        final CoordPair targerCell = new CoordPair(0,2);
        final List<Result> results = new ArrayList<>();
        assert(boardMap[1][1].beforeMoveOut(testPiratId, results, targerCell));
        assert(boardMap[targerCell.getY()][targerCell.getY()].beforeMoveIn(testPiratId, results));
        if(boardMap[1][1].moveOut(testPiratId, results)){
            assert (results.isEmpty());
        } else {
            fail("There are nothing problem to move out");
        }
        boardMap[targerCell.getX()][targerCell.getY()].moveIn(testPiratId, results);
        assert(results.size() == 1);
        assert(results.get(0).getStatus()>=0);
        for(int i = 0; i < mediumBoardSize; ++i) {
            for (int j = 0; j < mediumBoardSize; ++j) {
                if(i==targerCell.getX()&&j==targerCell.getY()){
                    assert(boardMap[i][j].getPiratIds().length==1);
                } else {
                    assert(boardMap[i][j].getPiratIds().length==0);
                }
            }
        }
    }

    @Test
    public void piratMoveTest(){
        final Integer smallBoardSize = 3;
        final AbstractCell[][] boardMap = new AbstractCell[smallBoardSize][smallBoardSize];
        for(int i = 0; i < smallBoardSize; ++i) {
            for (int j = 0; j < smallBoardSize; ++j) {
                boardMap[i][j] = new BoardCell(smallBoardSize * i + j);
                boardMap[i][j].setNeighbors(new CoordPair(i,j));
            }
        }
        final Integer testPiratId = 0;
        boardMap[1][1].setPiratId(testPiratId);
        for(int i = 0; i < smallBoardSize; ++i) {
            for (int j = 0; j < smallBoardSize; ++j) {
                if(i==1&&j==1){
                    assert(boardMap[i][j].getPiratIds().length==1);
                } else {
                    assert(boardMap[i][j].getPiratIds().length==0);
                }
            }
        }
        final List<Result> results = new ArrayList<>();
        assert(boardMap[1][1].beforeMoveOut(testPiratId, results, new CoordPair(0,0)));
        assert(boardMap[0][0].beforeMoveIn(testPiratId, results));
        if(boardMap[1][1].moveOut(testPiratId, results)){
            assert (results.isEmpty());
        } else {
            fail("There are nothing problem to move out");
        }
        boardMap[0][0].moveIn(testPiratId, results);
        assert(results.size() == 1);
        assert(results.get(0).getStatus()>=0);
        for(int i = 0; i < smallBoardSize; ++i) {
            for (int j = 0; j < smallBoardSize; ++j) {
                if(i==0&&j==0){
                    assert(boardMap[i][j].getPiratIds().length==1);
                } else {
                    assert(boardMap[i][j].getPiratIds().length==0);
                }
            }
        }
    }
}
