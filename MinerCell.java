package ru.lesson.lessons.miner.v7;



// €чейка дл€ игры "—апер"
public class MinerCell {

    // константы, описывающие возможные состо€ни€ €чейки (поле cellState)
    final static int CELL_STATE_CLOSED = 0; // €чейка закрыта
    final static int CELL_STATE_OPEN = 1; // €чейка открыта
    final static int CELL_STATE_MARKED_AS_MINE = 2; // €чейка помечена как мина

    private boolean isMine = false; // true = мина
    private int cellState = 0; // CELL_STATE_CLOSED, CELL_STATE_OPEN, CELL_STATE_MARKED_AS_MINE
    private int cntMinesNear = 0; // количество мин вокруг



    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setCellState(int cellState) {
        this.cellState = cellState;
    }

    public void setCntMinesNear(int cntMinesNear) {
        this.cntMinesNear = cntMinesNear;
    }


    public boolean getIsMine() { return isMine; }

    public int getCellState() {
        return cellState;
    }

    public int getCntMinesNear() {
        return cntMinesNear;
    }




}
