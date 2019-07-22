package com.example.testavocado.DamkaGame;

public class point {
    int color;
    float x,y;
    float prevX,prevY;
    int myI;
    int myJ;
    int turn;
    boolean gotToTheEnd;

    public point() {
    }

    public point(int color, float x, float y, float prevX, float prevY, int myI, int myJ, int turn, boolean gotToTheEnd) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.prevX = prevX;
        this.prevY = prevY;
        this.myI = myI;
        this.myJ = myJ;
        this.turn = turn;
        this.gotToTheEnd = gotToTheEnd;
    }

    public boolean isGotToTheEnd() {
        return gotToTheEnd;
    }

    public void setGotToTheEnd(boolean gotToTheEnd) {
        this.gotToTheEnd = gotToTheEnd;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public float getPrevX() {
        return prevX;
    }

    public void setPrevX(float prevX) {
        this.prevX = prevX;
    }

    public float getPrevY() {
        return prevY;
    }

    public void setPrevY(float prevY) {
        this.prevY = prevY;
    }

    public int getMyI() {
        return myI;
    }

    public void setMyI(int myI) {
        this.myI = myI;
    }

    public int getMyJ() {
        return myJ;
    }

    public void setMyJ(int myJ) {
        this.myJ = myJ;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
