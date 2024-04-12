package com.example.chessproject.UserUtils;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public  int  ChessScore;
    public  String username;
    public  String password;

    public User(){

    }

    public User(String password, String username, int ChessScore) {
        this.username = username;
        this.password = password;
        this.ChessScore = ChessScore;
    }
}
