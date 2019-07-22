package com.example.testavocado.XOGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class XOview extends View {
    public XOview(Context context) {
        super(context);
    }


    float screenWidth,screenHeight;
    boolean firstTimeInDraw=true;
    int NoOneWon=0;
    spot allSpots[][]=new spot[3][3];
    int whoIsTurn,userTurn,AIturn;
    int []Choice=new int[2];
    AI ai;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint grid=new Paint();
        grid.setColor(Color.BLACK);
        grid.setStrokeWidth(10);
        Paint Xpaint=new Paint();
        Xpaint.setColor(Color.RED);
        Xpaint.setStrokeWidth(20);
        Paint Opaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        Opaint.setColor(Color.GREEN);
        Opaint.setStrokeWidth(15);
        Opaint.setStyle(Paint.Style.STROKE);


        if(firstTimeInDraw) {
            screenWidth=this.getWidth();
            screenHeight=this.getHeight();
            int width = 0, height = 100,cnt=0;
            for (int i = 0; i < allSpots.length; i++) {
                for (int j = 0; j < allSpots.length; j++) {
                    allSpots[i][j] = new spot(width, height, width + screenWidth / 3, height + (screenHeight-100) / 3, false, -1);
                    width += screenWidth / 3;
                }
                width = 0;
                height += (screenHeight-100) / 3;
            }
            firstTimeInDraw=false;


            NoOneWon=0;

            Random rand = new Random();
            whoIsTurn= rand.nextInt(2);

            AIturn=whoIsTurn;

            if(AIturn==1)
                userTurn=0;
            else
                userTurn=1;

            ai=new AI(AIturn,userTurn,allSpots);

            if(1==AIturn)
            {
             AIMove();
             }

        }

        //Game structur
        canvas.drawLine(0,100,screenWidth,100,grid);
        canvas.drawLine(screenWidth,100,screenWidth,screenHeight,grid);
        canvas.drawLine(screenWidth,screenHeight,0,screenHeight,grid);
        canvas.drawLine(0,screenHeight,0,100,grid);


        canvas.drawLine(screenWidth/3,100,screenWidth/3,screenHeight,grid);
        canvas.drawLine(screenWidth*2/3,100,screenWidth*2/3,screenHeight,grid);


        canvas.drawLine(0,100+((screenHeight-100)/3),screenWidth,100+((screenHeight-100)/3),grid);
        canvas.drawLine(0,100+((screenHeight-100)*2/3),screenWidth,100+((screenHeight-100)*2/3),grid);

        //Draw X or O Turn
        if(userTurn==1) {
            canvas.drawLine(screenWidth, 0, screenWidth - 90, 90, grid);
            canvas.drawLine(screenWidth, 90, screenWidth - 90, 0, grid);

        }
        else
            canvas.drawCircle(screenWidth-90,50,50,grid);

        //Draw all spots with X or O
        for (int i = 0; i < allSpots.length ; i++)
            for (int j = 0; j < allSpots.length ; j++)
                if(allSpots[i][j].isChecked()){
                    if(allSpots[i][j].getXorO()==1) {
                        canvas.drawLine(allSpots[i][j].getX1(), allSpots[i][j].getY1(), allSpots[i][j].getX2(), allSpots[i][j].getY2(), Xpaint);
                        canvas.drawLine(allSpots[i][j].getX2(), allSpots[i][j].getY1(), allSpots[i][j].getX1(), allSpots[i][j].getY2(), Xpaint);

                    }
                    else if(allSpots[i][j].getXorO()==0) {
                        float sumX=allSpots[i][j].getX1() + ((allSpots[i][j].getX2() - allSpots[i][j].getX1()) / 2),

                                sumY=allSpots[i][j].getY1() + ((allSpots[i][j].getY2() - allSpots[i][j].getY1()) / 2);

                        canvas.drawCircle(sumX, sumY,70, Opaint);
                    }
                }


               // Draw if there is a Win
        if(checkifWon || NoOneWon==9)
        {
            canvas.drawRect(0,0,100,100,Xpaint);

            if(checkifWon)
                canvas.drawLine(xyWIN[0],xyWIN[1],xyWIN[2],xyWIN[3],grid);

        }




    }




    boolean checkifWon=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean loopEnd=true;
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if (!checkifWon&&NoOneWon<9 && event.getY() > 100) {
                for (int i = 0; i < allSpots.length && loopEnd; i++)
                    for (int j = 0; j < allSpots.length && loopEnd; j++)
                        if ((event.getX() >= allSpots[i][j].getX1() && event.getX() <= allSpots[i][j].getX2()) && (event.getY() >= allSpots[i][j].getY1() && event.getY() <= allSpots[i][j].getY2())) {
                            if (!allSpots[i][j].isChecked()) {
                                allSpots[i][j].setXorO(userTurn);
                                allSpots[i][j].setChecked(true);
                                checkIfwon(allSpots, allSpots[i][j].getXorO());
                                ChangePlayer();
                                NoOneWon++;
                                Log.i("a3","check=="+checkifWon);

                                if(!checkifWon&&NoOneWon<9)
                                {
                                    AIMove();
                                }
                                invalidate();
                               // if(!checkifWon) {
                                //    NoOneWon++;
                              //  }

                            }
                            loopEnd = false;
                        }

            }




            else if ((checkifWon ||NoOneWon==9)&& ((event.getX() >= 0 && event.getX() <= 100) && (event.getY() >= 0 && event.getY() <= 100))) {
                firstTimeInDraw = true;
                checkifWon = false;
                invalidate();
            }

        }
      return true;
    }


    public void AIMove(){
        Choice = ai.AIchoice(allSpots);
        allSpots[Choice[0]][Choice[1]].setChecked(true);
        allSpots[Choice[0]][Choice[1]].setXorO(AIturn);
        checkIfwon(allSpots, allSpots[Choice[0]][Choice[1]].getXorO());
        ChangePlayer();
        NoOneWon++;


    }

    public void ChangePlayer(){
        whoIsTurn= (whoIsTurn==1?0:1);
    }




    float xyWIN[]=new float[4];


    public void checkIfwon(spot allSpots[][],int XorOtoChcek)
    {
        boolean loopEnd=true;
        int cntRow=0,cntCol=0,cntDiameter1=0,cntDiameter2=0;


        for (int i = 0; i < allSpots.length&&loopEnd; i++) {
            for (int j = 0; j < allSpots.length; j++) {
                if (allSpots[i][j].isChecked() && allSpots[i][j].getXorO() == XorOtoChcek)
                    cntRow++;

                if (allSpots[j][i].isChecked() && allSpots[j][i].getXorO() == XorOtoChcek)
                    cntCol++;

                if (i == j && allSpots[i][j].isChecked() && allSpots[i][j].getXorO() == XorOtoChcek)
                    cntDiameter1++;

                if (i + j == allSpots.length - 1 && allSpots[i][j].isChecked() && allSpots[i][j].getXorO() == XorOtoChcek)
                    cntDiameter2++;

            }

            if(cntRow==allSpots.length) {
                loopEnd = false;
                xyWIN[0]=allSpots[i][0].getX1();
                xyWIN[1]=allSpots[i][0].getY1()+((allSpots[i][0].getY2()-allSpots[i][0].getY1())/2);
                xyWIN[2]=allSpots[i][2].getX2();
                xyWIN[3]=allSpots[i][2].getY1()+((allSpots[i][2].getY2()-allSpots[i][2].getY1())/2);
                checkifWon=true;
                Log.i("a3","row win");


            }

            else if(cntCol==allSpots.length)
            {
                loopEnd = false;
                xyWIN[0]=allSpots[0][i].getX1()+((allSpots[0][i].getX2()-allSpots[0][i].getX1())/2);
                xyWIN[1]=allSpots[0][i].getY1();
                xyWIN[2]=allSpots[2][i].getX1()+((allSpots[2][i].getX2()-allSpots[2][i].getX1())/2);
                xyWIN[3]=allSpots[2][i].getY2();
                checkifWon=true;
                Log.i("a3","col win");

            }
            cntRow=0;
            cntCol=0;

        }
        if(cntDiameter1==allSpots.length)
        {
            xyWIN[0]=0;
            xyWIN[1]=100;
            xyWIN[2]=screenWidth;
            xyWIN[3]=screenHeight;
            checkifWon=true;
            Log.i("a3","cntDiameter1 win");

        }
        else if(cntDiameter2==allSpots.length){
            xyWIN[0]=screenWidth;
            xyWIN[1]=100;
            xyWIN[2]=0;
            xyWIN[3]=screenHeight;
            checkifWon=true;
            Log.i("a3","cntDiameter2 win");

        }
    }



}
