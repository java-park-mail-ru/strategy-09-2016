package ru.mail.park.mechanics.game;

public class Ship extends AbstractCell{
    private CoordPair location;
    private CoordPair[] avaliableDirection = new CoordPair[2];
    private static final Integer NUMNEIGHBORS = 1;

    public Ship(Integer id, CoordPair location, CoordPair orientation) {
        super.id = id;
        this.location = location;
        this.avaliableDirection[0] = orientation;
        final CoordPair tempPair = new CoordPair(-orientation.getX(),-orientation.getY());
        this.avaliableDirection[1] = tempPair;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        resetNeighbors();
    }

    public void resetNeighbors(){
        if(location.getX().equals(0)){
            neighbors[0] = new CoordPair(location.getX()+1,location.getY());
            return;
        }
        if(location.getX().equals(12)){
            neighbors[0] = new CoordPair(location.getX()-1,location.getY());
            return;
        }
        if(location.getY().equals(0)){
            neighbors[0] = new CoordPair(location.getX(),location.getY()+1);
            return;
        }
        if(location.getY().equals(12)){
            neighbors[0] = new CoordPair(location.getX(),location.getY()-1);
        }
    }


    public CoordPair[] getAvaliableDirection() {
        return avaliableDirection;
    }

    public CoordPair getLocation() {
        return location;
    }

    public void setLocation(CoordPair direction) {
        location = new CoordPair(location.getX()+direction.getX(), location.getY()+direction.getY());
    }
}
