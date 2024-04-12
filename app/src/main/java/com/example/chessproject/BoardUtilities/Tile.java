package com.example.chessproject.BoardUtilities;

import com.example.chessproject.ChessPieces.Piece;

public class Tile {
    private Piece pieceOnTile;

    public Tile(Piece pieceOnTile) {
        this.pieceOnTile = pieceOnTile;
    }

    public Piece getPieceOnTile() {
        return pieceOnTile;
    }

    public void setPieceOnTile(Piece pieceOnTile) {
        this.pieceOnTile = pieceOnTile;
    }
}
