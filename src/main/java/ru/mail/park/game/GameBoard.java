package ru.mail.park.game;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by victor on 02.11.16.
 */
public class GameBoard {
    private static final Integer BOARDHIGHT = 13;
    private static final Integer BOARDWIGHT = 13;
    private Integer[][] boardMap = new Integer[BOARDHIGHT][BOARDWIGHT];

    public GameBoard() {
        final Integer NUMBEFOFCELL = 117;
        Vector<Integer> CellIdPool = new Vector<Integer>();
        for(int i = 0; i < NUMBEFOFCELL; ++i) {
            CellIdPool.add(i);
        }
        Collections.shuffle(CellIdPool);

        Integer currentElement = 0;
        for(int i = 1; i < 12; ++i) {
            for( int j = 1; j < 12; ++j) {
                if(!(i==1&&j==1)&&!(i==1&&j==11)&&!(i==11&&j==11)&&!(i==11&&j==1)) {
                    boardMap[i][j] = CellIdPool.get(currentElement);
                    ++currentElement;
                }
            }
        }
        Integer CoastId = 117;
        for(int i = 1; i < 12; ++i) {
            boardMap[i][0]=CoastId;
            ++CoastId;
        }
        boardMap[11][1]=CoastId;
        ++CoastId;
        for(int j = 1; j < 12; ++j) {
            boardMap[12][j] = CoastId;
            ++CoastId;
        }
        boardMap[11][11]=CoastId;
        ++CoastId;
        for(int i = 11; i >0; --i) {
            boardMap[i][12]=CoastId;
            ++CoastId;
        }
        boardMap[1][11]=CoastId;
        ++CoastId;
        for(int j = 11; j > 0; --j) {
            boardMap[0][j] = CoastId;
            ++CoastId;
        }
        boardMap[1][1]=CoastId;
        ++CoastId;

        boardMap[0][0]=-1;
        boardMap[12][0]=-1;
        boardMap[12][12]=-1;
        boardMap[0][12]=-1;

    }

    public Integer getBoardMapId(Integer x, Integer y) {
        return boardMap[x][y];
    }
}
