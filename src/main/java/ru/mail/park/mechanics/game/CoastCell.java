package ru.mail.park.game;

public class CoastCell extends AbstractCell {
    private static final Integer NUMNEIGHBORS = 2;
    // вроде как и магические числа, но делать их константами, принадлежащими клетке тоже странно
    // это параметры игровой доски

    public CoastCell(Integer id, CoordPair thisCellCord) {
        super.id = id;
        super.isUnderShip = false;
        super.neighbors = new CoordPair[NUMNEIGHBORS];
        super.canBeFinal = true;
        setNeighbors(thisCellCord);
    }

    @Override
    public void setNeighbors(CoordPair myCord) {
        final Integer thisCellX = myCord.getX();
        final Integer thisCellY = myCord.getY();
        if(thisCellX==0){ //северный берег
            northCoastCase(thisCellX, thisCellY);
            return;
        }
        if(thisCellY==12){ //восточный берег
            eastCoastCase(thisCellX, thisCellY);
            return;
        }
        if(thisCellX==12){ //южный берег
            southCoastCase(thisCellX, thisCellY);
            return;
        }
        if(thisCellY==0){ //западный берег
            westCoastCase(thisCellX, thisCellY);
            return;
        }
        CoordPair tempPair;
        if(thisCellX==1&&thisCellY==1){ //северо-западный угол
            tempPair = new CoordPair(thisCellX + 1, thisCellY - 1);
            neighbors[0] = tempPair;
            tempPair = new CoordPair(thisCellX - 1, thisCellY + 1);
            neighbors[1] = tempPair;
            return;
        }
        if(thisCellX==11&&thisCellY==1){ //северо-восточный угол
            tempPair = new CoordPair(thisCellX - 1, thisCellY - 1);
            neighbors[0] = tempPair;
            tempPair = new CoordPair(thisCellX + 1, thisCellY + 1);
            neighbors[1] = tempPair;
            return;
        }
        if(thisCellX==11&&thisCellY==11){ //юго-восточный угол
            tempPair = new CoordPair(thisCellX + 1, thisCellY - 1);
            neighbors[0] = tempPair;
            tempPair = new CoordPair(thisCellX - 1, thisCellY + 1);
            neighbors[1] = tempPair;
            return;
        }
        if(thisCellX==1&&thisCellY==11) { //юго-западный угол
            tempPair = new CoordPair(thisCellX + 1, thisCellY + 1);
            neighbors[0] = tempPair;
            tempPair = new CoordPair(thisCellX - 1, thisCellY - 1);
            neighbors[1] = tempPair;
        }
    }

    private void northCoastCase(Integer thisCellX, Integer thisCellY){
        CoordPair tempPair;
        switch(thisCellY){
            case 1:
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX , thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 2:
                tempPair = new CoordPair(thisCellX + 1, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 10:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 11:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            default:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
        }
    }

    private void eastCoastCase(Integer thisCellX, Integer thisCellY){
        CoordPair tempPair;
        switch(thisCellX){
            case 1:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            case 2:
                tempPair = new CoordPair(thisCellX - 1, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            case 10:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY - 1);
                neighbors[1] = tempPair;
                break;
            case 11:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[1] = tempPair;
                break;
            default:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
        }
    }

    private void southCoastCase(Integer thisCellX, Integer thisCellY){
        CoordPair tempPair;
        switch(thisCellY){
            case 1:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 2:
                tempPair = new CoordPair(thisCellX - 1, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 10:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 11:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            default:
                tempPair = new CoordPair(thisCellX, thisCellY - 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
        }
    }

    private void westCoastCase(Integer thisCellX, Integer thisCellY){
        CoordPair tempPair;
        switch(thisCellX){
            case 1:
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            case 2:
                tempPair = new CoordPair(thisCellX - 1, thisCellY + 1);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
                break;
            case 10:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            case 11:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX, thisCellY + 1);
                neighbors[1] = tempPair;
                break;
            default:
                tempPair = new CoordPair(thisCellX - 1, thisCellY);
                neighbors[0] = tempPair;
                tempPair = new CoordPair(thisCellX + 1, thisCellY);
                neighbors[1] = tempPair;
        }
    }

    @Override
    public String getView(){
        StringBuilder builder = new StringBuilder();
        if(!isUnderShip) {
            builder.append("~~");
        } else {
            builder.append(" S ");
        }
        return builder.toString();
    }
}
