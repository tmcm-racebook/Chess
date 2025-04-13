package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean hasMoved;

    public Rook(Color color, Position position) {
        super(color, Type.ROOK, position);
        hasMoved = false;
    }

    @Override
    public List<Position> getValidMoves(BoardState boardState) {
        List<Position> validMoves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        // Check moves in all four directions
        checkDirection(boardState, validMoves, row, col, 1, 0);   // Down
        checkDirection(boardState, validMoves, row, col, -1, 0);  // Up
        checkDirection(boardState, validMoves, row, col, 0, 1);   // Right
        checkDirection(boardState, validMoves, row, col, 0, -1);  // Left

        return validMoves;
    }

    private void checkDirection(BoardState boardState, List<Position> validMoves, 
                              int startRow, int startCol, int rowDelta, int colDelta) {
        int currentRow = startRow + rowDelta;
        int currentCol = startCol + colDelta;

        while (true) {
            Position pos = new Position(currentRow, currentCol);
            if (!boardState.isValidPosition(pos)) {
                break;
            }

            Piece piece = boardState.getPieceAt(pos);
            if (piece == null) {
                validMoves.add(pos);
            } else {
                if (piece.getColor() != getColor()) {
                    validMoves.add(pos); // Can capture opponent's piece
                }
                break; // Stop at first piece encountered
            }

            currentRow += rowDelta;
            currentCol += colDelta;
        }
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
    }

    public void makeMove(Position position) {
        super.setPosition(position);
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
} 