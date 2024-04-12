package com.example.chessproject.ChessPieces;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Coordinate;
import com.example.chessproject.BoardUtilities.Tile;
import com.example.chessproject.BoardUtilities.Move;

import java.util.ArrayList;

public abstract class Piece {
    // color
    protected Color color;

    public Piece(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract ArrayList<Move> calculateLegalMoves(Coordinate pieceCoordinate, Tile[][] pieces_Array);
}
