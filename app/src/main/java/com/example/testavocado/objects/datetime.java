package com.example.testavocado.objects;

public class datetime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int secound;
    //----------------------------------------------------------------------------------->
    public datetime(int year, int month, int day, int hour, int minute, int secound) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.secound = secound;
    }
    //------------------------------------------------------------------------------------>

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecound() {
        return secound;
    }

    public void setSecound(int secound) {
        this.secound = secound;
    }
    //--------------------------------------------------------------------------------------->

    @Override
    public String toString() {
        return year+"/"+month+"/"+day+" "+hour+":"+minute+":"+secound;
    }
}
