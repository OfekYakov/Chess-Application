package com.example.chessproject.ChessPieces;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Coordinate;
import com.example.chessproject.BoardUtilities.Move;
import com.example.chessproject.BoardUtilities.Tile;

import java.util.ArrayList;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public ArrayList<Move> calculateLegalMoves(Coordinate pieceCoordinate, Tile[][] pieces_Array) {
        ArrayList<Move> candidateLegalMoves = new ArrayList<>();

        // left
        if(pieceCoordinate.getJ() - 1 >= 0){
            if(pieces_Array[pieceCoordinate.getI()][pieceCoordinate.getJ() - 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI()][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.getColor()){
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI(), pieceCoordinate.getJ() - 1)));
            }
        }
        // right
        if(pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
            if(pieces_Array[pieceCoordinate.getI()][pieceCoordinate.getJ() + 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI()][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.getColor()){
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI(), pieceCoordinate.getJ() + 1)));
            }
        }


        // down
        if(pieceCoordinate.getI() + 1 < pieces_Array.length){
            if(pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ()].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ()].getPieceOnTile().getColor() != this.getColor()){
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ())));
            }

            // down - right
            if(pieceCoordinate.getJ() - 1 >= 0){
                if(pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() - 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() - 1)));
                }
            }

            // down - left
            if(pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
                if(pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() + 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() + 1)));
                }
            }
        }

        // up
        if(pieceCoordinate.getI() - 1 >= 0){
            if(pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ()].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ()].getPieceOnTile().getColor() != this.getColor()){
                candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ())));
            }

            // up - right
            if(pieceCoordinate.getJ() - 1 >= 0){
                if(pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() - 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() - 1)));
                }
            }

            // up - left
            if(pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
                if(pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() + 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.getColor()){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() + 1)));
                }
            }

        }

        return candidateLegalMoves;
    }


}
