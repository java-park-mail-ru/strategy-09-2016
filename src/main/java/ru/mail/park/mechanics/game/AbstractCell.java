package ru.mail.park.mechanics.game;

import org.eclipse.jetty.util.ArrayUtil;
import ru.mail.park.mechanics.utils.results.MovementResult;
import ru.mail.park.mechanics.utils.results.Result;

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

    public Integer[] killEnemy(Integer newPiratIds) {
        Integer[] deadPirats = new Integer[0];
        for(Integer piratId : piratIds) {
            if((piratId / 3) != (newPiratIds / 3) ){
                piratIds = ArrayUtil.removeFromArray(piratIds, piratId); //в этой клетке их больше нет
                deadPirats = ArrayUtil.addToArray(deadPirats, piratId, Integer.class);
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
        //results.add(new MovementResult(-1));
        if(isUnderShip){
            return gameBoard.isShipNeighbors(piratId / 3, targetCell);
        } else if(isNeighbors(targetCell)){
                return true;
            } else {
                results.add(new MovementResult(-2));
                return false;
            }
    }


    public List<Result> moveIn(Integer newPiratId, List<Result> results){
        //gameBoard.pirats[piratMove.getPiratId() - 3 * playerId].setLocation(piratMove.getTargetCell()); //тут тоже что-то может пойти не так
        System.out.println("Почти сходили");
        results.add(new MovementResult(newPiratId / 3, newPiratId % 3,this.cord));
        //А еще надо убить всех пиратов из фражеской команды
        for(Integer piratId : piratIds) {
            if((piratId / 3) != (newPiratId / 3) ){
                results.addAll(gameBoard.movePirat(new Movement(piratId,
                        this.cord, gameBoard.getShipCord(piratId / 3)), piratId));
            }
        }
        System.out.println(results.get(0).getStatus());
        return results;
    }

    public Boolean moveOut(Integer piratId, List<Result> results){
        for(Integer existingPiratId : piratIds){
            if(existingPiratId.equals(piratId)) {
                piratIds = ArrayUtil.removeFromArray(piratIds,piratId); //удялет по ключу, не по позиции
                return true;
            }
        }
        System.out.println("WTF??");
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
