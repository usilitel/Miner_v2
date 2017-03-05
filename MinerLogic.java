package ru.lesson.lessons.miner.v7;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

// логика дл€ игры "—апер"
 public class MinerLogic {

    final int BLOCK_SIZE = 30; // размер блока в пиксел€х
    final String SIGN_OF_FLAG = "f"; // символ флага
    final String SIGN_OF_BOMB = "B"; // символ бомбы

    final int FIELD_SIZE_X = 7; // размер пол€ (количество €чеек по X)
    final int FIELD_SIZE_Y = 7; // размер пол€ (количество €чеек по Y)
    final int COUNT_OF_MINES = 4; // количество мин на поле
    final int[] COLOURS_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0, 0x0, 0x0, 0x0}; // цвета цифр




    MinerFieldPaintable minerField;
    MinerCell[][] arrayMinerCells; // массив €чеек

    // константы, описывающие возможное состо€ние игры (поле gameState)
    final static int GAME_STATE_STOP = 0; // игра еще не началась
    final static int GAME_STATE_STARTED = 1; // игра продолжаетс€
    final static int GAME_STATE_FINISHED_SUCCESS = 2; // игра завершилась с успехом
    final static int GAME_STATE_FINISHED_FAIL = 3; // игра завершилась с проигрышем

    // константы, описывающие что нужно сделать с €чейкой (ProcessCell(...,int action))
    final static int ACTION_OPEN = 0; // открыть €чейку
    final static int ACTION_MARK_UNMARK = 1; // пометить €чейку/сн€ть пометку
    final static int MOUSE_BUTTON_LEFT = 1;
    final static int MOUSE_BUTTON_RIGHT = 3;



    Random random = new Random();
    int countOpenedCells;
    int gameState; // текущее состо€ние игры (GAME_STATE_STOP, GAME_STATE_STARTED, GAME_STATE_FINISHED_SUCCESS, GAME_STATE_FINISHED_FAIL)
    int bangX, bangY; // координаты взрыва

    public MinerLogic(){
        minerField = new MinerFieldPaintable();
        minerField.setBLOCK_SIZE(BLOCK_SIZE);
        minerField.setSIGN_OF_BOMB(SIGN_OF_BOMB);
        minerField.setSIGN_OF_FLAG(SIGN_OF_FLAG);


        gameState=GAME_STATE_STARTED;
        countOpenedCells=0;
        arrayMinerCells = new MinerCell[FIELD_SIZE_X][FIELD_SIZE_Y];

        this.GenerateField();

        // обрабатываем клик мышью
        minerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int x = e.getX() / BLOCK_SIZE; // вычисл€ем координаты €чейки
                int y = e.getY() / BLOCK_SIZE;

                // запускаем обработку
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




    // обрабатываем действие пользовател€
    public void ProcessCell(int x, int y, int action) {
        MinerCell cell = arrayMinerCells[x][y];
        if(gameState==GAME_STATE_STARTED){
            switch(action) {
                case ACTION_OPEN:
                    if(cell.getCellState()==MinerCell.CELL_STATE_CLOSED){ // если €чейка не помечена - то открываем ее
                        OpenCell(x, y);
                        if (cell.getIsMine()){ // напоролись на мину - заканчиваем игру
                            gameState=GAME_STATE_FINISHED_FAIL;
                            System.out.println("Game over.");
                            bangX=x;
                            bangY=y;
                        }
                        else{ // открыли свободную €чейку - провер€ем закончилась ли игра
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

    // открываем €чейку и соседние (если вокруг нет мин)
    public void OpenCell(int x, int y){
        MinerCell cell = arrayMinerCells[x][y];
        if(cell.getCellState()!=MinerCell.CELL_STATE_OPEN){
            cell.setCellState(MinerCell.CELL_STATE_OPEN);
            countOpenedCells++;
        }

        // идем вокруг текущей €чейки и рекурсивно открываем соседние €чейки
        if(cell.getCntMinesNear()==0){
            for(int dx=-1;dx<2;dx++)
                for(int dy=-1;dy<2;dy++) {
                    // 1) проверка на существование €чейки.  2) уже открытые €чейки не трогаем.
                    if(((x+dx)>=0 && (x+dx)<=FIELD_SIZE_X-1 && (y+dy)>=0 && (y+dy)<=FIELD_SIZE_Y-1) && (arrayMinerCells[x+dx][y+dy].getCellState()!=MinerCell.CELL_STATE_OPEN)){
                        OpenCell(x+dx, y+dy);
                    }
                }
        }
    }

    // проверка на успешное завершение игры
    public void CheckGameOver(){
        if(countOpenedCells==(FIELD_SIZE_X * FIELD_SIZE_Y - COUNT_OF_MINES)){
            gameState=GAME_STATE_FINISHED_SUCCESS;
            System.out.println("Game successfully finished.");
        }
    }

    // генерируем поле, расставл€ем мины, заполн€ем поле CntMinesNear
    public void GenerateField(){
        // заполн€ем поле €чейками
        for(int x=0;x<FIELD_SIZE_X;x++)
            for(int y=0;y<FIELD_SIZE_Y;y++) {
                arrayMinerCells[x][y] = new MinerCell();
            }

        // расставл€ем мины
        int cntMines=0;
        while(cntMines<COUNT_OF_MINES){
            int x=random.nextInt(FIELD_SIZE_X);
            int y=random.nextInt(FIELD_SIZE_Y);
            if(!arrayMinerCells[x][y].getIsMine()){
                arrayMinerCells[x][y].setIsMine(true);
                cntMines++;
            }
        }

        // считаем количество мин вокруг €чейки
        for(int x=0;x<FIELD_SIZE_X;x++)
            for(int y=0;y<FIELD_SIZE_Y;y++) {
                for(int dx=-1;dx<2;dx++) {
                    for (int dy = -1; dy < 2; dy++) {
                        if ((x + dx) >= 0 && (x + dx) <= FIELD_SIZE_X - 1 && (y + dy) >= 0 && (y + dy) <= FIELD_SIZE_Y - 1) { // проверка на существование €чейки
                            if (arrayMinerCells[x + dx][y + dy].getIsMine()) {
                                arrayMinerCells[x][y].setCntMinesNear(arrayMinerCells[x][y].getCntMinesNear() + 1);
                            }
                        }
                    }
                }
            }
    }








    // –асширение класса MinerField (игровое поле дл€ игры "—апер") дл€ реализации логики отрисовки €чеек.
    // ¬нутри класса MinerField эту логику не стал реализовывать, чтобы вс€ логика была внутри класса MinerLogic.
    class MinerFieldPaintable extends MinerField{

        // перерисовываем всЄ поле
        public void paint(Graphics g) {
            super.paint(g);
            //System.out.println("MinerFieldPaintable.paint");
            for (int x = 0; x < FIELD_SIZE_X; x++){ // перерисовываем все €чейки
                for (int y = 0; y < FIELD_SIZE_Y; y++) {
                    drawCell(g, x, y);
                }
            }
        }


        // рисуем отдельную €чейку
        public void drawCell(Graphics g, int x, int y){

            MinerCell cell = arrayMinerCells[x][y];
            Color color = Color.black;
            int cntMinesNear = 0;
            String cellString = "";

            minerField.drawEmptyBlock(g, x, y);

            if ((gameState==GAME_STATE_FINISHED_SUCCESS || gameState==GAME_STATE_FINISHED_FAIL) && (cell.getIsMine())) { // если игра закончена » мина - открываем ее
                if ((x==bangX && y==bangY) && (gameState==GAME_STATE_FINISHED_FAIL)) {color = Color.red;}  // цвет бомбы
                else {color = Color.black;}
                minerField.drawBomb(g, x, y, color);
            }
            else{
                switch(cell.getCellState()) { // иначе смотрим на состо€ние €чейки
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

