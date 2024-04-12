package com.example.chessproject.BoardUtilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chessproject.AppActivities.GameActivity;
import com.example.chessproject.R;
import com.example.chessproject.GamesRecords.gameRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChessCanvas extends View {

    public TimerTask gameTimer;
    boolean playerClickOnce;
    Context context;
    Board game_board;
    int screenWidth;
    boolean isPlayerTurn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference playRoomReference;
    Color playerColor;
    ArrayList<Move> candidateLegalMoves;
    TextView userTimerView, enemyTimerView;
    Move playerMoveToApply = new Move(null, null);
    ValueEventListener playRoomListener;
    ArrayList<String> allGameMoves;
    int boardColorID;
    Player enemyPlayer;

    public ChessCanvas(Context context, int screenWidth, String roomKey, String color, TextView userTimerView, TextView enemyTimerView) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        getBoardColor();
        playerClickOnce = false;
        allGameMoves = new ArrayList<>();
        this.userTimerView = userTimerView;
        this.enemyTimerView = enemyTimerView;
        if (color.equals("black")){
            playerColor = Color.BLACK;
            gameTimer = new TimerTask(userTimerView, enemyTimerView, false, true, this);
        }
        else if (color.equals("white")){
            gameTimer = new TimerTask(userTimerView, enemyTimerView, true, false, this);
            playerColor = Color.WHITE;
        }
        gameTimer.execute(60 * 15, 60 * 15);
        isPlayerTurn = color.equals("black") ? false : true;
        game_board = new Board(playerColor);
        firebaseDatabase = FirebaseDatabase.getInstance();
        playRoomReference = firebaseDatabase.getReference("Rooms").child("PlayingRooms").child(roomKey);
        playRoomListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room playRoom = snapshot.getValue(Room.class);
                if (playRoom.boardFEN.equals(getResources().getString(R.string.StartBoardFEN))) {
                    // gets the enemy player once at the start in order to pass it to game record
                    enemyPlayer = playerColor == Color.WHITE ? playRoom.player2 : playRoom.player1;
                }

                if (playRoom.boardFEN.equals("enemy run out of time")){
                    playRoomReference.removeEventListener(this);
                    FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "enemy run out of time", enemyPlayer));
                    ((GameActivity)context).enemyRunOutOfTime();
                }

                if (playRoom.boardFEN.equals("enemy quit")) {
                    playRoomReference.removeEventListener(this);
                    FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "enemy quit", enemyPlayer));
                    ((GameActivity) context).playerWonByQuit();
                }

                BoardUtils.FEN_To_Board(game_board.getPiece_array(), playRoom.boardFEN);
                allGameMoves.add(playRoom.boardFEN);
                // if not start of the game;
                if (!playRoom.boardFEN.equals(getResources().getString(R.string.StartBoardFEN))) {
                    isPlayerTurn = !isPlayerTurn;
                    gameTimer.isEnemyTimerRunning = !gameTimer.isEnemyTimerRunning;
                    gameTimer.isUserTimerRunning = !gameTimer.isUserTimerRunning;
                }
                invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        playRoomReference.addValueEventListener(playRoomListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (playerColor == Color.WHITE) {
            drawWhiteViewBoard(canvas);
        } else {
            drawBlackViewBoard(canvas);
        }
        if (candidateLegalMoves != null)
            displayCandidateCoordinate(canvas);

        if (game_board.isDraw()) {
            FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "draw", enemyPlayer));
            playRoomReference.removeEventListener(playRoomListener);
            ((GameActivity) context).draw();
        } else {
            // check if enemy under draw
            game_board.playerColor = playerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
            if (game_board.isDraw()) {
                FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "draw", enemyPlayer));
                playRoomReference.removeEventListener(playRoomListener);
                ((GameActivity) context).draw();
            }
            game_board.playerColor = playerColor == Color.WHITE ? Color.WHITE : Color.BLACK;
        }

        // check if user under checkmate
        if (game_board.isCheckMate()) {
            FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "loss", enemyPlayer));
            playRoomReference.removeEventListener(playRoomListener);
            ((GameActivity) context).userLost();
        } else {
            // check if user won
            game_board.playerColor = (playerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
            // if enemy under checkmate
            if (game_board.isCheckMate()) {
                FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "win", enemyPlayer));
                playRoomReference.removeEventListener(playRoomListener);
                ((GameActivity) context).userWon();
            } else {
                game_board.playerColor = (playerColor == Color.WHITE) ? Color.WHITE : Color.BLACK;
            }
        }

    }

    public void getBoardColor() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("board_color", 0);
        String boardColor = sharedPreferences.getString("color", "brown");
        switch (boardColor) {
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
    }

    public void drawWhiteViewBoard(Canvas canvas) {
        for (int i = 0; i < game_board.getPiece_array().length; i++) {
            for (int j = 0; j < game_board.getPiece_array().length; j++) {
                Rect tile = new Rect(screenWidth / 8 * j, screenWidth / 8 * i, screenWidth / 8 * j + screenWidth / 8, screenWidth / 8 * i + screenWidth / 8);
                Paint paint = new Paint();
                if (((i + j) % 2) == 0)
                    paint.setColor(getResources().getColor(R.color.board_cream));
                else
                    paint.setColor(getResources().getColor(boardColorID));
                canvas.drawRect(tile, paint);
                if (game_board.getPiece_array()[i][j].getPieceOnTile() != null) {
                    Bitmap picHolder = BoardUtils.Piece_To_Picture(game_board.getPiece_array()[i][j].getPieceOnTile(), screenWidth / 8, getResources());
                    canvas.drawBitmap(picHolder, screenWidth / 8 * j, screenWidth / 8 * i, paint);
                }
            }
        }
    }

    public void drawBlackViewBoard(Canvas canvas) {
        for (int i = 0; i < game_board.getPiece_array().length; i++) {
            for (int j = 0; j < game_board.getPiece_array().length; j++) {
                Rect tile = new Rect(screenWidth / 8 * j, screenWidth / 8 * i, screenWidth / 8 * j + screenWidth / 8, screenWidth / 8 * i + screenWidth / 8);
                Paint paint = new Paint();
                if (((i + j) % 2) == 0)
                    paint.setColor(getResources().getColor(R.color.board_cream));
                else
                    paint.setColor(getResources().getColor(boardColorID));
                canvas.drawRect(tile, paint);
                if (game_board.getPiece_array()[game_board.getPiece_array().length - i - 1][game_board.getPiece_array().length - j - 1].getPieceOnTile() != null) {
                    Bitmap picHolder = BoardUtils.Piece_To_Picture(game_board.getPiece_array()[game_board.getPiece_array().length - i - 1][game_board.getPiece_array().length - j - 1].getPieceOnTile(), screenWidth / 8, getResources());
                    canvas.drawBitmap(picHolder, screenWidth / 8 * j, screenWidth / 8 * i, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                clickTheBoard(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    public void clickTheBoard(float x, float y) {
        if (!isPlayerTurn)
            return;

        Coordinate clickCoordinate = playerColor == Color.BLACK ? BoardUtils.convertToCoordinateForBlackPlayer(x, y, screenWidth)
                : BoardUtils.convertToCoordinateForWhitePlayer(x, y, screenWidth);

        if (playerClickOnce == false) {
            if (game_board.getPiece_array()[clickCoordinate.getI()][clickCoordinate.getJ()].getPieceOnTile() != null && game_board.getPiece_array()[clickCoordinate.getI()][clickCoordinate.getJ()].getPieceOnTile().getColor() == playerColor) {
                candidateLegalMoves = game_board.returnLegalMovesToCanvas(game_board.getPiece_array()[clickCoordinate.getI()][clickCoordinate.getJ()].getPieceOnTile(), clickCoordinate);
                playerClickOnce = true;
                playerMoveToApply.setStartCoordinate(clickCoordinate);
                invalidate();
            }
        } else if (playerClickOnce == true) {
            if (game_board.getPiece_array()[clickCoordinate.getI()][clickCoordinate.getJ()].getPieceOnTile() != null && game_board.getPiece_array()[clickCoordinate.getI()][clickCoordinate.getJ()].getPieceOnTile().getColor() == playerColor) {
                playerClickOnce = false;
                clickTheBoard(x, y);
            } else {
                if (isValidMove(clickCoordinate)) {
                    playerClickOnce = false;
                    playerMoveToApply.setCandidateCoordinate(clickCoordinate);
                    game_board.applyMove(playerMoveToApply);
                    // checks if user won
                    candidateLegalMoves = null;
                    playRoomReference.child("boardFEN").setValue(BoardUtils.board_To_FEN(game_board.getPiece_array()));
                }
            }
        }
    }

    public void updateUserQuit() {
        FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "user quit", enemyPlayer));
        playRoomReference.removeEventListener(playRoomListener);
        playRoomReference.child("boardFEN").setValue("enemy quit");
    }

    public void updateTimeIsUp(){
        playRoomReference.removeEventListener(playRoomListener);
        playRoomReference.child("boardFEN").setValue("enemy run out of time");
        FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).push().setValue(new gameRecord(allGameMoves, "run out of time", enemyPlayer));
        ((GameActivity)context).userRunOutOfTime();
    }

    public boolean isValidMove(Coordinate clickCoordinate) {
        for (Move move : candidateLegalMoves) {
            if (Coordinate.isCoordinateEquals(move.getCandidateCoordinate(), clickCoordinate))
                return true;
        }
        return false;
    }

        public void displayCandidateCoordinate(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.candidateMoveColor));
        if (playerColor == Color.WHITE) {
            for (Move move : candidateLegalMoves) {
                canvas.drawCircle(move.getCandidateCoordinate().getJ() * screenWidth / 8 + screenWidth / 16, move.getCandidateCoordinate().getI() * screenWidth / 8 + screenWidth / 16, screenWidth / 64, paint);
            }
        } else {
            for (Move move : candidateLegalMoves) {
                canvas.drawCircle((int) screenWidth - (int) (move.getCandidateCoordinate().getJ() * screenWidth / 8 + screenWidth / 16), (int) screenWidth - (int) (move.getCandidateCoordinate().getI() * screenWidth / 8 + screenWidth / 16), screenWidth / 64, paint);
            }
        }
    }
}













