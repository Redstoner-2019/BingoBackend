package me.redstoner2019.bingobackend.bingo;

public class BingoField {
    private String[][] fields = new String[5][5];
    private Boolean[][] selections = new Boolean[5][5];

    public String[][] getFields() {
        return fields;
    }

    public void setField(int x, int y, String value){
        fields[x][y] = value;
    }

    public String getField(int x, int y){
        return fields[x][y];
    }

    public Boolean[][] getSelections() {
        return selections;
    }

    public void setSelected(int x, int y, boolean value){
        selections[x][y] = value;
    }

    public Boolean getSelected(int x, int y){
        return selections[x][y];
    }
}
