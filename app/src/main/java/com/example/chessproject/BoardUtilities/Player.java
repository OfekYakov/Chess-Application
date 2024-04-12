package com.example.chessproject.BoardUtilities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Player {
    public Color playerColor;
    public String uid;

    public Player(){

    }
    public Player(Color playerColor, String uid){
        this.uid = uid;
        this.playerColor = playerColor;
    }

}
