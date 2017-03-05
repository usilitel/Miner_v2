package ru.lesson.lessons.miner.v7;



// ������ ��� ���� "�����"
public class MinerCell {

    // ���������, ����������� ��������� ��������� ������ (���� cellState)
    final static int CELL_STATE_CLOSED = 0; // ������ �������
    final static int CELL_STATE_OPEN = 1; // ������ �������
    final static int CELL_STATE_MARKED_AS_MINE = 2; // ������ �������� ��� ����

    private boolean isMine = false; // true = ����
    private int cellState = 0; // CELL_STATE_CLOSED, CELL_STATE_OPEN, CELL_STATE_MARKED_AS_MINE
    private int cntMinesNear = 0; // ���������� ��� ������



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
