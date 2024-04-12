package com.example.chessproject.BoardUtilities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Room {

    public Player player1;
    public Player player2;

    public String boardFEN;

    public Room(){

    }

    public Room(Player player1, Player player2, String boardFEN){
        this.player1 = player1;
        this.player2 = player2;
        this.boardFEN = boardFEN;
    }
}
