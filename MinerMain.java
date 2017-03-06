package ru.lesson.lessons.miner.v7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

//import ru.lesson.lessons.miner.v6.MinerField;
//import ru.lesson.lessons.miner.v6.MinerLogic;


// Форма для игры "Сапер"
 public class MinerMain extends JFrame {

    final String TITLE_OF_PROGRAM = "Miner";
    final int START_LOCATION = 200;
    final int FIELD_DX = 6; // добавка в пикселях по X (чтобы все умещалось в форму)
    final int FIELD_DY = 29; //29+0; // + 16; // добавка в пикселях по Y

    MinerLogic minerLogic;
    MinerField minerField;

    public static void main(String[] args){
        new MinerMain();
    }


    public MinerMain(){
        //minerField = new MinerField();
        minerLogic = new MinerLogic();
        minerField = minerLogic.minerField;

        this.setTitle(TITLE_OF_PROGRAM);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(START_LOCATION, START_LOCATION, minerLogic.FIELD_SIZE_X * minerLogic.BLOCK_SIZE + FIELD_DX, minerLogic.FIELD_SIZE_Y * minerLogic.BLOCK_SIZE + FIELD_DY);
        this.setResizable(false);

        this.add(BorderLayout.CENTER, minerField);
        this.setVisible(true);
        //minerLogic.MinerFieldPaint();

    }


}
