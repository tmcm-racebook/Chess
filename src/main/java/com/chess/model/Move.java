package com.chess.model;

public class Move {
    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece capturedPiece;
    private final boolean wasPromotion;

    public Move(Position from, Position to, Piece piece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.wasPromotion = false;
    }

    public Move(Position from, Position to, Piece piece, Piece capturedPiece, boolean wasPromotion) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.wasPromotion = wasPromotion;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public boolean wasPromotion() {
        return wasPromotion;
    }

    @Override
    public String toString() {
        String moveStr = piece.getType() + " " + from + " -> " + to;
        if (capturedPiece != null) {
            moveStr += " captures " + capturedPiece.getType();
        }
        if (wasPromotion) {
            moveStr += " (promotion)";
        }
        return moveStr;
    }
} 