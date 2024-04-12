package com.example.chessproject.GamesRecords;

import com.example.chessproject.BoardUtilities.Player;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class gameRecord {
    public ArrayList<String> gameMoves;
    public String gameResult;
    public Player enemyPlayer;

    public gameRecord(){

    }

    public gameRecord(ArrayList<String> gameMoves, String gameResult, Player enemyPlayer){
        this.gameMoves = gameMoves;
        this.gameResult = gameResult;
        this.enemyPlayer = enemyPlayer;
    }

}
