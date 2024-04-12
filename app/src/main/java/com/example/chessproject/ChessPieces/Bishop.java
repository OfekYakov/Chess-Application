package com.example.chessproject.ChessPieces;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Coordinate;
import com.example.chessproject.BoardUtilities.Move;
import com.example.chessproject.BoardUtilities.Tile;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public ArrayList<Move> calculateLegalMoves(Coordinate pieceCoordinate, Tile[][] pieces_Array) {

        ArrayList<Move> candidateLegalMoves = new ArrayList<>();

        // up - left
        for (int k = 1; k < pieces_Array.length; k++){
            if(pieceCoordinate.getI() - k < 0 || pieceCoordinate.getJ() - k < 0){
                break;
            }
            if(pieces_Array[pieceCoordinate.getI() - k][pieceCoordinate.getJ() - k].getPieceOnTile() != null){
                if (pieces_Array[pieceCoordinate.getI() - k][pieceCoordinate.getJ() - k].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - k , pieceCoordinate.getJ() - k)));
                }
                break;
            }
            candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - k , pieceCoordinate.getJ() - k)));
        }

        // up right
        for (int k = 1; k < pieces_Array.length; k++){
            if(pieceCoordinate.getI() - k < 0 || pieceCoordinate.getJ() + k == pieces_Array.length){
                break;
            }
            if(pieces_Array[pieceCoordinate.getI() - k][pieceCoordinate.getJ() + k].getPieceOnTile() != null) {
                if (pieces_Array[pieceCoordinate.getI() - k][pieceCoordinate.getJ() + k].getPieceOnTile().getColor() != this.getColor()) {
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - k, pieceCoordinate.getJ() + k)));
                }
                break;
            }
            candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - k , pieceCoordinate.getJ() + k)));
        }

        // down - left
        for (int k = 1; k < pieces_Array.length; k++){
            if(pieceCoordinate.getI() + k == pieces_Array.length || pieceCoordinate.getJ() - k < 0){
                break;
            }

            if(pieces_Array[pieceCoordinate.getI() + k][pieceCoordinate.getJ() - k].getPieceOnTile() != null) {
                if (pieces_Array[pieceCoordinate.getI() + k][pieceCoordinate.getJ() - k].getPieceOnTile().getColor() != this.getColor()) {
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + k, pieceCoordinate.getJ() - k)));
                }
                break;
            }
            candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + k , pieceCoordinate.getJ() - k)));
        }


        // down right
        for(int k = 1; k < pieces_Array.length; k++){
            if(pieceCoordinate.getI() + k == pieces_Array.length || pieceCoordinate.getJ() + k == pieces_Array.length){
                break;
            }
            if(pieces_Array[pieceCoordinate.getI() + k][pieceCoordinate.getJ() + k].getPieceOnTile() != null){
                if(pieces_Array[pieceCoordinate.getI() + k][pieceCoordinate.getJ() + k].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + k, pieceCoordinate.getJ() + k)));
                }
                break;
            }
            candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + k, pieceCoordinate.getJ() + k)));
        }

        return candidateLegalMoves;
    }


}
