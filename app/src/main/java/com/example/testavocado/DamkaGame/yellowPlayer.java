package com.example.testavocado.DamkaGame;

import java.util.List;

public class yellowPlayer implements  player{
    List<point> myPoints;
    damkaBoard [][]myBoard;


    @Override
    public void makeTheMove(List<point> allPoints, damkaBoard[][] myBord, int i1, int[] gotoJ, int[] gotoI,int [] poitToRemoveI, int []poitToRemoveJ) {
        this.myPoints = allPoints;
        this.myBoard = myBoard;
        boolean loopEnd3;
        int g=allPoints.get(i1).getTurn();


      ///if the spot is in the middle means have tow places to go

        if (allPoints.get(i1).getMyJ() < myBord.length - 1 && allPoints.get(i1).getMyJ() > 0) {
            int ipoint, jpoint;
            ipoint = allPoints.get(i1).getMyI();
            jpoint = allPoints.get(i1).getMyJ();
            loopEnd3 = true;

//left
            if (allPoints.get(i1).getMyI()+2<8&&allPoints.get(i1).getMyJ()-2>=0&&myBord[ipoint + 1][jpoint - 1].checked)
                for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                    {
                        if (allPoints.get(m).getMyI() == ipoint + 1 && allPoints.get(m).getMyJ() == jpoint - 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if (!myBord[allPoints.get(i1).getMyI() + 2][allPoints.get(i1).getMyJ() - 2].checked) {
                                    gotoJ[2] = allPoints.get(i1).getMyJ() - 2;
                                    gotoI[2] = allPoints.get(i1).getMyI() + 2;

                                    poitToRemoveI[2]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[2]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }
                    }
                }
            else if(allPoints.get(i1).getMyI()+1<8&&allPoints.get(i1).getMyJ()-1 >=0&&!myBord[ipoint + 1][jpoint - 1].checked) {
                gotoJ[2] = allPoints.get(i1).getMyJ() - 1;
                gotoI[2] = allPoints.get(i1).getMyI() +  1;
            }

            loopEnd3 = true;
//right
            if (allPoints.get(i1).getMyI()+2<8&&allPoints.get(i1).getMyJ()+2<8&&myBord[ipoint + 1][jpoint + 1].checked)
                for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                    if (allPoints.get(m).getMyI() == ipoint + 1 && allPoints.get(m).getMyJ() == jpoint + 1)  {
                        if(allPoints.get(m).getTurn() == (g==1?0:1)){
                            if (!myBord[allPoints.get(i1).getMyI() + 2][allPoints.get(i1).getMyJ() + 2].checked) {
                                gotoJ[3] = allPoints.get(i1).getMyJ() + 2;
                                gotoI[3] = allPoints.get(i1).getMyI() + 2;

                                poitToRemoveI[3]=allPoints.get(m).getMyI();
                                poitToRemoveJ[3]=allPoints.get(m).getMyJ();
                            }
                        }

                        loopEnd3 = false;
                    }
                }

            else if(allPoints.get(i1).getMyI()+1<8&&allPoints.get(i1).getMyJ()+1<8&&!myBord[ipoint + 1][jpoint + 1].checked){
                gotoJ[3] = allPoints.get(i1).getMyJ() + 1;
                gotoI[3] = allPoints.get(i1).getMyI() + 1;

            }



        }


        else {
            // if the spot is at the beginnig means at 0 index
            if (allPoints.get(i1).getMyJ() == 0) {
                loopEnd3 = true;
                int ipoint, jpoint;
                ipoint = allPoints.get(i1).getMyI();
                jpoint = allPoints.get(i1).getMyJ();

                if (allPoints.get(i1).getMyI()+2<8&&myBord[ipoint + 1][jpoint + 1].checked)
                    for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                        if (allPoints.get(m).getMyI() == ipoint + 1 && allPoints.get(m).getMyJ() == jpoint + 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if (!myBord[allPoints.get(i1).getMyI() + 2][allPoints.get(i1).getMyJ() + 2].checked) {
                                    gotoJ[2] = allPoints.get(i1).getMyJ() + 2;
                                    gotoI[2] = allPoints.get(i1).getMyI() + 2;

                                    poitToRemoveI[2]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[2]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }
                    }

                else if(allPoints.get(i1).getMyI()+1<8&&!myBord[ipoint+1][jpoint+1].isChecked()){
                    gotoI[2] = allPoints.get(i1).getMyI() + 1;
                    gotoJ[2] = allPoints.get(i1).getMyJ() + 1;
                }
            }

            // if the spot is at the last means at 7 index

            else {
                loopEnd3 = true;
                int ipoint, jpoint;
                ipoint = allPoints.get(i1).getMyI();
                jpoint = allPoints.get(i1).getMyJ();

                if (allPoints.get(i1).getMyI()+2<8&&myBord[ipoint + 1][jpoint - 1].checked)
                    for (int m = 0; m < allPoints.size() && loopEnd3; m++) {
                        if (allPoints.get(m).getMyI() == ipoint + 1 && allPoints.get(m).getMyJ() == jpoint - 1 ) {
                            if(allPoints.get(m).getTurn() == (g==1?0:1)){
                                if ( !myBord[allPoints.get(i1).getMyI() + 2][allPoints.get(i1).getMyJ() - 2].checked) {
                                    gotoJ[2] = allPoints.get(i1).getMyJ() - 2;
                                    gotoI[2] = allPoints.get(i1).getMyI() + 2;

                                    poitToRemoveI[2]=allPoints.get(m).getMyI();
                                    poitToRemoveJ[2]=allPoints.get(m).getMyJ();
                                }
                            }

                            loopEnd3 = false;
                        }
                    }

                else if(allPoints.get(i1).getMyI()+1<8&&!myBord[ipoint+1][jpoint-1].isChecked()) {
                    gotoJ[2] = allPoints.get(i1).getMyJ() - 1;
                    gotoI[2] = allPoints.get(i1).getMyI() + 1;
                }
            }
        }
    }


}
