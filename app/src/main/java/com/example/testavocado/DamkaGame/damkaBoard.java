package com.example.testavocado.DamkaGame;

public class damkaBoard {
    float x1,y1,x2,y2;
    boolean checked;
    int color;
    int i;
    int j;



    public damkaBoard() {
    }

    public damkaBoard(float x1, float y1, float x2, float y2, boolean checked, int color, int i, int j) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.checked = checked;
        this.color = color;
        this.i = i;
        this.j = j;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getColor() {
        return color;
    }

    public void setColor() {
        this.color = color;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
