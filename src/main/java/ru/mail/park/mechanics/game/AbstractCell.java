package ru.mail.park.mechanics.game;

import org.eclipse.jetty.util.ArrayUtil;
import ru.mail.park.mechanics.utils.results.MovementResult;
import ru.mail.park.mechanics.utils.results.Result;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCell {
    protected GameBoard gameBoard;
    protected Integer id;
    protected CoordPair cord;
    protected CoordPair[] neighbors;
    protected Integer[] piratIds = new Integer[0];
    protected Boolean isUnderShip;
    protected Boolean canBeFinal;
    protected Integer countOfCoins; //на первый взгял - абсурд, монетки есть только в клетке с сундуком
    //но не стоит забывать, что пираты могут выбросить монету на любой клетке поля

    public Integer getId() {
        return id;
    }

    public void setNeighbors(CoordPair myCord) {
    }

    public CoordPair[] getNeighbors(){
        return neighbors;
    }

    public Boolean isNeighbors(CoordPair testCell){
        for(CoordPair neighbor : neighbors){
            if(neighbor.equals(testCell))
                return true;
        }
        return false;
    }

    public Integer setPiratId(Integer newPiratId) {
        piratIds = ArrayUtil.addToArray(piratIds,newPiratId,Integer.class);
        return 0;
    }

    public List<Integer> killEnemy(Integer newPiratIds) {
        List<Integer> deadPirats = new ArrayList<>();
        for(Integer piratId : piratIds) {
            if((piratId / 3) != (newPiratIds / 3) ){
                piratIds = ArrayUtil.removeFromArray(piratIds, piratId); //в этой клетке их больше нет
                deadPirats.add(piratId);
            }
        }
        return deadPirats;
    }

    public Boolean piratLeave(Integer leaverPiratId){
        for(Integer piratId:piratIds){
            if(piratId.equals(leaverPiratId)) {
                piratIds = ArrayUtil.removeFromArray(piratIds,leaverPiratId); //удялет по ключу, не по позиции
                return true;
            }
        }
        return false;
    }



    public Boolean beforeMoveIn(Integer piratId, List<Result> results){
        return true;
    }

    public Boolean beforeMoveOut(Integer piratId, List<Result> results, CoordPair targetCell){
        if(isNeighbors(targetCell)){
            return true;
        } else if(isUnderShip){
            return gameBoard.isShipNeighbors(piratId / 3, targetCell);
        } else {
            results.add(new MovementResult(-2));
            return false;
        }
    }


    public CoordPair getCord() {
        return cord;
    }

    public Boolean moveIn(Integer newPiratId, List<Result> results, List<Integer> deadPirats){
        results.add(new MovementResult(newPiratId / 3, newPiratId % 3,this.cord));
        deadPirats.addAll(killEnemy(newPiratId));
        piratIds = ArrayUtil.addToArray(piratIds,newPiratId,Integer.class);
        return true;
    }

    public Boolean moveOut(Integer piratId, List<Result> results){
        for(Integer existingPiratId : piratIds){
            if(existingPiratId.equals(piratId)) {
                piratIds = ArrayUtil.removeFromArray(piratIds,piratId); //удялет по ключу, не по позиции
                return true;
            }
        }
        results.add(new MovementResult(-1));
        return false;
    }

    public Boolean getUnderShip() {
        return isUnderShip;
    }

    public void setUnderShip(Boolean underShip) {
        isUnderShip = underShip;
    }

    public Integer[] getPiratIds() {
        return piratIds;
    }

    public String getView(){
        return "";
    }
}
