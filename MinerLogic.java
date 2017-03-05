package ru.lesson.lessons.miner.v7;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

// ������ ��� ���� "�����"
 public class MinerLogic {

    final int BLOCK_SIZE = 30; // ������ ����� � ��������
    final String SIGN_OF_FLAG = "f"; // ������ �����
    final String SIGN_OF_BOMB = "B"; // ������ �����

    final int FIELD_SIZE_X = 7; // ������ ���� (���������� ����� �� X)
    final int FIELD_SIZE_Y = 7; // ������ ���� (���������� ����� �� Y)
    final int COUNT_OF_MINES = 4; // ���������� ��� �� ����
    final int[] COLOURS_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0, 0x0, 0x0, 0x0}; // ����� ����




    MinerFieldPaintable minerField;
    MinerCell[][] arrayMinerCells; // ������ �����

    // ���������, ����������� ��������� ��������� ���� (���� gameState)
    final static int GAME_STATE_STOP = 0; // ���� ��� �� ��������
    final static int GAME_STATE_STARTED = 1; // ���� ������������
    final static int GAME_STATE_FINISHED_SUCCESS = 2; // ���� ����������� � �������
    final static int GAME_STATE_FINISHED_FAIL = 3; // ���� ����������� � ����������

    // ���������, ����������� ��� ����� ������� � ������� (ProcessCell(...,int action))
    final static int ACTION_OPEN = 0; // ������� ������
    final static int ACTION_MARK_UNMARK = 1; // �������� ������/����� �������
    final static int MOUSE_BUTTON_LEFT = 1;
    final static int MOUSE_BUTTON_RIGHT = 3;



    Random random = new Random();
    int countOpenedCells;
    int gameState; // ������� ��������� ���� (GAME_STATE_STOP, GAME_STATE_STARTED, GAME_STATE_FINISHED_SUCCESS, GAME_STATE_FINISHED_FAIL)
    int bangX, bangY; // ���������� ������

    public MinerLogic(){
        minerField = new MinerFieldPaintable();
        minerField.setBLOCK_SIZE(BLOCK_SIZE);
        minerField.setSIGN_OF_BOMB(SIGN_OF_BOMB);
        minerField.setSIGN_OF_FLAG(SIGN_OF_FLAG);


        gameState=GAME_STATE_STARTED;
        countOpenedCells=0;
        arrayMinerCells = new MinerCell[FIELD_SIZE_X][FIELD_SIZE_Y];

        this.GenerateField();

        // ������������ ���� �����
        minerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int x = e.getX() / BLOCK_SIZE; // ��������� ���������� ������
                int y = e.getY() / BLOCK_SIZE;

                // ��������� ���������
                switch (e.getButton()) {
                    case MOUSE_BUTTON_LEFT:
                        ProcessCell(x, y, MinerLogic.ACTION_OPEN);
                        break;
                    case MOUSE_BUTTON_RIGHT:
                        ProcessCell(x, y, MinerLogic.ACTION_MARK_UNMARK);
                        break;
                }
            }
        });
    }




    // ������������ �������� ������������
    public void ProcessCell(int x, int y, int action) {
        MinerCell cell = arrayMinerCells[x][y];
        if(gameState==GAME_STATE_STARTED){
            switch(action) {
                case ACTION_OPEN:
                    if(cell.getCellState()==MinerCell.CELL_STATE_CLOSED){ // ���� ������ �� �������� - �� ��������� ��
                        OpenCell(x, y);
                        if (cell.getIsMine()){ // ���������� �� ���� - ����������� ����
                            gameState=GAME_STATE_FINISHED_FAIL;
                            System.out.println("Game over.");
                            bangX=x;
                            bangY=y;
                        }
                        else{ // ������� ��������� ������ - ��������� ����������� �� ����
                            CheckGameOver();
                        }
                    }
                    break;
                case ACTION_MARK_UNMARK:
                    switch(cell.getCellState()) {
                        case MinerCell.CELL_STATE_CLOSED:
                            cell.setCellState(MinerCell.CELL_STATE_MARKED_AS_MINE);
                            break;
                        case MinerCell.CELL_STATE_MARKED_AS_MINE:
                            cell.setCellState(MinerCell.CELL_STATE_CLOSED);
                            break;
                    }
                    break;
            }
            minerField.repaint();
        }
    }

    // ��������� ������ � �������� (���� ������ ��� ���)
    public void OpenCell(int x, int y){
        MinerCell cell = arrayMinerCells[x][y];
        if(cell.getCellState()!=MinerCell.CELL_STATE_OPEN){
            cell.setCellState(MinerCell.CELL_STATE_OPEN);
            countOpenedCells++;
        }

        // ��������� �������� ������
        if(cell.getCntMinesNear()==0) {
            OpenCellAround(x, y);
        }
    }

    // ���� ������ ������� ������ � ���������� ��������� �������� ������
    public void OpenCellAround(int x, int y){
        for(int dx=-1;dx<2;dx++)
            for(int dy=-1;dy<2;dy++) {
                // 1) �������� �� ������������� ������.  2) ��� �������� ������ �� �������.
                if(((x+dx)>=0 && (x+dx)<=FIELD_SIZE_X-1 && (y+dy)>=0 && (y+dy)<=FIELD_SIZE_Y-1) && (arrayMinerCells[x+dx][y+dy].getCellState()!=MinerCell.CELL_STATE_OPEN)){
                    OpenCell(x+dx, y+dy);
                }
            }
    }

    // �������� �� �������� ���������� ����
    public void CheckGameOver(){
        if(countOpenedCells==(FIELD_SIZE_X * FIELD_SIZE_Y - COUNT_OF_MINES)){
            gameState=GAME_STATE_FINISHED_SUCCESS;
            System.out.println("Game successfully finished.");
        }
    }

    // ���������� ����, ����������� ����, ��������� ���� CntMinesNear
    public void GenerateField(){
        // ��������� ���� ��������
        for(int x=0;x<FIELD_SIZE_X;x++)
            for(int y=0;y<FIELD_SIZE_Y;y++) {
                arrayMinerCells[x][y] = new MinerCell();
            }

        // ����������� ����
        int cntMines=0;
        while(cntMines<COUNT_OF_MINES){
            int x=random.nextInt(FIELD_SIZE_X);
            int y=random.nextInt(FIELD_SIZE_Y);
            if(!arrayMinerCells[x][y].getIsMine()){
                arrayMinerCells[x][y].setIsMine(true);
                cntMines++;
            }
        }

        // ������� ���������� ��� ������ ������
        for(int x=0;x<FIELD_SIZE_X;x++)
            for(int y=0;y<FIELD_SIZE_Y;y++) {
                for(int dx=-1;dx<2;dx++) {
                    for (int dy = -1; dy < 2; dy++) {
                        if ((x + dx) >= 0 && (x + dx) <= FIELD_SIZE_X - 1 && (y + dy) >= 0 && (y + dy) <= FIELD_SIZE_Y - 1) { // �������� �� ������������� ������
                            if (arrayMinerCells[x + dx][y + dy].getIsMine()) {
                                arrayMinerCells[x][y].setCntMinesNear(arrayMinerCells[x][y].getCntMinesNear() + 1);
                            }
                        }
                    }
                }
            }
    }








    // ���������� ������ MinerField (������� ���� ��� ���� "�����") ��� ���������� ������ ��������� �����.
    // ������ ������ MinerField ��� ������ �� ���� �������������, ����� ��� ������ ���� ������ ������ MinerLogic.
    class MinerFieldPaintable extends MinerField{

        // �������������� �� ����
        public void paint(Graphics g) {
            super.paint(g);
            //System.out.println("MinerFieldPaintable.paint");
            for (int x = 0; x < FIELD_SIZE_X; x++){ // �������������� ��� ������
                for (int y = 0; y < FIELD_SIZE_Y; y++) {
                    drawCell(g, x, y);
                }
            }
        }


        // ������ ��������� ������
        public void drawCell(Graphics g, int x, int y){

            MinerCell cell = arrayMinerCells[x][y];
            Color color = Color.black;
            int cntMinesNear = 0;
            String cellString = "";

            minerField.drawEmptyBlock(g, x, y);

            if ((gameState==GAME_STATE_FINISHED_SUCCESS || gameState==GAME_STATE_FINISHED_FAIL) && (cell.getIsMine())) { // ���� ���� ��������� � ���� - ��������� ��
                if ((x==bangX && y==bangY) && (gameState==GAME_STATE_FINISHED_FAIL)) {color = Color.red;}  // ���� �����
                else {color = Color.black;}
                minerField.drawBomb(g, x, y, color);
            }
            else{
                switch(cell.getCellState()) { // ����� ������� �� ��������� ������
                    case MinerCell.CELL_STATE_CLOSED:
                        minerField.drawClosedBlock(g, x, y);
                        break;
                    case MinerCell.CELL_STATE_OPEN:
                        cntMinesNear = cell.getCntMinesNear();
                        if(cntMinesNear>0){
                            cellString = Integer.toString(cntMinesNear);
                            color = new Color(COLOURS_OF_NUMBERS[cntMinesNear-1]);
                            minerField.drawNumber(g, x, y, cellString, color);
                        }
                        break;
                    case MinerCell.CELL_STATE_MARKED_AS_MINE:
                        minerField.drawFlag(g,x,y);
                        break;
                }
            }
        }

    }

}

