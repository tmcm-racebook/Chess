package com.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean hasMoved;
    private boolean justMadeDoubleMove;

    public Pawn(Color color, Position position) {
        super(color, Type.PAWN, position);
        hasMoved = false;
        justMadeDoubleMove = false;
    }

    @Override
    public List<Position> getValidMoves(BoardState boardState) {
        List<Position> validMoves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        int direction = getColor() == Color.WHITE ? -1 : 1; // White moves up (-1), Black moves down (+1)

        // Forward move - can only move forward if the square is empty
        Position oneStep = new Position(row + direction, col);
        if (boardState.isValidPosition(oneStep) && boardState.getPieceAt(oneStep) == null) {
            validMoves.add(oneStep);

            // Double move from starting position - can only move if both squares are empty
            if (!hasMoved) {
                Position twoSteps = new Position(row + 2 * direction, col);
                if (boardState.isValidPosition(twoSteps) && 
                    boardState.getPieceAt(twoSteps) == null && 
                    boardState.getPieceAt(oneStep) == null) {
                    validMoves.add(twoSteps);
                }
            }
        }

        // Diagonal capture moves - can only capture diagonally
        Position[] capturePositions = {
            new Position(row + direction, col - 1),
            new Position(row + direction, col + 1)
        };

        for (Position capturePos : capturePositions) {
            if (boardState.isValidPosition(capturePos)) {
                Piece targetPiece = boardState.getPieceAt(capturePos);
                // Only add capture move if there's an opponent's piece
                if (targetPiece != null && targetPiece.getColor() != getColor()) {
                    validMoves.add(capturePos);
                }
            }
        }

        // En passant captures
        if ((getColor() == Color.WHITE && row == 3) || (getColor() == Color.BLACK && row == 4)) {
            Position[] adjacentPositions = {
                new Position(row, col - 1),
                new Position(row, col + 1)
            };

            for (Position adjPos : adjacentPositions) {
                if (boardState.isValidPosition(adjPos)) {
                    Piece adjacentPiece = boardState.getPieceAt(adjPos);
                    if (adjacentPiece instanceof Pawn && 
                        adjacentPiece.getColor() != getColor() && 
                        ((Pawn) adjacentPiece).justMadeDoubleMove) {
                        // En passant capture position
                        Position enPassantPos = new Position(row + direction, adjPos.getCol());
                        // Make sure the en passant square is empty
                        if (boardState.getPieceAt(enPassantPos) == null) {
                            validMoves.add(enPassantPos);
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    @Override
    public void setPosition(Position position) {
        Position oldPos = getPosition();
        super.setPosition(position);
        // Only set hasMoved if this is an actual move (not initial placement)
        if (oldPos != null) {
            hasMoved = true;
            justMadeDoubleMove = Math.abs(position.getRow() - oldPos.getRow()) == 2;
        }
    }

    public boolean hasJustMadeDoubleMove() {
        return justMadeDoubleMove;
    }

    public void clearDoubleMovedFlag() {
        justMadeDoubleMove = false;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
} 