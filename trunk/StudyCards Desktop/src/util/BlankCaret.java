package util;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;

public class BlankCaret implements Caret{
    public void install(javax.swing.text.JTextComponent c){}
    public void deinstall(javax.swing.text.JTextComponent c){}
    public void paint(Graphics g){}
    public void addChangeListener(ChangeListener l){}
    public void removeChangeListener(ChangeListener l){}
    public boolean isVisible(){return false;}
    public void setVisible(boolean v){}
    public boolean isSelectionVisible(){return false;}
    public void setSelectionVisible(boolean v){}
    public void setMagicCaretPosition(Point p){}
    public Point getMagicCaretPosition(){return new Point(0,0);}
    public void setBlinkRate(int rate){}
    public int getBlinkRate(){return 10000;}
    public int getDot(){return 0;}
    public int getMark(){return 0;}
    public void setDot(int dot){}
    public void moveDot(int dot){}
};