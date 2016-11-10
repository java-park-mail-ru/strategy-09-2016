package ru.mail.park.game;

import org.eclipse.jetty.util.ArrayUtil;

/**
 * Created by victor on 03.11.16.
 */
abstract public class AbstractCell {
    protected Integer id;
    protected CoordPair cord;
    protected CoordPair[] neighbors;
    protected Integer[] piratIds = new Integer[0];
    protected Boolean isUnderShip;
    protected Boolean canBeFinal;

    public Integer getId() {
        return id;
    }

    public void setNeighbors(CoordPair MyCord) {
    }

    public CoordPair[] getNeighbors(){
        return neighbors;
    }

    public Boolean isNeighbors(CoordPair testCell){
        for(int i = 0; i < neighbors.length; ++i){
            if(neighbors[i]!=null)
            if(neighbors[i].getX()==testCell.getX()&&neighbors[i].getY()==testCell.getY())
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
                piratIds = ArrayUtil.removeFromArray(piratIds,leaverPiratId);
                return true;
            }
        }
        return false;
        //this.piratIds.
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
