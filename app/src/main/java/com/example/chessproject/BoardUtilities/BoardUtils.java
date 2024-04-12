package com.example.chessproject.BoardUtilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.chessproject.ChessPieces.Bishop;
import com.example.chessproject.ChessPieces.King;
import com.example.chessproject.ChessPieces.Knight;
import com.example.chessproject.ChessPieces.Pawn;
import com.example.chessproject.ChessPieces.Piece;
import com.example.chessproject.ChessPieces.Queen;
import com.example.chessproject.R;
import com.example.chessproject.ChessPieces.Rook;

public class BoardUtils {

    public static Bitmap Piece_To_Picture(Piece piece, int picSize, Resources resources){

        Bitmap returnBitmap = null;

        if(piece instanceof King){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_king);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_king);
            }
        }

        else if(piece instanceof Queen){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_queen);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_queen);
            }
        }

        else if(piece instanceof Bishop){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_bishop);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_bishop);
            }
        }

        else if(piece instanceof Rook){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_rook);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_rook);
            }
        }

        else if(piece instanceof Pawn){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_pawn);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_pawn);
            }
        }

        else if (piece instanceof Knight){
            if(piece.getColor() == Color.WHITE){
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.white_knight);
            }
            else{
                returnBitmap = BitmapFactory.decodeResource(resources, R.drawable.black_knight);
            }
        }


        return Bitmap.createScaledBitmap(returnBitmap, picSize, picSize, false);

    }


    public static Piece char_To_Piece(char piece_Char){

        Piece piece = null;

        if(piece_Char == 'K'){
            piece = new King(Color.WHITE);
        }
        else if(piece_Char == 'k'){
            piece = new King(Color.BLACK);
        }
        else if (piece_Char == 'Q'){
            piece = new Queen(Color.WHITE);
        }
        else if (piece_Char == 'q'){
            piece = new Queen(Color.BLACK);
        }
        else if (piece_Char == 'R'){
            piece = new Rook(Color.WHITE);
        }
        else if (piece_Char == 'r'){
            piece = new Rook(Color.BLACK);
        }
        else if (piece_Char == 'B'){
            piece = new Bishop(Color.WHITE);
        }
        else if (piece_Char == 'b'){
            piece = new Bishop(Color.BLACK);
        }
        else if(piece_Char == 'N'){
            piece = new Knight(Color.WHITE);
        }
        else if (piece_Char == 'n'){
            piece = new Knight(Color.BLACK);
        }
        else if(piece_Char == 'P'){
            piece = new Pawn(Color.WHITE);
        }
        else if(piece_Char == 'p'){
            piece = new Pawn(Color.BLACK);
        }

        return piece;
    }

    public static void FEN_To_Board(Tile[][] board_array, String fen){
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
        // LENGTH = 8 + 1 + 8 + 8 + 1 + 8 + 1 + 8
        int digit_count = 0;
        int string_count = 0;

       for(int i = 0; i < board_array.length; i++){
           for(int j = 0; j < board_array[i].length; j++) {
               if(digit_count > 0){
                   board_array[i][j].setPieceOnTile(null);
                   digit_count--;
                   continue;
               }
               // can move to the next char in the given fen string

               if(fen.charAt(string_count) == '/'){
                   string_count++;
               }

               if((fen.charAt(string_count) >= 'a' && fen.charAt(string_count) <= 'z') || (fen.charAt(string_count) >= 'A' && fen.charAt(string_count) <= 'Z')){
                   board_array[i][j].setPieceOnTile(char_To_Piece(fen.charAt(string_count)));
                   string_count++;
               }

               else if ((fen.charAt(string_count) >= '0' && fen.charAt(string_count) <= '9')){
                   digit_count = fen.charAt(string_count) - '0' - 1;
                   board_array[i][j].setPieceOnTile(null);
                   string_count++;
               }
           }
       }
    }

    public static char Piece_To_Char(Piece piece){

        if(piece instanceof King){
            if(piece.getColor() == Color.WHITE){
               return 'K';
            }
            else{
                return 'k';
            }
        }

        else if(piece instanceof Queen){
            if(piece.getColor() == Color.WHITE){
                return 'Q';
            }
            else{
                return 'q';
            }
        }

        else if(piece instanceof Bishop){
            if(piece.getColor() == Color.WHITE){
                return 'B';
            }
            else{
                return 'b';
            }
        }

        else if(piece instanceof Rook){
            if(piece.getColor() == Color.WHITE){
                return 'R';
            }
            else{
                return 'r';
            }
        }

        else if(piece instanceof Pawn){
            if(piece.getColor() == Color.WHITE){
                return 'P';
            }
            else{
                return 'p';
            }
        }

        else if (piece instanceof Knight){
            if(piece.getColor() == Color.WHITE){
                return 'N';
            }
            else{
                return 'n';
            }
        }

        return ' ';
    }

    public static Coordinate convertToCoordinateForWhitePlayer(float x, float y, int screenWidth){
        return new Coordinate((int)y / (screenWidth/8) , (int)x / (screenWidth/8));
    }

    public static Coordinate convertToCoordinateForBlackPlayer(float x, float y, int screenWidth){
        return new Coordinate((int)(screenWidth - y) / (screenWidth/8) , (int)(screenWidth - x) / (screenWidth/8));
    }

    public static String board_To_FEN(Tile[][] game_array){
        String FEN_RETURN = "";
        int empty_counter = 0;

        for (int i =0; i < game_array.length; i++){
            for (int j = 0; j < game_array[i].length; j++) {
                if (game_array[i][j].getPieceOnTile() == null){
                    empty_counter++;
                }
                else if (game_array[i][j].getPieceOnTile() != null){
                    if (empty_counter != 0) {
                        FEN_RETURN += String.valueOf(empty_counter);
                        empty_counter = 0;
                    }
                    FEN_RETURN += String.valueOf(Piece_To_Char(game_array[i][j].getPieceOnTile()));
                }
            }
            if (empty_counter != 0){
                FEN_RETURN += String.valueOf(empty_counter);
                empty_counter = 0;
            }
            if (i < game_array.length - 1) {
                FEN_RETURN += '/';
            }
        }
        return FEN_RETURN;
    }

    // a class that cannot be instantiate, a class of functions only
    public BoardUtils(){
        throw new RuntimeException("You cannot instantiate me");
    }
}
