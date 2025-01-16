package me.redstoner2019.bingobackend.bingo;

import java.util.ArrayList;
import java.util.List;

public class BingoField {
    private Boolean[][] selections = new Boolean[5][5];
    private List<String> bingosAchieved = new ArrayList<>();

    public BingoField(int width){
        selections = new Boolean[width][width];
    }

    public Boolean[][] getSelections() {
        return selections;
    }

    public void setSelected(int x, int y, boolean value){
        selections[x][y] = value;
    }

    public Boolean getSelected(int x, int y){
        if(selections[x][y] == null) return false;
        return selections[x][y];
    }

    public List<String> getBingosAchieved() {
        return bingosAchieved;
    }

    public boolean isBingoAchieved(String bingo) {
        return bingosAchieved.contains(bingo);
    }

    public void clearBingoAchieved(String bingo) {
        if(isBingoAchieved(bingo)) bingosAchieved.remove(bingo);
    }

    public void addBingoAchieved(String bingo){
        if(!bingosAchieved.contains(bingo)){
            bingosAchieved.add(bingo);
        }
    }
}
