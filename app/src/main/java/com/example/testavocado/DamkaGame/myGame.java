package com.example.testavocado.DamkaGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class myGame extends View {
    public myGame(Context context) {
        super(context);
    }

    damkaBoard[][] myBord ;
    ArrayList<point> allPoints ;

    boolean firstTimeInDraw = true, firtTimeAllPoints = true;
    float screenWidth, screenHeight;
    int whosTurn;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint gameStructure = new Paint();
        gameStructure.setStrokeWidth(1);
        gameStructure.setColor(Color.BLACK);

        if (firstTimeInDraw) {
            myBord= new damkaBoard[8][8];
            screenHeight = this.getHeight();
            screenWidth = this.getWidth();
            int cellColor = Color.rgb(249, 239, 222);
            float width = 0, height = 100;

            for (int i = 0; i < myBord.length; i++) {
                for (int j = 0; j < myBord.length; j++) {
                    myBord[i][j] = new damkaBoard(width, height, width + (screenWidth / 8), height + ((screenHeight - 100) / 8), false, cellColor, i, j);
                    width += (screenWidth / 8);

                    cellColor = (cellColor == Color.rgb(249, 239, 222) ? cellColor = Color.rgb(86, 86, 86)
                            : Color.rgb(249, 239, 222));

                }
                width = 0;
                height += ((screenHeight - 100) / 8);
                cellColor = (cellColor == Color.rgb(249, 239, 222) ? cellColor = Color.rgb(86, 86, 86)
                        : Color.rgb(249, 239, 222));
            }

            Random rand = new Random();
            int n = rand.nextInt(2);

            whosTurn = n;

            b1 = new bluePlayer();
            b2 = new yellowPlayer();

            gameEnded = false;

            firstTimeInDraw = false;
        }

        /// Draw Who's Turn

        if (whosTurn == 1) {
            gameStructure.setColor(Color.rgb(143, 177, 232));
            canvas.drawCircle(100, 50, 35, gameStructure);
        } else {
            gameStructure.setColor(Color.rgb(242, 211, 101));
            canvas.drawCircle(100, 50, 35, gameStructure);
        }


        /// Board Cells

        for (int i = 0; i < myBord.length; i++)
            for (int j = 0; j < myBord.length; j++) {
                gameStructure.setColor(myBord[i][j].getColor());
                canvas.drawRect(myBord[i][j].getX1(), myBord[i][j].getY1(), myBord[i][j].getX2(), myBord[i][j].getY2(), gameStructure);
            }

////intitalize all points
        if (firtTimeAllPoints) {
            allPoints=new ArrayList<point>();
            int color = Color.rgb(242, 211, 101), turn = 0;
            gameStructure.setColor(color);
            for (int i = 0; i < myBord.length; i++) {
                for (int j = 0; j < myBord.length; j++) {
                    if ((i % 2 != 0 && j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)) {
                        allPoints.add(new point(color, myBord[i][j].getX1() + ((screenWidth / 8) / 2), myBord[i][j].getY1() +
                                ((screenHeight / 8) / 2), myBord[i][j].getX1() +
                                ((screenWidth / 8) / 2), myBord[i][j].getY1() +
                                ((screenHeight / 8) / 2), i, j, turn,
                                false));

                        myBord[i][j].setChecked(true);
                    }
                }
                if (i == 2) {
                    i = 4;
                    color = Color.rgb(143, 177, 232);
                    gameStructure.setColor(color);
                    turn = 1;
                }
            }
            firtTimeAllPoints = false;
        }

        //draw place to go


        if (arrysCreated)
            for (int i = 0; i < gotoI.length; i++) {
                if (gotoI[i] != -1 && gotoJ[i] != -1) {
                    gameStructure.setColor(Color.rgb(234, 236, 249));
                    canvas.drawRect(myBord[gotoI[i]][gotoJ[i]].getX1(), myBord[gotoI[i]][gotoJ[i]].getY1(), myBord[gotoI[i]][gotoJ[i]].getX2(), myBord[gotoI[i]][gotoJ[i]].getY2(), gameStructure);
                }
            }

        ////Draw pointssss

        for (int i = 0; i < allPoints.size(); i++) {
            gameStructure.setColor(allPoints.get(i).getColor());
            canvas.drawCircle(allPoints.get(i).getX(), allPoints.get(i).getY(), 40, gameStructure);
        }

        //game WIN check
        if (gameEnded) {
            gameStructure.setColor(Color.RED);
            canvas.drawRect(screenWidth - 100, 0, screenWidth, 100, gameStructure);
        }


    }


    boolean arrysCreated = false, gameEnded = false;
    float prevX, preY;
    int thisPoint = -1;
    int[] gotoJ, gotoI, poitToRemoveI, poitToRemoveJ;
    bluePlayer b1;
    yellowPlayer b2;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean loopEnd = true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (gameEnded) {
                if (event.getX() <= screenWidth && event.getX() >= screenWidth - 100 && event.getY() >= 0 && event.getY() <= 100) {
                    firstTimeInDraw = true;
                    firtTimeAllPoints = true;
                    arrysCreated = false;
                    thisPoint = -1;
                }

            } else {
                arrysCreated = false;
                for (int i1 = 0; i1 < allPoints.size() && loopEnd; i1++) {
                    if ((event.getX() <= allPoints.get(i1).getX() + 40) &&
                            (event.getX() >= allPoints.get(i1).getX() - 40) &&
                            (event.getY() <= allPoints.get(i1).getY() + 40) &&
                            (event.getY() >= allPoints.get(i1).getY() - 40)) {

                        poitToRemoveI = new int[4];
                        poitToRemoveJ = new int[4];

                        gotoJ = new int[4];
                        gotoI = new int[4];


                        if (allPoints.get(i1).getTurn() == whosTurn) {
                            arrysCreated = true;

                            freeArrays(gotoI, gotoJ, poitToRemoveI, poitToRemoveJ);


                            boolean loopEnd3 = true;

                            if (allPoints.get(i1).getTurn() == 1) {
                                b1.makeTheMove(allPoints, myBord, i1, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                                if (allPoints.get(i1).isGotToTheEnd()) {
                                    b2.makeTheMove(allPoints, myBord, i1, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                                }
                            } else if (allPoints.get(i1).getTurn() == 0) {
                                b2.makeTheMove(allPoints, myBord, i1, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                                if (allPoints.get(i1).isGotToTheEnd())
                                    b1.makeTheMove(allPoints, myBord, i1, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                            }

                            Log.i("a1", "renovI(0)=" + poitToRemoveI[0] + " ---removeI(1)=" + poitToRemoveI[1] + "     removeJ(0)=" + poitToRemoveJ[0] + "   ---removeJ(1)=" + poitToRemoveJ[1]);

                            Log.i("a1", "I(0)=" + gotoI[0] + " ---I(1)=" + gotoI[1] + "     J(0)=" + gotoJ[0] + "   ---J(1)=" + gotoJ[1]);

                            thisPoint = i1;
                            loopEnd = false;
                        } else {
                            thisPoint = -1;
                        }


                    }
                }
                if (loopEnd)
                    thisPoint = -1;
            }


        }


        /////////////////Drag Action

        else if (!gameEnded && event.getAction() == MotionEvent.ACTION_MOVE && thisPoint != -1) {
            allPoints.get(thisPoint).setX(event.getX());
            allPoints.get(thisPoint).setY(event.getY());

        }


        /////////////////Touch Up Action

        else if (!gameEnded && event.getAction() == MotionEvent.ACTION_UP && thisPoint != -1) {

            loopEnd = true;


            for (int i = 0; i < 4 && loopEnd; i++) {
                if (gotoJ[i] != -1 && gotoI[i] != -1) {
                    if (!myBord[gotoI[i]][gotoJ[i]].checked && ((allPoints.get(thisPoint).getX()) >= myBord[gotoI[i]][gotoJ[i]].getX1()) &&
                            (allPoints.get(thisPoint).getX() <= myBord[gotoI[i]][gotoJ[i]].getX2()) &&
                            (allPoints.get(thisPoint).getY() >= myBord[gotoI[i]][gotoJ[i]].getY1()) &&
                            (allPoints.get(thisPoint).getY() <= myBord[gotoI[i]][gotoJ[i]].getY2())
                            ) {


                        myBord[allPoints.get(thisPoint).getMyI()][allPoints.get(thisPoint).getMyJ()].setChecked(false);

                        allPoints.get(thisPoint).setX(myBord[gotoI[i]][gotoJ[i]].getX1() + ((screenWidth / 8) / 2));
                        allPoints.get(thisPoint).setY(myBord[gotoI[i]][gotoJ[i]].getY1() + ((screenHeight / 8) / 2));
                        allPoints.get(thisPoint).setMyI(gotoI[i]);
                        allPoints.get(thisPoint).setMyJ(gotoJ[i]);

                        allPoints.get(thisPoint).setPrevX(allPoints.get(thisPoint).getX());
                        allPoints.get(thisPoint).setPrevY(allPoints.get(thisPoint).getY());

                        myBord[allPoints.get(thisPoint).getMyI()][allPoints.get(thisPoint).getMyJ()].setChecked(true);

                        arrysCreated = false;

                        if (gotoI[i] == 0) {
                            allPoints.get(thisPoint).setGotToTheEnd(true);
                            allPoints.get(thisPoint).setColor(Color.rgb(13, 83, 168));
                        } else if (gotoI[i] == 7) {
                            allPoints.get(thisPoint).setColor(Color.rgb(255, 184, 5));
                            allPoints.get(thisPoint).setGotToTheEnd(true);
                        }

                        int i_for_thisPoint = allPoints.get(thisPoint).getMyI();
                        int j_for_thisPoint = allPoints.get(thisPoint).getMyJ();

                        if (poitToRemoveI[i] != -1 && poitToRemoveJ[i] != -1)
                            for (int m = 0; m < allPoints.size() && loopEnd; m++) {
                                if (allPoints.get(m).getMyI() == poitToRemoveI[i] && allPoints.get(m).getMyJ() == poitToRemoveJ[i]) {
                                    myBord[poitToRemoveI[i]][poitToRemoveJ[i]].setChecked(false);
                                    allPoints.remove(m);
                                    loopEnd = false;
                                }
                            }

                        //if took out a spot find if there is another take down
                        boolean check = true;
                        if (!loopEnd) {
                            for (int find = 0; find < allPoints.size() && check; find++)
                                if (allPoints.get(find).getMyI() == i_for_thisPoint && allPoints.get(find).getMyJ() == j_for_thisPoint) {
                                    thisPoint = find;
                                    check = false;
                                }

                            freeArrays(gotoI, gotoJ, poitToRemoveI, poitToRemoveJ);
                            if (allPoints.get(thisPoint).isGotToTheEnd()) {
                                b1.makeTheMove(allPoints, myBord, thisPoint, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                                b2.makeTheMove(allPoints, myBord, thisPoint, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                            } else if (allPoints.get(thisPoint).getTurn() == 1)
                                b1.makeTheMove(allPoints, myBord, thisPoint, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                            else
                                b2.makeTheMove(allPoints, myBord, thisPoint, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);


                            check = true;

                            for (int k = 0; k < gotoI.length && check; k++)
                                if (poitToRemoveI[k] != -1 && poitToRemoveJ[k] != -1) {
                                    check = false;
                                    arrysCreated = true;
                                }
                        }

                        if (check) {
                            whosTurn = (whosTurn == 1 ? 0 : 1);
                            if (checkIfTheGameEnded(whosTurn)) {
                                Toast.makeText(getContext(), "Game Ended !", Toast.LENGTH_SHORT).show();
                                gameEnded = true;
                                whosTurn = (whosTurn == 1 ? 0 : 1);
                            } else{
                                //    Toast.makeText(getContext(), "not ended", Toast.LENGTH_SHORT).show();

                            }

                        }
                        loopEnd = false;

                    }

                }


            }


            if (loopEnd) {
                allPoints.get(thisPoint).setX(allPoints.get(thisPoint).getPrevX());
                allPoints.get(thisPoint).setY(allPoints.get(thisPoint).getPrevY());

            }

            Log.i("a1", "got to invalidate ");

        }

        invalidate();

        return true;
    }


    public void freeArrays(int[] gotoI, int[] gotoJ, int[] poitToRemoveI, int[] poitToRemoveJ) {
        for (int i = 0; i < 4; i++) {
            poitToRemoveI[i] = -1;
            poitToRemoveJ[i] = -1;

            gotoJ[i] = -1;
            gotoI[i] = -1;
        }
    }

    public boolean checkIfTheGameEnded(int turn) {
        boolean check = false;


        boolean loopEnd = true;
        for (int i = 0; i < allPoints.size() && loopEnd; i++) {
            freeArrays(gotoI, gotoJ, poitToRemoveI, poitToRemoveJ);
            if (allPoints.get(i).getTurn() == turn) {
                if (allPoints.get(i).isGotToTheEnd()) {
                    b1.makeTheMove(allPoints, myBord, i, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                    b2.makeTheMove(allPoints, myBord, i, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);
                } else if (turn == 1)
                    b1.makeTheMove(allPoints, myBord, i, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);

                else if (turn == 0)
                    b2.makeTheMove(allPoints, myBord, i, gotoJ, gotoI, poitToRemoveI, poitToRemoveJ);

                for (int m = 0; m < gotoI.length; m++)
                    if (gotoI[m] != -1 && gotoJ[m] != -1) {
                        loopEnd = false;
                    }
            }
        }

        check = (loopEnd ? true : false);
        Log.i("a1", "check===" + check);
        return check;
    }

}
