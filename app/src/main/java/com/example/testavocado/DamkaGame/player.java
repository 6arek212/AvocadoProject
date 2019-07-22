package com.example.testavocado.DamkaGame;

import java.util.List;

public interface player {
    public  void makeTheMove(List<point> allPoints, damkaBoard[][] myBord, int i1, int[] gotoJ, int[] gotoI, int[] poitToRemoveI, int[] poitToRemoveJ);
}
