package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chessproject.BoardUtilities.Board;
import com.example.chessproject.BoardUtilities.BoardUtils;
import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.R;
import com.example.chessproject.UserUtils.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShowPreviousGameActivity extends AppCompatActivity {

    ImageView btnNext ,btnPrevious;
    recordedGameCanvas gameCanvas;
    LinearLayout chessLayout;
    ImageView UserProfilePic, EnemyProfilePic;
    TextView UserUsername, EnemyUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_previous_game);

        EnemyProfilePic = findViewById(R.id.EnemyProfilePic);
        UserProfilePic = findViewById(R.id.UserProfilePic);
        UserUsername = findViewById(R.id.UserUserName);
        EnemyUserName = findViewById(R.id.EnemyUserName);

        // set user details
        UserUsername.setText(UserData.username);
        UserProfilePic.setImageBitmap(UserData.bitmap == null ? BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_profile_picture) : UserData.bitmap);

        // set enemy details

        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("EnemyUID")).
                child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EnemyUserName.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        getEnemyProfilePicture();

        chessLayout = findViewById(R.id.chessLayout);
        chessLayout.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth(), getScreenWidth()));
        gameCanvas = new recordedGameCanvas(this, getIntent().getStringArrayListExtra("gameBoardPositions"), getIntent().getStringExtra("UserColor"), getScreenWidth());
        chessLayout.addView(gameCanvas);

        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameCanvas.onClickNext();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameCanvas.onPreviousClick();
            }
        });
    }

    public void getEnemyProfilePicture(){
        StorageReference imageReference =  FirebaseStorage.getInstance().getReference().child("Images/" + getIntent().getStringExtra("EnemyUID"));
        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                EnemyProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // set default image from drawable
                EnemyProfilePic.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture));
            }
        });
    }

    public int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public class recordedGameCanvas extends View{

        ArrayList<String> allGamesPositions;
        Color playerColor;
        Board game_board;
        int positionCounter;
        int screenWidth;
        int boardColorID;
        Context context;
        public recordedGameCanvas(Context context, ArrayList<String> allGamesPositions, String color, int screenWidth) {
            super(context);
            this.context = context;
            this.positionCounter = 0;
            this.screenWidth = screenWidth;
            this.allGamesPositions = allGamesPositions;
            playerColor = color.equals("black") ? Color.BLACK : Color.WHITE;
            this.game_board = new Board(playerColor);
            SharedPreferences sharedPreferences = context.getSharedPreferences("board_color", 0);
            switch (sharedPreferences.getString("color", "brown")){
                case "brown":
                    boardColorID = R.color.board_brown;
                    break;
                case "blue":
                    boardColorID = R.color.board_Blue;
                    break;
                case "green":
                    boardColorID = R.color.board_Green;
                    break;
            }
            BoardUtils.FEN_To_Board(game_board.getPiece_array(), allGamesPositions.get(positionCounter));
        }

        public void onClickNext(){
            if (this.positionCounter >= allGamesPositions.size() - 1)
                Toast.makeText(context, "No more moves to show", Toast.LENGTH_SHORT).show();
            else{
                positionCounter++;
                BoardUtils.FEN_To_Board(game_board.getPiece_array(), allGamesPositions.get(positionCounter));
                invalidate();
            }
        }

        public void onPreviousClick(){
            if (this.positionCounter <= 0)
                Toast.makeText(context, "Start position", Toast.LENGTH_SHORT).show();
            else{
                positionCounter--;
                BoardUtils.FEN_To_Board(game_board.getPiece_array(), allGamesPositions.get(positionCounter));
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (playerColor == Color.BLACK)
                drawBlackViewBoard(canvas);
            else if (playerColor == Color.WHITE)
                drawWhiteViewBoard(canvas);
        }

        public void drawBlackViewBoard(Canvas canvas){
            for (int i = 0; i < game_board.getPiece_array().length; i++){
                for (int j = 0; j < game_board.getPiece_array().length; j++){
                    Rect tile = new Rect(screenWidth/8 * j , screenWidth/8 * i,  screenWidth/8 * j + screenWidth/8 , screenWidth/8 * i + screenWidth/8);
                    Paint paint = new Paint();
                    if (((i + j) % 2) == 0)
                        paint.setColor(getResources().getColor(R.color.board_cream));
                    else
                        paint.setColor(getResources().getColor(boardColorID));
                    canvas.drawRect(tile, paint);
                    if (game_board.getPiece_array()[game_board.getPiece_array().length - i - 1][game_board.getPiece_array().length - j - 1].getPieceOnTile() != null){
                        Bitmap picHolder = BoardUtils.Piece_To_Picture(game_board.getPiece_array()[game_board.getPiece_array().length - i - 1][game_board.getPiece_array().length - j - 1].getPieceOnTile(), screenWidth/8, getResources());
                        canvas.drawBitmap(picHolder, screenWidth/8 * j, screenWidth/8 * i , paint);
                    }
                }
            }
        }

        public void drawWhiteViewBoard(Canvas canvas){
            for (int i = 0; i < game_board.getPiece_array().length; i++){
                for (int j = 0; j < game_board.getPiece_array().length; j++){
                    Rect tile = new Rect(screenWidth/8 * j , screenWidth/8 * i,  screenWidth/8 * j + screenWidth/8 , screenWidth/8 * i + screenWidth/8);
                    Paint paint = new Paint();
                    if (((i + j) % 2) == 0)
                        paint.setColor(getResources().getColor(R.color.board_cream));
                    else
                        paint.setColor(getResources().getColor(boardColorID));
                    canvas.drawRect(tile, paint);
                    if (game_board.getPiece_array()[i][j].getPieceOnTile() != null){
                        Bitmap picHolder = BoardUtils.Piece_To_Picture(game_board.getPiece_array()[i][j].getPieceOnTile(), screenWidth/8, getResources());
                        canvas.drawBitmap(picHolder, screenWidth/8 * j, screenWidth/8 * i , paint);
                    }
                }
            }
        }
    }
}