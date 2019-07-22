package com.example.testavocado.XOGame;

public class spot {
    private float x1,y1,x2,y2;
    private boolean checked;
    private int XorO;


    public spot(float x1, float y1, float x2, float y2, boolean checked, int xorO) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.checked = checked;
        XorO = xorO;
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

    public int getXorO() {
        return XorO;
    }

    public void setXorO(int xorO) {
        XorO = xorO;
    }
}
