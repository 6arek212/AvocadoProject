package com.example.testavocado.XOGame;

import android.util.Log;

import java.util.Random;

public class AI {
    int AIside;
    int userSide;
    spot[][] allspots;
    int[] Choice;

    public AI(int AIside, int userSide, spot[][] allspots) {
        this.AIside = AIside;
        this.userSide = userSide;
        this.allspots = allspots;
        Choice = new int[2];
    }

    public int[] AIchoice(spot [][]allspots) {
        if (isCenterFree()) {
            Log.i("a2","return CenterFree  i="+Choice[0]+"  j="+Choice[1]);
            return Choice;
        }
        if(AiAboutToWin()){
            Log.i("a2","return AiAboutToWin  i="+Choice[0]+"  j="+Choice[1]);
            return Choice;
        }

        else if(userAboutToWin()){
            Log.i("a2","return userAboutToWin  i="+Choice[0]+"  j="+Choice[1]);
            return Choice;

        }

        else if(RandomSelect()){
            Log.i("a2","return RandomSelect  i="+Choice[0]+"  j="+Choice[1]);
            return Choice;

        }


        return Choice;
    }


    public boolean isCenterFree() {
        Log.i("a1","isCenterFree !!!!");

        if (!allspots[1][1].isChecked()) {
            Choice[0] = 1;
            Choice[1] = 1;
            return true;
        } else
            return false;
    }


    public boolean AiAboutToWin(){


        int cntRow = 0, cntCol = 0,cntDiamiter1=0,cntDiamiter2=0;
        Log.i("a1","AiiAboutToWin !!!!");

        for (int i = 0; i < allspots.length; i++) {
            for (int j = 0; j < allspots.length; j++) {
                if (allspots[i][j].isChecked() && allspots[i][j].getXorO() == AIside)
                    cntRow++;
                if(allspots[j][i].isChecked() && allspots[j][i].getXorO() == AIside)
                    cntCol++;

                if(i==j && allspots[i][j].getXorO() == AIside)
                    cntDiamiter1++;

                if(i+j==2 && allspots[i][j].getXorO() == AIside)
                    cntDiamiter2++;


            }
            if (cntRow == 2) {
                for (int m = 0; m < allspots.length; m++)
                    if (!allspots[i][m].isChecked()) {
                        Choice[0] = i;
                        Choice[1] = m;
                        Log.i("a2","return Ai Row cnt  i="+Choice[0]+"  j="+Choice[1]);

                        return true;
                    }
            }

            else {
                cntRow = 0;
            }

            if(cntCol==2) {
                for (int j = 0; j < allspots.length; j++)
                    if (!allspots[j][i].isChecked()) {
                        Choice[0] = j;
                        Choice[1] = i;

                        Log.i("a2","return Ai Col cnt  i="+Choice[0]+"  j="+Choice[1]);

                        return true;

                    }
            }else {
                cntCol = 0;
            }

        }

        if(cntDiamiter1==2) {
            for (int i=0;i<allspots.length;i++)
               if(!allspots[i][i].isChecked()) {
                   Choice[0] = i;
                   Choice[1] = i;
                   Log.i("a2","AiAboutToWin Diameter 1  i="+Choice[0]+"  j="+Choice[1]);

                   return true;
               }
        }
        else if (cntDiamiter2==2){
            for (int i=0,k=2;i<allspots.length;i++,k--)
                if(!allspots[i][k].isChecked()) {
                    Choice[0] = i;
                    Choice[1] = k;
                    Log.i("a2","AiAboutToWin Diameter 2  i="+Choice[0]+"  j="+Choice[1]);
                    return true;
                }

        }
        return false;
    }








    public boolean userAboutToWin() {


        int cntRow = 0, cntCol = 0,cntDiamiter1=0,cntDiamiter2=0;
        Log.i("a1","userAboutToWin !!!!");

        for (int i = 0; i < allspots.length; i++) {
            for (int j = 0; j < allspots.length; j++) {
                if (allspots[i][j].isChecked() && allspots[i][j].getXorO() == userSide)
                    cntRow++;
                if(allspots[j][i].isChecked() && allspots[j][i].getXorO() == userSide)
                    cntCol++;
                if(i==j && allspots[i][j].getXorO() == userSide)
                    cntDiamiter1++;

                if(i+j==2 && allspots[i][j].getXorO() == userSide)
                    cntDiamiter2++;

            }
         if (cntRow == 2) {
                for (int m = 0; m < allspots.length; m++)
                    if (!allspots[i][m].isChecked()) {
                        Choice[0] = i;
                        Choice[1] = m;
                        Log.i("a2","return User Row cnt  i="+Choice[0]+"  j="+Choice[1]);

                        return true;
                    }
            }

             else {
                cntRow = 0;
            }

              if(cntCol==2) {
                for (int m = 0; m < allspots.length; m++)
                    if (!allspots[m][i].isChecked()) {
                        Choice[0] = m;
                        Choice[1] = i;

                        Log.i("a2","return User Col cnt  i="+Choice[0]+"  j="+Choice[1]);

                        return true;

                    }
            }else {
                cntCol = 0;
            }

        }

        if(cntDiamiter1==2) {
            for (int i=0;i<allspots.length;i++)
                if(!allspots[i][i].isChecked()) {
                    Choice[0] = i;
                    Choice[1] = i;
                    return true;
                }
        }
        else if (cntDiamiter2==2){
            for (int i=0,k=2;i<allspots.length;i++,k--)
                if(!allspots[i][k].isChecked()) {
                    Choice[0] = i;
                    Choice[1] = k;

                    return true;
                }

        }

        return false;
    }


    public boolean RandomSelect(){
        int i1,j1;

        for(int i=0;i<allspots.length;i+=2)
            for(int k=0;k<allspots.length;k+=2)
                if(!allspots[i][k].isChecked())
                {
                    Choice[0]=i;
                    Choice[1]=k;
                    return true;

                }



        do {
            Random rand = new Random();
            i1 = rand.nextInt(3);
            j1 = rand.nextInt(3);
            Log.i("a1","i1==="+i1+""+"j1===="+j1);
        }while(allspots[i1][j1].isChecked());

        Choice[0]=i1;
        Choice[1]=j1;
            return true;

    }



}
