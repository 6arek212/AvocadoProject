package com.example.testavocado.DamkaGame;

import java.util.List;

public class bluePlayer implements player {
    List<point> myPoints;
    damkaBoard[][] myBoard;


    @Override
    public void makeTheMove(List<point> allPoints, damkaBoard[][] myBord, int i1, int[] gotoJ, int[] gotoI,int [] poitToRemoveI, int []poitToRemoveJ) {
        this.myPoints = allPoints;
        this.myBoard = myBoard;
        boolean loopEnd3;
        int g=allPoints.get(i1).getTurn();



        if (allPoints.get(i1).getMyJ() < myBord.length - 1 && allPoints.get(i1).getMyJ() > 0) {
            int ipoint, jpoint;
            ipoint = allPoints.get(i1).getMyI();
            jpoint = allPoints.get(i1).getMyJ();
            loopEnd3 = true;

//left
            if (allPoints.get(i1).getMyI()-2>=0&&allPoints.get(i1).getMyJ()-2>=0&&myBord[ipoint - 1][jpoint - 1].checked)
                for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                    {
                        if (allPoints.get(m).getMyI() == ipoint - 1 && allPoints.get(m).getMyJ() == jpoint - 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if (!myBord[allPoints.get(i1).getMyI() - 2][allPoints.get(i1).getMyJ() - 2].checked) {
                                    gotoJ[0] = allPoints.get(i1).getMyJ() - 2;
                                    gotoI[0] = allPoints.get(i1).getMyI() - 2;

                                    poitToRemoveI[0]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[0]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }
                    }
                }
            else if(allPoints.get(i1).getMyI()-1>=0&&allPoints.get(i1).getMyJ()-1>=0&&!myBord[ipoint - 1][jpoint - 1].checked) {
                gotoJ[0] = allPoints.get(i1).getMyJ() - 1;
                gotoI[0] = allPoints.get(i1).getMyI() - 1;
            }

            loopEnd3 = true;
//right
            if (allPoints.get(i1).getMyI()-2>=0&&allPoints.get(i1).getMyJ()+2<=7&&myBord[ipoint - 1][jpoint + 1].checked)
                for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                    if (allPoints.get(m).getMyI() == ipoint - 1 && allPoints.get(m).getMyJ() == jpoint + 1)  {
                       if(allPoints.get(m).getTurn() == (g==1?0:1)){
                           if ( !myBord[allPoints.get(i1).getMyI() - 2][allPoints.get(i1).getMyJ() + 2].checked) {
                               gotoJ[1] = allPoints.get(i1).getMyJ() + 2;
                               gotoI[1] = allPoints.get(i1).getMyI() - 2;

                               poitToRemoveI[1]=allPoints.get(m).getMyI();
                               poitToRemoveJ[1]=allPoints.get(m).getMyJ();
                           }

                       }

                        loopEnd3 = false;
                    }
                }

            else if(allPoints.get(i1).getMyI()-1>=0&&allPoints.get(i1).getMyJ()+1<8&&!myBord[ipoint - 1][jpoint + 1].checked){
                gotoJ[1] = allPoints.get(i1).getMyJ() + 1;
                gotoI[1] = allPoints.get(i1).getMyI() - 1;

            }


        }
        ///if he is at begining 0

        else {
            if (allPoints.get(i1).getMyJ() == 0) {
                loopEnd3 = true;
                int ipoint, jpoint;
                ipoint = allPoints.get(i1).getMyI();
                jpoint = allPoints.get(i1).getMyJ();

                if (allPoints.get(i1).getMyI()-2>=0&&myBord[ipoint - 1][jpoint + 1].checked)
                    for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                        if (allPoints.get(m).getMyI() == ipoint - 1 && allPoints.get(m).getMyJ() == jpoint + 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if ( !myBord[allPoints.get(i1).getMyI() - 2][allPoints.get(i1).getMyJ() + 2].checked) {
                                    gotoJ[0] = allPoints.get(i1).getMyJ() + 2;
                                    gotoI[0] = allPoints.get(i1).getMyI() - 2;

                                    poitToRemoveI[0]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[0]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }


                    }

                else if(allPoints.get(i1).getMyI()-1>=0&&!myBord[ipoint - 1][jpoint + 1].checked){
                    gotoI[0] = allPoints.get(i1).getMyI() - 1;
                    gotoJ[0] = allPoints.get(i1).getMyJ() + 1;
                }

            }
            ///if he is at last 7
            else {
                loopEnd3 = true;
                int ipoint, jpoint;
                ipoint = allPoints.get(i1).getMyI();
                jpoint = allPoints.get(i1).getMyJ();

                if (allPoints.get(i1).getMyI()-2>=0&&myBord[ipoint - 1][jpoint - 1].checked)
                    for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                        if (allPoints.get(m).getMyI() == ipoint - 1 && allPoints.get(m).getMyJ() == jpoint - 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if (!myBord[allPoints.get(i1).getMyI() - 2][allPoints.get(i1).getMyJ() - 2].checked) {
                                    gotoJ[0] = allPoints.get(i1).getMyJ() - 2;
                                    gotoI[0] = allPoints.get(i1).getMyI() - 2;

                                    poitToRemoveI[0]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[0]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }
                    }

                else if(allPoints.get(i1).getMyI()-1>=0&&!myBord[ipoint - 1][jpoint - 1].checked){
                    gotoJ[0] = allPoints.get(i1).getMyJ() - 1;
                    gotoI[0] = allPoints.get(i1).getMyI() - 1;
                }

            }

            gotoJ[1] = -1;
            gotoI[1] = -1;
        }
    }







}
