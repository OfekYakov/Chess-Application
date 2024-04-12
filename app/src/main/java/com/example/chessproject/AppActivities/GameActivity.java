package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chessproject.BoardUtilities.ChessCanvas;
import com.example.chessproject.R;
import com.example.chessproject.BoardUtilities.Room;
import com.example.chessproject.UserUtils.User;
import com.example.chessproject.UserUtils.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GameActivity extends AppCompatActivity {

    ImageView UserProfilePic, EnemyProfilePic;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference roomReference;
    Room playRoom;
    LinearLayout chessLayout;
    ChessCanvas chessCanvas;
    TextView UserUsername, EnemyUserName;
    String enemyUID;
    TextView enemyTimerView, userTimerView, userPoints, enemyPoints;
    Dialog endGameDialog;
    Button dialogExitBtn;
    AlertDialog quitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        firebaseDatabase = FirebaseDatabase.getInstance();
        roomReference = firebaseDatabase.getReference().child("Rooms").child("PlayingRooms").child(getIntent().getExtras().getString("roomKey"));
        roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get a reference to the playing room
                playRoom = snapshot.getValue(Room.class);
                // update the ui after retrieve data has finished
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void updateUI(){

        enemyUID = getIntent().getStringExtra("UserColor").equals("black") ? playRoom.player1.uid : playRoom.player2.uid;

        enemyTimerView = findViewById(R.id.enemyTimerView);
        userTimerView = findViewById(R.id.userTimerView);
        enemyPoints = findViewById(R.id.enemyPoints);
        userPoints = findViewById(R.id.userPoints);
        chessLayout = findViewById(R.id.chessLayout);
        chessLayout.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth(), getScreenWidth()));
        chessCanvas = new ChessCanvas(this, getScreenWidth(), getIntent().getExtras().getString("roomKey"), getIntent().getStringExtra("UserColor"), userTimerView, enemyTimerView);
        chessLayout.addView(chessCanvas);

        UserProfilePic = findViewById(R.id.UserProfilePic);
        EnemyProfilePic = findViewById(R.id.EnemyProfilePic);
        setPlayersProfilePic();

        UserUsername = findViewById(R.id.UserUserName);
        EnemyUserName = findViewById(R.id.EnemyUserName);
        setPlayersData();


    }

    public void setPlayersProfilePic(){
        if (UserData.bitmap != null)
            UserProfilePic.setImageBitmap(UserData.bitmap);

        // get enemy profile pic from storage

        StorageReference imageReference =  FirebaseStorage.getInstance().getReference().child("Images/" + enemyUID);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                EnemyProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void setPlayersData(){
        UserUsername.setText(UserData.username);
        userPoints.setText("(" + UserData.ChessScore + ")");
        // get enemy's username from database
        FirebaseDatabase.getInstance().getReference().child("Users").child(enemyUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User enemy = snapshot.getValue(User.class);
                EnemyUserName.setText(enemy.username);
                enemyPoints.setText("(" + enemy.ChessScore + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void userLost(){

        if (UserData.ChessScore >= 30) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore - 30);
        }
        createLossDialog();
    }

    public void createLossDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.loss_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                roomReference.removeValue();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " - 30 = " + (UserData.ChessScore - 30));
        endGameDialog.show();
    }
    public void userWon(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore + 30);
        createWinDialog();
        roomReference.removeValue();
    }

    public void createWinDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.win_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " + 30 = " + (UserData.ChessScore + 30));
        endGameDialog.show();
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    public void showQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Quit alert");
        builder.setMessage("You're about to exit the game quit it and lose points!" + '\n'+ '\n' + "Are you sure you want to quit the game?");
        builder.setNegativeButton("Stay", (dialog, which) -> {quitDialog.dismiss();});
        builder.setPositiveButton("Quit", (dialog, which)-> {
            userQuit();
            quitDialog.dismiss();
            super.onBackPressed();
        });
        quitDialog = builder.create();
        quitDialog.show();
    }

    public void userQuit(){
        chessCanvas.updateUserQuit();
        if (UserData.ChessScore >= 30) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore - 30);
        }

    }


    public void playerWonByQuit(){
        roomReference.removeValue();
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore + 30);
        createWinByQuitDialog();
    }

    public void createWinByQuitDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.win_by_quit_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " + 30 = " + (UserData.ChessScore + 30));
        endGameDialog.show();
    }

    public void draw(){

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore + 15);
        showDrawDialog();
        // makes sure that only one player will delete the room
        if (getIntent().getStringExtra("UserColor").equals("white"))
            roomReference.removeValue();
    }

    public void showDrawDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.draw_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " + 15 = " + (UserData.ChessScore + 15));
        endGameDialog.show();
    }

    public void userRunOutOfTime(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore - 30);
        showUserRunOutOfTimeDialog();
    }

    public void enemyRunOutOfTime(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("ChessScore").setValue(UserData.ChessScore + 30);
        showEnemyRunOutOfTimeDialog();
        roomReference.removeValue();
    }

    public void showEnemyRunOutOfTimeDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.enemy_out_of_time_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " + 30 = " + (UserData.ChessScore + 30));
        endGameDialog.show();
    }

    public void showUserRunOutOfTimeDialog(){
        endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.user_out_of_time_dialog);
        endGameDialog.setCancelable(false);
        dialogExitBtn = endGameDialog.findViewById(R.id.exitButton);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGameDialog.dismiss();
                GameActivity.this.finish();
            }
        });
        TextView pointsView = endGameDialog.findViewById(R.id.pointsView);
        pointsView.setText("Current point: " + UserData.ChessScore + " - 30 = " + (UserData.ChessScore -30 ));
        endGameDialog.show();
    }

    public void stopGameTimer(){
        chessCanvas.gameTimer.isUserTimerRunning = false;
        chessCanvas.gameTimer.isEnemyTimerRunning = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (endGameDialog != null) {
            endGameDialog.dismiss();
        }
        // stop async task
        stopGameTimer();
    }
}