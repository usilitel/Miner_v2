package ru.lesson.lessons.miner.v7;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// ������� ���� ��� ���� "�����"
// ������ ����� ����������� ������ ������ MinerLogic ��� ���������� ������ ��������� �����
public class MinerField  extends JPanel{

    private int BLOCK_SIZE; // ������ ����� � ��������
    private String SIGN_OF_FLAG; // ������ �����
    private String SIGN_OF_BOMB; // ������ �����




    public void setBLOCK_SIZE(int BLOCK_SIZE) {
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    public void setSIGN_OF_FLAG(String SIGN_OF_FLAG) {
        this.SIGN_OF_FLAG = SIGN_OF_FLAG;
    }

    public void setSIGN_OF_BOMB(String SIGN_OF_BOMB) {
        this.SIGN_OF_BOMB = SIGN_OF_BOMB;
    }


    //------------------------
    // ������ ��� ��������� ������ ����� �����

    public void drawNumber(Graphics g, int x, int y, String string, Color color){ // ������ ����� (���������� ��� ������)
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, BLOCK_SIZE));
        g.drawString(string, x*BLOCK_SIZE + BLOCK_SIZE/4, (int)Math.floor(y*BLOCK_SIZE+BLOCK_SIZE*0.87));
    }
    public void drawBomb(Graphics g, int x, int y, Color color){ // ������ ����
        this.drawNumber(g,x,y,SIGN_OF_BOMB,color);
    }
    public void drawFlag(Graphics g, int x, int y){ // ������ �����
        drawClosedBlock(g,x,y);
        this.drawNumber(g,x,y,SIGN_OF_FLAG,Color.red);
    }
    public void drawEmptyBlock(Graphics g, int x, int y){ // ������ ������ �������� ����
        g.setColor(Color.lightGray);
        g.drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }
    public void drawClosedBlock(Graphics g, int x, int y){ // ������ �������� ����
        g.setColor(Color.lightGray);
        g.fill3DRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, true);
    }
    //------------------------

}
