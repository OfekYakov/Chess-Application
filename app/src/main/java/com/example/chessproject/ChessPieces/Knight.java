package com.example.chessproject.ChessPieces;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Coordinate;
import com.example.chessproject.BoardUtilities.Move;
import com.example.chessproject.BoardUtilities.Tile;

import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
    }

    @Override
    public ArrayList<Move> calculateLegalMoves(Coordinate pieceCoordinate, Tile[][] pieces_Array) {
        ArrayList<Move> candidateLegalMoves = new ArrayList<>();

       // up
       if(pieceCoordinate.getI() - 2 >= 0){
           if(pieceCoordinate.getJ() - 1 >= 0){
               if(pieces_Array[pieceCoordinate.getI() - 2][pieceCoordinate.getJ() - 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 2][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.color){
                   candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 2, pieceCoordinate.getJ() - 1)));
               }
           }
           if(pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
               if(pieces_Array[pieceCoordinate.getI() - 2][pieceCoordinate.getJ() + 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 2][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.color){
                   candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 2, pieceCoordinate.getJ() + 1)));
               }
           }
       }

       // down
        if(pieceCoordinate.getI() + 2 < pieces_Array.length){
            if(pieceCoordinate.getJ() - 1 >= 0){
                if(pieces_Array[pieceCoordinate.getI() + 2][pieceCoordinate.getJ() - 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 2][pieceCoordinate.getJ() - 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 2, pieceCoordinate.getJ() - 1)));
                }
            }
            if(pieceCoordinate.getJ() + 1 < pieces_Array[pieceCoordinate.getI()].length){
                if(pieces_Array[pieceCoordinate.getI() + 2][pieceCoordinate.getJ() + 1].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 2][pieceCoordinate.getJ() + 1].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 2, pieceCoordinate.getJ() + 1)));
                }
            }
        }

        // left
        if(pieceCoordinate.getJ() - 2 >= 0){
            if(pieceCoordinate.getI() - 1 >= 0){
                if(pieces_Array[pieceCoordinate.getI() - 1 ][pieceCoordinate.getJ() - 2].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() - 2].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() - 2)));
                }
            }
            if(pieceCoordinate.getI() + 1 < pieces_Array.length){
                if(pieces_Array[pieceCoordinate.getI() + 1 ][pieceCoordinate.getJ() - 2].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() - 2].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() - 2)));
                }
            }
        }

        // right
        if(pieceCoordinate.getJ() + 2 < pieces_Array[pieceCoordinate.getI()].length){
            if(pieceCoordinate.getI() - 1 >= 0){
                if(pieces_Array[pieceCoordinate.getI() - 1 ][pieceCoordinate.getJ() + 2].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() - 1][pieceCoordinate.getJ() + 2].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() - 1, pieceCoordinate.getJ() + 2)));
                }
            }
            if(pieceCoordinate.getI() + 1 < pieces_Array.length){
                if(pieces_Array[pieceCoordinate.getI() + 1 ][pieceCoordinate.getJ() + 2].getPieceOnTile() == null || pieces_Array[pieceCoordinate.getI() + 1][pieceCoordinate.getJ() + 2].getPieceOnTile().getColor() != this.color){
                    candidateLegalMoves.add(new Move(pieceCoordinate, new Coordinate(pieceCoordinate.getI() + 1, pieceCoordinate.getJ() + 2)));
                }
            }
        }

        return candidateLegalMoves;
    }


}
