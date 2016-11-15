package ru.mail.park.game;

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

    private void cornerCaseCut(Integer importantCord){
        switch(importantCord){
            case 1:
                neighbors[0] = new CoordPair(-1,-1);
                neighbors[1] = new CoordPair(-1,-1);
                break;
            case 2:
                neighbors[0] = new CoordPair(-1,-1);
                break;
            case 10:
                neighbors[2] = new CoordPair(-1,-1);
                break;
            case 11:
                neighbors[1] = new CoordPair(-1,-1);
                neighbors[2] = new CoordPair(-1,-1);
                break;
        }
    }

    public void resetNeighbors(){
        if(location.getX()==0){
            neighbors[0] = new CoordPair(location.getX()+1,location.getY()); /*
            neighbors[1] = new CoordPair(location.getX()+1,location.getY());
            neighbors[2] = new CoordPair(location.getX()+1,location.getY()+1);
            cornerCaseCut(location.getY()); */
            return;
        }
        if(location.getX()==12){
            neighbors[0] = new CoordPair(location.getX()-1,location.getY()); /*
            neighbors[1] = new CoordPair(location.getX()-1,location.getY());
            neighbors[2] = new CoordPair(location.getX()-1,location.getY()+1);
            cornerCaseCut(location.getY()); */
            return;
        }
        if(location.getY()==0){
            neighbors[0] = new CoordPair(location.getX(),location.getY()+1); /*
            neighbors[1] = new CoordPair(location.getX(),location.getY()+1);
            neighbors[2] = new CoordPair(location.getX()+1,location.getY()+1);
            cornerCaseCut(location.getX()); */
            return;
        }
        if(location.getY()==12){
            neighbors[0] = new CoordPair(location.getX(),location.getY()-1); /*
            neighbors[1] = new CoordPair(location.getX(),location.getY()-1);
            neighbors[2] = new CoordPair(location.getX()+1,location.getY()-1);
            cornerCaseCut(location.getX()); */
            return;
        }
        return;
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
