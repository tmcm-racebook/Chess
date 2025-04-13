package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Color color, Position position) {
        super(color, Type.KNIGHT, position);
    }

    @Override
    public List<Position> getValidMoves(BoardState boardState) {
        List<Position> validMoves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        // All possible L-shaped moves
        int[][] moves = {
            {row + 2, col + 1}, {row + 2, col - 1},
            {row - 2, col + 1}, {row - 2, col - 1},
            {row + 1, col + 2}, {row + 1, col - 2},
            {row - 1, col + 2}, {row - 1, col - 2}
        };

        for (int[] move : moves) {
            Position pos = new Position(move[0], move[1]);
            if (boardState.isValidPosition(pos)) {
                Piece piece = boardState.getPieceAt(pos);
                if (piece == null || piece.getColor() != getColor()) {
                    validMoves.add(pos);
                }
            }
        }

        return validMoves;
    }
} 