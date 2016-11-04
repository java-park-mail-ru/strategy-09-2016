package ru.mail.park.game;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by victor on 02.11.16.
 */
public class GameBoard {
    private static final Integer BOARDHIGHT = 13;
    private static final Integer BOARDWIGHT = 13;
    private AbstractCell[][] boardMap = new AbstractCell[BOARDHIGHT][BOARDWIGHT];

    public GameBoard() {
        final Integer NUMBEFOFCELL = 117;
        Vector<BoardCell> CellIdPool = new Vector<BoardCell>();
        Vector<AbstractCell> CellPool = new Vector<AbstractCell>();
        for(int i = 0; i < NUMBEFOFCELL; ++i) {
            CellIdPool.add(new BoardCell(i));
        }
        Collections.shuffle(CellIdPool);

        Integer currentElement = 0;
        for(int i = 1; i < 12; ++i) {
            for( int j = 1; j < 12; ++j) {
                if(!(i==1&&j==1)&&!(i==1&&j==11)&&!(i==11&&j==11)&&!(i==11&&j==1)) {
                    CellIdPool.get(currentElement).setNeighbors(new CoordPair(i,j));
                    boardMap[i][j] = CellIdPool.get(currentElement);
                    ++currentElement;
                }
            }
        }
        Integer CoastId = 117;
        for(int i = 1; i < 12; ++i) {
            boardMap[i][0]=new BoardCell(CoastId, new CoordPair(i,0));
            ++CoastId;
        }
        boardMap[11][1]=new BoardCell(CoastId, new CoordPair(11,1));
        ++CoastId;
        for(int j = 1; j < 12; ++j) {
            boardMap[12][j]=new BoardCell(CoastId, new CoordPair(12,j));
            ++CoastId;
        }
        boardMap[11][11]=new BoardCell(CoastId, new CoordPair(11,11));
        ++CoastId;
        for(int i = 11; i >0; --i) {
            boardMap[i][12]=new BoardCell(CoastId, new CoordPair(i,12));
            ++CoastId;
        }
        boardMap[1][11]=new BoardCell(CoastId, new CoordPair(1,11));
        ++CoastId;
        for(int j = 11; j > 0; --j) {
            boardMap[0][j]=new BoardCell(CoastId, new CoordPair(0,j));
            ++CoastId;
        }
        boardMap[1][1]=new BoardCell(CoastId, new CoordPair(1,1));
        ++CoastId;

        boardMap[0][0]=new BoardCell(-1, new CoordPair(0,0));
        boardMap[12][0]=new BoardCell(-1, new CoordPair(12,0));
        boardMap[12][12]=new BoardCell(-1, new CoordPair(12,12));
        boardMap[0][12]=new BoardCell(-1, new CoordPair(0,12));

    }

    public Integer getBoardMapId(Integer x, Integer y) {
        return boardMap[x][y].getId();
    }
}
