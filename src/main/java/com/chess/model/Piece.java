package com.chess.model;

import java.util.List;

public abstract class Piece {
    public enum Color { WHITE, BLACK }
    public enum Type { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }

    private final Color color;
    private final Type type;
    private Position position;

    protected Piece(Color color, Type type, Position position) {
        this.color = color;
        this.type = type;
        this.position = position;
    }

    public abstract List<Position> getValidMoves(BoardState boardState);

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s %s at %s", color, type, position);
    }
} 