package com.example.mylibrary;

public class RectData {
   int w=100;
    int h=100;
    int x=100;
    int y=100;




    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public RectData(int w, int h, int x, int y) {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;

    }
}

