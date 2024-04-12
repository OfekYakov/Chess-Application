package com.example.chessproject.BoardUtilities;

public class Move {
    private Coordinate startCoordinate;
    private Coordinate CandidateCoordinate;

    public Move(Coordinate startCoordinate, Coordinate candidateCoordinate) {
        this.startCoordinate = startCoordinate;
        CandidateCoordinate = candidateCoordinate;
    }

    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(Coordinate startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Coordinate getCandidateCoordinate() {
        return CandidateCoordinate;
    }

    public void setCandidateCoordinate(Coordinate candidateCoordinate) {
        CandidateCoordinate = candidateCoordinate;
    }
}
