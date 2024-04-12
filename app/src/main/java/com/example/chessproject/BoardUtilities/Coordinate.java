package com.example.chessproject.BoardUtilities;

public class Coordinate {

    private int i;
    private int j;

    public Coordinate(int i, int j){
        this.i = i;
        this.j = j;
    }

    public int getI(){return this.i;}

    public int getJ(){return this.j;}

    public void setI(int i){this.i = i;}

    public void setJ(int j){this.j = j;}

    public static boolean isCoordinateEquals(Coordinate c1, Coordinate c2){
        if (c1.getI() == c2.getI() && c1.getJ() == c2.getJ())
            return true;
        return false;
    }
}
