package ru.mail.park.mechanics.game;

import org.eclipse.jetty.util.ArrayUtil;

public abstract class AbstractCell {
    protected Integer id;
    protected CoordPair cord;
    protected CoordPair[] neighbors;
    protected Integer[] piratIds = new Integer[0];
    protected Boolean isUnderShip;
    protected Boolean canBeFinal;

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
            if(neighbor!=null)
            if(neighbor.getX().equals(testCell.getX())&&neighbor.getY().equals(testCell.getY()))
                return true;
        }
        return false;
    }

    public Integer setPiratId(Integer newPiratId) {
        piratIds = ArrayUtil.addToArray(piratIds,newPiratId,Integer.class);
        return 0;
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
