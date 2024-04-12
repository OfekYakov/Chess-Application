package com.example.chessproject.BoardUtilities;

import com.example.chessproject.ChessPieces.King;
import com.example.chessproject.ChessPieces.Piece;

import java.util.ArrayList;

public class Board {
    private Tile[][] piece_array = new Tile[8][8];

    // a private attribute that helps the board to know what player is it
    public Color playerColor;

    public Board(Color playerColor) {
        this.playerColor = playerColor;
        for(int i = 0; i < piece_array.length; i++){
            for(int j = 0; j < piece_array[0].length; j++){
                piece_array[i][j] = new Tile(null);
            }
        }
    }
    public Tile[][] getPiece_array() {
        return piece_array;
    }

    public void applyMove(Move moveToApply){
        piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].getPieceOnTile());
        piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile(null);

    }

    
    // check if the king under check
    public boolean isKingUnderCheck() {

        ArrayList<ArrayList<Move>> AllEnemyCandidateMoves = new ArrayList<>();
        Coordinate kingCoordinate = null;

        for (int i = 0; i < piece_array.length; i++) {
            for (int j = 0; j < piece_array[i].length; j++) {
                // All opponents moves
                if (piece_array[i][j].getPieceOnTile() != null && piece_array[i][j].getPieceOnTile().getColor() != playerColor) {
                    AllEnemyCandidateMoves.add(piece_array[i][j].getPieceOnTile().calculateLegalMoves(new Coordinate(i, j), piece_array));
                }
                // find king coordinate
                if (piece_array[i][j].getPieceOnTile() instanceof King) {
                    if (piece_array[i][j].getPieceOnTile().getColor() == playerColor) {
                        kingCoordinate = new Coordinate(i, j);
                    }
                }
            }
        }
        if (kingCoordinate == null){
            return false;
        }

        for (ArrayList<Move> attackList : AllEnemyCandidateMoves){
            for (Move move : attackList){
                if (Coordinate.isCoordinateEquals(move.getCandidateCoordinate(), kingCoordinate)){
                    return  true;
                }
            }
        }
        return false;
    }

    // check if checkmate
    public boolean isCheckMate(){
        return isKingUnderCheck() && !AnyPieceCanCancelCheck();
    }

    // check if draw
    public boolean isDraw(){
        Coordinate kingPlacement = null;
        for (int i = 0; i < piece_array.length; i++){
            for (int j = 0; j < piece_array[i].length; j++){
                if (piece_array[i][j].getPieceOnTile() != null){
                    if (piece_array[i][j].getPieceOnTile() instanceof King && piece_array[i][j].getPieceOnTile().getColor() == playerColor){
                        kingPlacement = new Coordinate(i, j);
                    }
                }
            }
        }
        if (kingPlacement == null)
            return false;
        return !isKingUnderCheck() && this.returnLegalMovesToCanvas(piece_array[kingPlacement.getI()][kingPlacement.getJ()].getPieceOnTile() ,kingPlacement) == null;
    }

    private boolean AnyPieceCanCancelCheck() {
        // get list of all player in the same alliance
        ArrayList<ArrayList<Move>> allDefenceMoves = new ArrayList<>();
        for (int i = 0; i < piece_array.length; i++){
            for (int j = 0; j < piece_array[i].length; j++){
                if (piece_array[i][j].getPieceOnTile() != null && piece_array[i][j].getPieceOnTile().getColor() == playerColor){
                    allDefenceMoves.add(piece_array[i][j].getPieceOnTile().calculateLegalMoves(new Coordinate(i, j), piece_array));
                }
            }
        }

        for (ArrayList<Move> DefenseMoves : allDefenceMoves){
            for (Move move : DefenseMoves){
                if (DoesMoveDefense(move)){
                    return true;
                }
            }
        }
        return false;
    }

    // check if certain piece can cancel a check
    private ArrayList<Move> canCertainPieceCancelCheck(Piece piece, Coordinate coordinate){
        ArrayList<Move> allDefenseMoves = new ArrayList<>();
        for (Move move : piece.calculateLegalMoves(coordinate, piece_array)){
            if (DoesMoveDefense(move)){
                allDefenseMoves.add(move);
            }
        }
        return allDefenseMoves;
    }


    // a method that get a piece from chessCanvas and return moves of the piece
    public ArrayList<Move> returnLegalMovesToCanvas(Piece piece, Coordinate coordinate){

        if (isKingUnderCheck()){
            return canCertainPieceCancelCheck(piece, coordinate);
        }
        ArrayList<Move> movesToReturn = piece.calculateLegalMoves(coordinate, piece_array);
        filterLegalMoves(movesToReturn);
        return movesToReturn;
    }

    // used in returnLegalMoves in order to filter all the moves that cause check
    private void filterLegalMoves(ArrayList<Move> currentLegalMoves){
        ArrayList<Move> moveToRemove = new ArrayList<>();
        for (Move move : currentLegalMoves){
            if (DoesMoveRevealKingToCheck(move)){
                moveToRemove.add(move);
            }
        }
        currentLegalMoves.removeAll(moveToRemove);
    }

    // used in filterLegalMoves
    private boolean DoesMoveRevealKingToCheck(Move moveToApply){
        Piece attackedPiece = piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile();

        // make the move
        piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].getPieceOnTile());
        piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile(null);

        // un make the move

        if (isKingUnderCheck()){
            piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile());
            piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile(attackedPiece);
            return true;
        }
        piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile());
        piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile(attackedPiece);
        return false;
    }



    // make and unmake move
    private boolean DoesMoveDefense(Move moveToApply){

        Piece attackedPiece = piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile();

        piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].getPieceOnTile());
        piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile(null);

        if (isKingUnderCheck()){
            // un make the move
            piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile());
            piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile(attackedPiece);
            return false;
        }
        // un make the move
        piece_array[moveToApply.getStartCoordinate().getI()][moveToApply.getStartCoordinate().getJ()].setPieceOnTile( piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].getPieceOnTile());
        piece_array[moveToApply.getCandidateCoordinate().getI()][moveToApply.getCandidateCoordinate().getJ()].setPieceOnTile(attackedPiece);
        return true;

    }

}
