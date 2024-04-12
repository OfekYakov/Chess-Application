package com.example.chessproject.BoardUtilities;

import android.os.AsyncTask;
import android.widget.TextView;

public class TimerTask extends AsyncTask <Integer, Integer, String>{

    public TextView userTimerView, enemyTimerView;
    public boolean isUserTimerRunning, isEnemyTimerRunning;
    int userSecondsCounter, enemySecondsCounter;
    ChessCanvas chessCanvasReference;

    public TimerTask(TextView userTimerView, TextView enemyTimerView, boolean isUserTimerRunning, boolean isEnemyTimerRunning, ChessCanvas chessCanvas){
        this.userTimerView = userTimerView;
        this.enemyTimerView = enemyTimerView;
        this.isUserTimerRunning = isUserTimerRunning;
        this.isEnemyTimerRunning = isEnemyTimerRunning;
        this.chessCanvasReference = chessCanvas;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... integers) {
         this.userSecondsCounter = integers[0];
         this.enemySecondsCounter = integers[1];


        while (isEnemyTimerRunning || isUserTimerRunning){
            if (isUserTimerRunning){
                if (this.userSecondsCounter > 0) {
                    try {
                        Thread.sleep(1000);
                        userSecondsCounter--;
                        if (userSecondsCounter == 0){
                            // publish progress because changes in the UI cannot be made inside of doInBackground
                            publishProgress(-1);
                        }
                        publishProgress(this.userSecondsCounter);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            else if (isEnemyTimerRunning) {
                if (this.enemySecondsCounter > 0) {
                    try {
                        Thread.sleep(1000);
                        this.enemySecondsCounter--;
                        publishProgress(this.enemySecondsCounter);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return "time is up";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (values[0] == -1) {
            // enemy runs out of time
            chessCanvasReference.updateTimeIsUp();
        }

        if (isUserTimerRunning){
            userTimerView.setText(values[0]/60 + ":" + values[0] % 60);
        }
        else if (isEnemyTimerRunning){
            enemyTimerView.setText(values[0]/60 + ":" + values[0] % 60);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
