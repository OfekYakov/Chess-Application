package com.example.chessproject.LeaderBoard;

import android.graphics.Bitmap;

public class LeaderBoardItem {

     public String UserName;
     public int UserScore;
     public Bitmap UserPicture;

    public LeaderBoardItem(String userName, int userScore, Bitmap userPicture) {
        UserName = userName;
        UserScore = userScore;
        UserPicture = userPicture;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserScore() {
        return UserScore;
    }

    public void setUserScore(int userScore) {
        UserScore = userScore;
    }

    public Bitmap getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(Bitmap userPicture) {
        UserPicture = userPicture;
    }
}
