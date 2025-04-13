package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Color color, Position position) {
        super(color, Type.QUEEN, position);
    }

    @Override
    public List<Position> getValidMoves(BoardState boardState) {
        List<Position> validMoves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        // Horizontal and vertical moves (like Rook)
        checkDirection(boardState, validMoves, row, col, 1, 0);   // Down
        checkDirection(boardState, validMoves, row, col, -1, 0);  // Up
        checkDirection(boardState, validMoves, row, col, 0, 1);   // Right
        checkDirection(boardState, validMoves, row, col, 0, -1);  // Left

        // Diagonal moves (like Bishop)
        checkDirection(boardState, validMoves, row, col, 1, 1);   // Down-Right
        checkDirection(boardState, validMoves, row, col, 1, -1);  // Down-Left
        checkDirection(boardState, validMoves, row, col, -1, 1);  // Up-Right
        checkDirection(boardState, validMoves, row, col, -1, -1); // Up-Left

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
} 