package com.example.chessproject.ChessPieces;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Coordinate;
import com.example.chessproject.BoardUtilities.Move;
import com.example.chessproject.BoardUtilities.Tile;

import java.util.ArrayList;

public class Pawn extends Piece{

    public Pawn(Color color) {
        super(color);
    }


    @Override
    public ArrayList<Move> calculateLegalMoves(Coordinate pieceCoordinate, Tile[][] pieces_Array) {
        ArrayList<Move> candidateLegalMoves = new ArrayList<>();

        if(this.color == Color.BLACK) {
            if(pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ()].getPieceOnTile() == null)
            {
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ())));
                // first move
                if (pieceCoordinate.getI() == 1){
                    if (pieces_Array[pieceCoordinate.getI() + 2][pieceCoordinate.getJ()].getPieceOnTile() == null){
                        candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 2, pieceCoordinate.getJ())));
                    }
                }
            }
            if(pieceCoordinate.getJ()  + 1 < pieces_Array[pieceCoordinate.getI()].length){
                if (pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() + 1].getPieceOnTile() != null && pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() + 1)));
                }
            }
            if(pieceCoordinate.getJ() - 1>= 0){
                if(pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() - 1].getPieceOnTile() != null && pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() - 1)));
                }
            }
        }

        else if(this.color == Color.WHITE){
            if(pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ()].getPieceOnTile() == null)
            {
                // first move
                if (pieceCoordinate.getI() == 6){
                    if (pieces_Array[pieceCoordinate.getI() - 2][pieceCoordinate.getJ()].getPieceOnTile() == null){
                        candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 2, pieceCoordinate.getJ())));
                    }
                }
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ())));
            }
            if (pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
                if (pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() + 1].getPieceOnTile() != null && pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() + 1)));
                }
            }
            if(pieceCoordinate.getJ() - 1>= 0){
                if(pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() - 1].getPieceOnTile() != null && pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() - 1)));
                }
            }
        }
        return candidateLegalMoves;
    }

}
